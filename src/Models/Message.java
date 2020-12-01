package Models;

import Utils.Serializable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {

    private String content;
    private Long timestamp;

    public Message (String content, Long timestamp) {
        this.content = content;
        this.timestamp = timestamp;
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

    public Long getTimestamp() {
        return timestamp;
    }

    public String getTime() {
        Date date = new Date(this.getTimestamp());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(date);
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public byte[] serialize() {
        return (this.content + Serializable.SEPARATOR + this.timestamp).getBytes();
    }

    @Override
    public void unserialize(byte[] bytes) {
        String string = new String(bytes);
        String[] result = string.split(Serializable.SEPARATOR+"(?!.*"+Serializable.SEPARATOR+")");
        System.out.println(result.length);
        System.out.println(result[0]);
        this.content = result[0];
        this.timestamp = Long.decode(result[1]);
    }
}
