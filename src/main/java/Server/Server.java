package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Vector;

public class Server {
    private final int PORT = 8189;
    private Vector<ClientHandler> clients;
    private AuthService authService;
    public AuthService getAuthService(){
        return authService;
    }
    public Server(){
        ServerSocket server = null;
        Socket socket = null;
        authService = null;
        clients = new Vector<>();
        try{
            server = new ServerSocket(PORT);
            authService = new BaseAuthService();
            authService.start();
            System.out.println("Сервер запущен, ждем клиентов");
            while(true){
                socket = server.accept(); //режим ожидания, возвращает объект типа сокет, блокирует выполнение кода

                System.out.println(LocalDateTime.now() + " Клиент подключился");
            }
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Не удалось запустить сервер");
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
}
