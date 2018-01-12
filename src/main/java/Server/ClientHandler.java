package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientHandler {
    private Socket socket;
    private Server server;
    private DataOutputStream out;
    private DataInputStream in;
    private String name;
    private int state;
    private int prevState;

    public ClientHandler(Socket socket, Server server){
        try{
            this.socket = socket;
            this.server = server;
            name = "";
            state = 0;

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
                        System.out.println(str);

                        if(str.equalsIgnoreCase("/end")) {
                            break;
                        }

                        switch (state){

                            case 0:
                                //Базовое состояние - ожидаем авторизацию
                                //При успешной авторизации - переходим в состояние работы (можем выполнять все функции по работе с файлами)
                                {
                                        if(str.startsWith("/auth")){
                                            System.out.println("Попытка авторизации");
                                            String[] elements = str.split(" ");
                                            String nick = server.getAuthService().getNickByLoginPass(elements[1], elements[2]);
                                            if(nick != null){
                                                if(!server.isNickBusy(nick)){
                                                    sendMessage("/authok" + nick);
                                                    this.name = nick;
                                                    server.broadcast(this.name + " успешно авторизовался!");
                                                    state = 10;  //Смена состояния
                                                    break;
                                                }else sendMessage("Учетная запись используется");
                                            }else sendMessage("Не верные логин/пароль");
                                        }else sendMessage("Для начала нужно авторизоваться");

                                }
                                break;

                            //Основное состояние - в нем мы ждем команды (загрузка, скачивание, удаление, обновление, /перемещение)
                            case 10:
                            {

                                if (str.startsWith("/getlist")) {
                                    System.out.println("Client" + name + "запросил список файлов");
                                    sendMessage("/getlist 1.txt 2.txt 3.txt 4.txt");
                                }

                                if (str.startsWith("/loadnew")) {
                                    System.out.println("Client" + name + "инициириует загрузку нового файла на сервер");
                                }

                                if (str.startsWith("/download")) {
                                    System.out.println("Client" + name + "инициириует загрузку файла с сервера");
                                }

                                if (str.startsWith("/delete")) {
                                    System.out.println("Client" + name + "инициириует удаление файла");
                                }

                                if (str.startsWith("/update")) {
                                    System.out.println("Client" + name + "инициириует обновление файла");
                                }

                                if (str.startsWith("/move")) {
                                    System.out.println("Client" + name + "инициириует обновление файла");
                                }
                            }


                        }


                    }catch(EOFException e){}
                    if (state != prevState)
                        System.out.println("Client " + name + " changed state to: " + state);
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
            if (this.name.equalsIgnoreCase(""))
            {
                sendMessage("Время для авторизации вышло - соединение закрыто");
                System.out.println(LocalDateTime.now() + " Клиент не авторизовался - отключаем");
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
    public String getName(){
        return name;
    }
}
