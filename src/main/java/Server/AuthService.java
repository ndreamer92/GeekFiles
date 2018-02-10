package Server;

public interface AuthService {
    void start();
    void stop();
    String authorizeByLogPass(String login, String pass);
    boolean regAttempt(String login, String pass);
}
