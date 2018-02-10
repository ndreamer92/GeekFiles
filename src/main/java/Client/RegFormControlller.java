package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegFormControlller {
    SceneController sc = SceneController.getInstance();
    NetworkClient nc = NetworkClient.getInstance();

    @FXML
    TextField fieldLogin;
    @FXML
    PasswordField fieldPass;
    @FXML
    PasswordField fieldPassConfirm;

    public void onPressBtnConfirm(ActionEvent actionEvent) {
        if (fieldPass.getText().equalsIgnoreCase(fieldPassConfirm.getText()))
            nc.regRequest(fieldLogin.getText(),fieldPass.getText());
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Пароли не совпадают!");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка!");
            alert.showAndWait();
        }
    }

    public void onPressBtnCancel(ActionEvent actionEvent) {
        sc.openLoginScene();
    }
}
