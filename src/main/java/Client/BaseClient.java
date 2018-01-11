package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BaseClient {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
        private boolean isConnected;
        private boolean isAuthorized;
        private int state;

        public BaseClient(){

        }

        public void connect (String host, int port){//Подключаемся
            try{
                socket = new Socket("localhost", 8189);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                isConnected = true;
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        public boolean getConnectionState(){
            return isConnected;
        }

        public void authorize(String login, String pwd){
            try {//попытка авторизации
                out.writeUTF("/auth" + login + " " + pwd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public boolean getAuthorizationState(){
            return isAuthorized;
        }

        public void clientActivityStart(){//основная деятельность клиентского приложения
            new Thread(()->{//Рабочий поток
                while(true){
                    try {
                        String msg = in.readUTF();
                        if (msg.startsWith("/authok")){
                            System.out.println("yoba");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }

}
