package Network;

import Network.Utils.Address;
import Utils.Serializable;

public class Packet implements Serializable {

    public static final short DEFAULT_SRC_PORT = 1920;

    protected String payload;
    protected Address dest;
    protected Address src;

    public Packet(String pl) {
        this.src = Address.getMyIP();
        this.payload = pl;
    }

    public Packet() {
        this((String)null);
    }

    public Packet(byte[] bytes) {
        this.unserialize(bytes);
    }

    public String getPayload() {
        return payload;
    }

    public Address getDest() {
        return dest;
    }

    public Address getSrc() {
        return src;
    }

    @Override
    //public String serialize() {return payload;}
    public byte[] serialize() {
        return this.payload.getBytes();
    }

    @Override
    public void unserialize(byte[] bytes) {
        this.payload = new String(bytes);
    }
}
