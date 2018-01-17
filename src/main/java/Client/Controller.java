package Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import zGBFCommon.FSFile;

import java.util.ArrayList;

public class Controller  implements NCEventListener{
    NetworkClient nc = NetworkClient.getInstance();

    @FXML
    private ListView fileList;
    @FXML
    private Label lbFileName;
    @FXML
    private Label lbFileSize;
    @FXML
    private Label lbModified;

    @FXML
    public void onPressGetList(ActionEvent actionEvent) {
        nc.addListener(this);
        nc.refreshFileList();
    }

    @Override
    public void onFileListRefresh() {
        ObservableList<String> items = FXCollections.observableArrayList(nc.getFilelist());
        fileList.setItems(items);
    }

    @Override
    public void onSuccesfullAuthorization() {

    }

    @Override
    public void onSuccesfullConnection() {

    }


    public void onFileListClick(MouseEvent mouseEvent) {
        //System.out.println(fileList.getSelectionModel().getSelectedItem().toString());
        FSFile tFile = nc.gbfUser.getFileByName(fileList.getSelectionModel().getSelectedItem().toString());
        lbFileName.setText(tFile.getName());
        lbFileSize.setText(tFile.getSize()/1024+ "KB");
        lbModified.setText("01/01.2018");
    }
}
