package zCommon;

import java.io.Serializable;
import java.util.Date;

public class FSFile implements Serializable {
    private String name;
    private String path;
    private String extension;
    private long size;
    private Date lastModified;
    private String ownerID;

    public FSFile(String name, String path, long size){
        this.name = name;
        this.path = path;
        this. size = size;
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public long getSize(){
        return this.size;
    }

    @Override
    public String toString(){
        return ("Name: " + this.name + " Path: " + this.path + " Size: " + this.size);
    }
}
