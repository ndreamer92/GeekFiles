package Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.format.datetime.joda.DateTimeParser;
import zGBFCommon.FSFile;

import java.io.File;
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

    public void onPressDownload(ActionEvent actionEvent) {
        nc.downloadFileFromServer(fileList.getSelectionModel().getSelectedItem().toString());
    }

    public void onPressLoad(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog((Stage)((Node)actionEvent.getSource()).getScene().getWindow());
        nc.uploadFileToServer(new FSFile(file.getName(),file.getPath(),file.length(),file.lastModified()));
        nc.refreshFileList();
    }

    //Drag-drop файла в лист из ОС
    public void onDragFile(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()){
            dragEvent.acceptTransferModes(TransferMode.COPY);
        }
        dragEvent.consume();
    }

    public void onDragDroppedFIle(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        boolean success = false;
        File file = db.getFiles().get(0);
        if (db.hasFiles()){
            nc.uploadFileToServer(new FSFile(file.getName(),file.getPath(),file.length(),file.lastModified()));
            nc.refreshFileList();
            success = true;
        }
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }
}
