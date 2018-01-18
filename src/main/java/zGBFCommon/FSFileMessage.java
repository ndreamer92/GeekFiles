package zGBFCommon;

import java.io.Serializable;

public class FSFileMessage implements Serializable{
    private String name;
    private byte[] data;

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }

    public FSFileMessage(String name, byte[] data) {
        this.name = name;
        this.data = data;
    }
}
