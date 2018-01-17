package zGBFCommon;

import java.io.Serializable;
import java.util.Date;

public class FSFile implements Serializable {
    private String name;
    private String path;
    private String extension;
    private long size;
    private long lastModified;
    private String ownerID;

    public FSFile(String name, String path, long size,long lastModified){
        this.name = name;
        this.path = path;
        this. size = size;
        this.lastModified = lastModified;
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

    public long getLastModified() {
        return lastModified;
    }

    @Override
    public String toString(){
        return ("Name: " + this.name + " Path: " + this.path + " Size: " + this.size);
    }
}
