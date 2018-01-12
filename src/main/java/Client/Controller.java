package Client;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;

public class Controller {
    BaseClient bc;

    @FXML
    public void onPressConnect(ActionEvent actionEvent) {
        bc = new BaseClient();
        bc.connect("localhost",8189);
        bc.clientActivityStart();
    }

    @FXML
    public void onPressAuth(ActionEvent actionEvent) {
        bc.authorize("login1","pass1");
    }

    public void onPressGetList(ActionEvent actionEvent) {
        bc.getFileList();
    }
}
