package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class WelcomeController implements NCEventListener{
    NetworkClient nc = NetworkClient.getInstance();

    @FXML
    private Button bAuthorize;
    @FXML
    private TextField fieldUserID;
    @FXML
    private PasswordField fieldPassword;

    @FXML
    private Scene secondScene;
    @FXML

    public void setSecondScene(Scene scene) {
        secondScene = scene;
    }

    public void openMainForm(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(secondScene);
    }

    public void onPressConnect(ActionEvent actionEvent) {
        nc.addListener(this);
        nc.connect("localhost",8189);
    }

    public void onPressAuthorize(ActionEvent actionEvent) {
        nc.authorize(fieldUserID.getText(),fieldPassword.getText());
        openMainForm(actionEvent);
    }

    @Override
    public void onFileListRefresh() {

    }

    @Override
    public void onSuccesfullAuthorization() {

    }

    @Override
    public void onSuccesfullConnection() {
        bAuthorize.setDisable(false);
        fieldUserID.setEditable(true);
        fieldPassword.setEditable(true);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успешное подключение");
        alert.setHeaderText(null);
        alert.setContentText("Установлено подключение к серверу ");
        alert.showAndWait();
    }
}
