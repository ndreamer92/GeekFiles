package Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.springframework.format.datetime.joda.DateTimeParser;
import zGBFCommon.FSFile;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
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

    //Event Listeners
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
    /////


    @FXML
    public void onPressGetList(ActionEvent actionEvent) {
        nc.addListener(this);
        nc.refreshFileList();
    }




    public void onFileListClick(MouseEvent mouseEvent) {
        FSFile tFile = nc.gbfUser.getFileByName(fileList.getSelectionModel().getSelectedItem().toString());
        lbFileName.setText(tFile.getName());
        lbFileSize.setText(tFile.getSize() / 1024+ "KB");

        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        lbModified.setText(format.format(tFile.getLastModified()));
    }

    public void onDeleteFile(ActionEvent actionEvent) {
        nc.deleteFileByName(fileList.getSelectionModel().getSelectedItem().toString());
    }
}
