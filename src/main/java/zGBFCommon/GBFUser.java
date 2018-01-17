package zGBFCommon;

import java.io.Serializable;
import java.util.LinkedList;

public class GBFUser implements Serializable {
    private String name;
    private String uid;
    private LinkedList<FSFile> fileList;

    public GBFUser(String name, String uid, LinkedList<FSFile> fileList){
        this.name = name;
        this.uid = uid;
        this.fileList = fileList;
    }

    public void setFileList(LinkedList<FSFile> fileList) {
        this.fileList = fileList;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public LinkedList<FSFile> getFileList() {
        return fileList;
    }

    public FSFile getFileByName (String fileName){
        for (int i = 0; i < fileList.size(); i++) {
            if (fileList.get(i).getName().equalsIgnoreCase(fileName))
                return fileList.get(i);
        }
        return null;
    }
}
