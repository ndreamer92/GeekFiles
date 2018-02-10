package zGBFCommon;

import java.io.Serializable;
import java.util.LinkedList;

public class GBFUser implements Serializable {
    private String name;
    private String hash;
    private LinkedList<FSFile> fileList;

    public GBFUser(String name, String hash, LinkedList<FSFile> fileList){
        this.name = name;
        this.hash = hash;
        this.fileList = fileList;
    }

    public void setFileList(LinkedList<FSFile> fileList) {
        this.fileList = fileList;
    }

    public String getName() {
        return name;
    }

    public String getHash() {
        return hash;
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
