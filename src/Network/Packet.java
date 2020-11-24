package Network;

import Utils.Serializable;

public class Packet implements Serializable {
    protected String payload;

    public Packet(String pl) {
        this.payload = pl;
    }

    public Packet() {
        this(null);
    }

    public String getPayload() {
        return payload;
    }

    @Override
    public String serialize() {
        return payload;
    }

    @Override
    public void unserialize(String string) {
        this.payload = string;
    }
}
