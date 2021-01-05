package Clavardage.Models;

import Clavardage.Network.Models.Address;
import Clavardage.Utils.Serializable;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {

    private String content;
    private String sender;
    private Timestamp timestamp;

    public Message(String sender, String content, Timestamp created_at) {
        this.content = content;
        this.sender = sender;
        this.timestamp = created_at;
    }

    public Message (String content, Long timestampL) {
        this.content = content;
        if (timestampL != null) {
            this.timestamp = new Timestamp(timestampL);
        }
    }

    public Message (String content) {
        this(content, (new Date()).getTime());
    }
    public Message () {
        this(null, null);
    }

    public String getContent() {
        return content;
    }
    public String getSender() {return sender; }
    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    public String getTime() {
        Date date = new Date(this.timestamp.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(date);
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", sender="+ sender +
                '}';
    }

    @Override
    public byte[] serialize() {
        return (this.content + Serializable.SEPARATOR + this.timestamp.getTime()).getBytes();
    }

    @Override
    public void unserialize(byte[] bytes) {
        String string = new String(bytes);
        String[] result = string.split(Serializable.SEPARATOR+"(?!.*"+Serializable.SEPARATOR+")");
        this.content = result[0];
        this.timestamp = new Timestamp(Long.decode(result[1]));
    }
}
