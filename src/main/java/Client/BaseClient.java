package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class BaseClient {
        Logger log;
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
        private boolean isConnected;
        private boolean isAuthorized;
        private int state,prevState;
        private String[] filelist;

        public BaseClient(){
            log = Logger.getLogger("BaseClient");
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
                out.writeUTF("/auth " + login + " " + pwd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public boolean getAuthorizationState(){
            return isAuthorized;
        }

        public void getFileList(){
            if (state == 10)
                state = 20;
        }

        public void clientActivityStart(){//основная деятельность клиентского приложения
            new Thread(()->{//Рабочий поток
                while(true){
                    try {
                        String msg = in.readUTF();
                        switch (state) {
                            case 0:
                                if (msg.startsWith("/authok")) {
                                    log.info("Успешная авторизация");
                                    isAuthorized = true;
                                    state = 20;
                                }
                                //break;

                            case 10://авторизованы, соеденены и ждем команды
;
                                //break;

                            case 20://Запрос списка файлов
                                out.writeUTF("/getlist");
                                state = 21; //Ждем ответ
                                //break;

                            case 21:
                                if (msg.startsWith("/getlist"))
                                    filelist = msg.split(" ");
                                System.out.println(filelist.toString());
                                    state=10;
                                //break;

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (state != prevState) {
                        log.info("Клиент изменил состояние c: " + prevState + " на " + state);
                    }

                }

            }).start();
            log.info("Поток закрылся!");
        }

}
