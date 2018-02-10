package Server;

import zGBFCommon.FSFile;
import zGBFCommon.FSFileMessage;
import zGBFCommon.GBFUser;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;

public class ClientHandler {
    Logger log;
    private Socket socket;
    private Server server;
    private DataOutputStream out;
    private ObjectOutputStream objout;
    private DataInputStream in;
    private ObjectInputStream objin;
    private String hash;
    private int state;
    private int prevState;
    private GBFUser gbfUser;
    private FileSystem fileSystem;

    public ClientHandler(Socket socket, Server server){
        try{
            log = Logger.getLogger("ClientHandler");
            this.socket = socket;
            this.server = server;
            hash = "";
            state = 0;
            fileSystem = FileSystem.getInstance();
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        }catch(IOException e){
            e.printStackTrace();
        }

        new Thread(()->{//Поток для клиента
            try{
                while(true){
                    String str = null;
                    try{
                        //Тут будет общение клиента с сервером
                        str = in.readUTF();

                        if(str.equalsIgnoreCase("/end")) {
                            log.info("Клиент завершил сеанс");
                            break;
                        }

                        switch (state){

                            case 0:
                                //Базовое состояние - ожидаем авторизацию
                                //При успешной авторизации - переходим в состояние работы (можем выполнять все функции по работе с файлами)
                                {
                                        if(str.startsWith("/auth")){
                                            log.info("Попытка авторизации от " + socket.getInetAddress());
                                            String[] elements = str.split(" ");
                                            System.out.println(str);
                                            String hash = server.getAuthService().authorizeByLogPass(elements[1], elements[2]);
                                            if(hash != null){
                                                if(!server.isNickBusy(hash)){
                                                    sendMessage("/authok" + hash);
                                                    this.hash = hash;
                                                    gbfUser = new GBFUser(elements[1],hash, fileSystem.getUserFileList(hash));
                                                    log.info(this.hash + " успешно авторизовался!");
                                                    state = 10;  //Смена состояния
                                                    break;
                                                }else sendMessage("/Authfail Учетная запись используется");
                                            }else sendMessage("/Authfail Не верные логин/пароль");
                                        };//else sendMessage("/Authfail Для начала нужно авторизоваться");

                                        if (str.startsWith("/reg")){
                                            log.info("Попытка регистрации от "  + socket.getInetAddress());
                                            String[] elements = str.split(" ");
                                            System.out.println(str);
                                            Boolean regResult = server.getAuthService().regAttempt(elements[1], elements[2]);
                                            if (regResult)
                                                sendMessage("/reg ok");
                                            else
                                                sendMessage("/reg fail");
                                        }
                                }
                                break;

                            //Основное состояние - в нем мы ждем команды (загрузка, скачивание, удаление, обновление, /перемещение)
                            case 10:
                            {
                                if (str != "")
                                    log.info(str);
                                if (str.startsWith("/getlist")) {
                                    log.info("Client " + hash + " запросил список файлов");
                                    gbfUser.setFileList(fileSystem.getUserFileList(gbfUser.getHash()));
                                    sendObject(gbfUser);
                                }

                                if (str.startsWith("/loadnew")) {
                                    log.info("Client " + hash + " инициириует загрузку нового файла на сервер");
                                    recieveFile();
                                }

                                if (str.startsWith("/download")) {
                                    log.info("Client " + hash + " инициириует загрузку файла с сервера");
                                    FSFile fsFile = gbfUser.getFileByName(str.replace("/download ",""));
                                    sendFile(fsFile);
                                }

                                if (str.startsWith("/delete")) {
                                    log.info("Client " + hash + " инициириует удаление файла");
                                    FSFile fsFile = gbfUser.getFileByName(str.replace("/delete ",""));
                                    File fFile = new File(fsFile.getPath());
                                    fFile.delete();
                                }

                            }
                        }


                    }catch(EOFException e){}
                    if (state != prevState)
                    log.info("Клиент " + hash + " изменил состояние алгоритма на: " + state);
                    prevState = state;
                }
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                server.unsubscribeMe(this);
                try{
                    socket.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(()->{//Поток будет проверять авторизован ли клиент
            try {
                Thread.sleep(120000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Если клиент не авторизовался - закрываем соединение
            if (this.hash.equalsIgnoreCase(""))
            {
                sendMessage("Время для авторизации вышло - соединение закрыто");
                log.info(" Клиент не авторизовался - отключаем");
                try{
                    socket.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void sendMessage(String msg){
        try{
            out.writeUTF(msg);
            out.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void sendObject(Object obj){
        try{
            objout = new ObjectOutputStream(socket.getOutputStream());
            objout.writeObject(obj);
            objout.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendFile(FSFile fileToSend){
        try {
            FSFileMessage fm = new FSFileMessage(fileToSend.getName(), Files.readAllBytes(Paths.get(fileToSend.getPath())));
            objout = new ObjectOutputStream(socket.getOutputStream());
            objout.writeObject(fm);
            objout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recieveFile(){
        try {
            objin = new ObjectInputStream(socket.getInputStream());
            FSFileMessage fm = (FSFileMessage) objin.readObject();
            Files.write(Paths.get("src/main/java/Server/FS/" + gbfUser.getHash() + "/" + fm.getName()), fm.getData(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {

        }
    }
    public String getHash(){
        return hash;
    }
}
