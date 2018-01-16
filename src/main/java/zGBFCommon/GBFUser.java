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

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public LinkedList<FSFile> getFileList() {
        return fileList;
    }
}
