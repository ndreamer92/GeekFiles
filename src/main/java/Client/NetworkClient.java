package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import zGBFCommon.GBFUser;

public class NetworkClient {
    private static NetworkClient ourInstance = new NetworkClient();
    Logger log;
    GBFUser gbfUser;
    private ArrayList<NCEventListener> listeners = new ArrayList<NCEventListener>();
    private Socket socket;
    private DataInputStream in;
    private ObjectInputStream objin;
    private DataOutputStream out;
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
            objin = new ObjectInputStream(socket.getInputStream());
            log.info("Успешное подключение!");
            fireOnSuccesfullConnection();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void authorize(String login, String pwd){
        try {//попытка авторизации
            out.writeUTF("/auth " + login + " " + pwd);

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

            new Thread(()-> {//Ловим ответ от сервера
                while (true){
                    try {
                        Object msg = objin.readObject();
                        gbfUser = (GBFUser)msg;
                        System.out.println(gbfUser.getFileList().get(0).getName());
                        fireFileListChanged();
                        System.out.println(msg.toString());
                    }catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String[] getFilelist() {
        return null;
    }

}
