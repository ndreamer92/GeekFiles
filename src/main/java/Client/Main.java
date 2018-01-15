package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        //Создание сцен (авторизации и основной формы)
        FXMLLoader loginPane = new FXMLLoader(getClass().getResource("/Client/Welcome.fxml"));
        Parent lp = loginPane.load();
        Scene loginScene = new Scene(lp);

        FXMLLoader mainformPane = new FXMLLoader(getClass().getResource("/Client/Mainform.fxml"));
        Parent mp = mainformPane.load();
        Scene mainformScene = new Scene(mp);

        WelcomeController welcomeController = (WelcomeController) loginPane.getController();
        welcomeController.setSecondScene(mainformScene);

        primaryStage.setScene(loginScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
