package Client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    private static SceneController ourInstance = new SceneController();

    public static SceneController getInstance() {
        return ourInstance;
    }

    private Stage primaryStage;
    private Scene loginScene;
    private Scene regFormScene;
    private Scene mainformScene;

    private SceneController() {
    }

    public void initializeSC(Stage primaryStage){
        this.primaryStage = primaryStage;
        try
        {
            FXMLLoader loginPane = new FXMLLoader(getClass().getResource("/Client/Welcome.fxml"));
            Parent lp = loginPane.load();
            this.loginScene = new Scene(lp);

            FXMLLoader mainformPane = new FXMLLoader(getClass().getResource("/Client/Mainform.fxml"));
            Parent mp = mainformPane.load();
            this.mainformScene = new Scene(mp);

            FXMLLoader regPane = new FXMLLoader(getClass().getResource("/Client/Regform.fxml"));
            Parent rp = regPane.load();
            this.regFormScene = new Scene(rp);

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void startPrimary(){
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public void openLoginScene(){
        primaryStage.setScene(loginScene);
    }

    public void openRegistrationForm(){
        primaryStage.setScene(regFormScene);
    }

    public void openMainScene(){
        primaryStage.setScene(mainformScene);
    }

}
