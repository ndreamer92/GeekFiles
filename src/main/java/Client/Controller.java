package Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.ListView;

public class Controller  implements NCEventListener{
    //BaseClient bc;
    NetworkClient nc = NetworkClient.getInstance();

    @FXML
    private ListView fileList;

    @FXML
    public void onPressConnect(ActionEvent actionEvent) {
        nc.addListener(this);
    }

    @FXML
    public void onPressAuth(ActionEvent actionEvent) {
        nc.authorize("login1","pass1");
    }

    @FXML
    public void onPressGetList(ActionEvent actionEvent) {
        nc.refreshFileList();
    }

    @Override
    public void onFileListRefresh() {
        String[] fl = null;
        fl = nc.getFilelist();
        ObservableList<String> items = FXCollections.observableArrayList(fl);
        fileList.setItems(items);
    }

    @Override
    public void onSuccesfullAuthorization() {

    }

    @Override
    public void onSuccesfullConnection() {

    }


}
