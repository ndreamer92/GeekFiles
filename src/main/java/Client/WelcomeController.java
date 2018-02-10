package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class WelcomeController implements NCEventListener{
    NetworkClient nc = NetworkClient.getInstance();
    SceneController sc = SceneController.getInstance();
    @FXML
    public static Stage STAGE;
    @FXML
    private Button bAuthorize;
    @FXML
    private TextField fieldUserID;
    @FXML
    private PasswordField fieldPassword;
    @FXML
    private Button btnReg;

    @FXML
    private void initialize(){
        if (nc.getConnectionState()){
            bAuthorize.setDisable(false);
            btnReg.setDisable(false);
            fieldUserID.setEditable(true);
            fieldPassword.setEditable(true);
        }

    }

    public void openMainForm(ActionEvent actionEvent) {
        sc.openMainScene();
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
        btnReg.setDisable(false);
        fieldUserID.setEditable(true);
        fieldPassword.setEditable(true);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успешное подключение");
        alert.setHeaderText(null);
        alert.setContentText("Установлено подключение к серверу ");
        alert.showAndWait();
    }

    public void onBtnReg(ActionEvent actionEvent) {
        sc.openRegistrationForm();
    }
}
