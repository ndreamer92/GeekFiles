package Client;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WelcomeController {
    NetworkClient nc = NetworkClient.getInstance();

    private Scene secondScene;

    public void setSecondScene(Scene scene) {
        secondScene = scene;
    }

    public void openMainForm(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(secondScene);
    }

    public void onPressConnect(ActionEvent actionEvent) {
        nc.connect("localhost",8189);
    }

    public void onPressAuthorize(ActionEvent actionEvent) {
        nc.authorize("login1","pass1");
        openMainForm(actionEvent);
    }
}
