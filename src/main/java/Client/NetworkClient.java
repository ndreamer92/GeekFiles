package Client;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.logging.Logger;

import zGBFCommon.Encryptor;
import zGBFCommon.FSFile;
import zGBFCommon.FSFileMessage;
import zGBFCommon.GBFUser;

public class NetworkClient {
    private static NetworkClient ourInstance = new NetworkClient();
    Logger log;
    GBFUser gbfUser;
    private ArrayList<NCEventListener> listeners = new ArrayList<NCEventListener>();
    private Socket socket;
    private DataInputStream in;
    private ObjectInputStream objin;
    private ObjectOutputStream objout;
    private DataOutputStream out;
    private Encryptor encryptor;
    private NetworkClient() {
        log = Logger.getLogger("ClientHandler");
    }

    public final static NetworkClient getInstance() {
        return ourInstance;
    }

    public void addListener(NCEventListener listener){
        listeners.add(listener);
    }

    public void removeListener(NCEventListener listener){
        listeners.remove(listener);
    }

    private void fireFileListChanged(){
        for (NCEventListener listener : listeners){
            listener.onFileListRefresh();
        }
    }

    private void fireOnSuccesfullAuthorization(){
        for (NCEventListener listener : listeners){
            listener.onSuccesfullAuthorization();
        }
    }

    private void fireOnSuccesfullConnection(){
        for (NCEventListener listener : listeners){
            listener.onSuccesfullConnection();
        }
    }

    public void connect (String host, int port){//Подключаемся
        try{
            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            log.info("Успешное подключение!");
            fireOnSuccesfullConnection();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void disconnect(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void authorize(String login, String pwd){
        try {//попытка авторизации
            out.writeUTF("/auth " + Encryptor.encrypt(login) + " " + Encryptor.encrypt(pwd));

            new Thread(()-> {//Ловим ответ от сервера
                while (true){
                    try {
                       String msg = in.readUTF();
                        if (msg.startsWith("/authok")){
                            fireOnSuccesfullAuthorization();
                            log.info("Успешная авторизация");
                        break;
                        }
                        if (msg.startsWith("/Authfail"))
                            log.info("Ошибка при авторизации");
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshFileList(){
        try {//Запрос списка файлов
            out.writeUTF("/getlist");
            log.info("Запрос списка файлов с сервера");
                    try {
                        objin = new ObjectInputStream(socket.getInputStream());
                        Object msg = objin.readObject();
                        gbfUser = (GBFUser)msg;
                    }catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }finally {
                        fireFileListChanged();
                    }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadFileFromServer(String filename){
        try {
            out.writeUTF("/download " + filename);
            log.info("Запрос файла с сервера");
            objin = new ObjectInputStream((socket.getInputStream()));
            FSFileMessage fm = (FSFileMessage) objin.readObject();
            Files.write(Paths.get("src/main/java/Client/FS/" + fm.getName()), fm.getData(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void uploadFileToServer(FSFile file){
        try {
            out.writeUTF("/loadnew");
            log.info("Загрузка нового файла на сервер");
            objout = new ObjectOutputStream((socket.getOutputStream()));
            FSFileMessage fm = new FSFileMessage(file.getName(),Files.readAllBytes(Paths.get(file.getPath())));
            objout.writeObject(fm);
            objout.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFileByName(String filename){
        try {
            out.writeUTF("/delete " + filename);
            log.info("Запрос на удаление файла " + filename);
            refreshFileList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getFilelist() {
        ArrayList<String> fl=new ArrayList();
        for (FSFile file:gbfUser.getFileList()) {
            fl.add(file.getName());
        }
        return fl;
    }

}
