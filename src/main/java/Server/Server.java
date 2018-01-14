package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Vector;
import java.util.logging.Logger;

public class Server {
    private final int PORT = 8189;
    private Vector<ClientHandler> clients;
    private AuthService authService;
    private Logger log;
    public AuthService getAuthService(){
        return authService;
    }
    public Server(){
        log = Logger.getLogger("ClientHandler");
        ServerSocket server = null;
        Socket socket = null;
        authService = null;
        clients = new Vector<>();
        try{
            server = new ServerSocket(PORT);
            authService = new BaseAuthService();
            authService.start();
            log.info("Сервер запущен, ждем клиентов");
            while(true){
                socket = server.accept(); //режим ожидания, возвращает объект типа сокет, блокирует выполнение кода
                subscribeMe(new ClientHandler(socket, this));
                log.info(LocalDateTime.now() + " Клиент подключился");
            }
        }catch(IOException e){
            e.printStackTrace();
            log.warning("Не удалось запустить сервер");

        }finally{
            try{
                socket.close();
                server.close();
            }catch(IOException e){
                e.printStackTrace();
            }
            authService.stop();
        }
    }
    public synchronized boolean isNickBusy(String nick){
        for(ClientHandler c: clients){
            if(c.getName().equals(nick)) return true;
        }
        return false;
    }
    public void broadcast(String msg){
        for(ClientHandler c: clients){
            c.sendMessage(msg);
        }
    }

    public void subscribeMe(ClientHandler c){
        clients.add(c);
    }
    public void unsubscribeMe(ClientHandler c){
        clients.remove(c);
    }
}
