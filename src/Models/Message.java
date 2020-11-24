package Models;

import Utils.Serializable;

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

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public String serialize() {
        return this.content + Serializable.SEPARATOR + this.timestamp;
    }

    @Override
    public void unserialize(String string) {
        String[] result = string.split(Serializable.SEPARATOR+"(?!.*"+Serializable.SEPARATOR+")");
        System.out.println(result.length);
        System.out.println(result[0]);
        this.content = result[0];
        this.timestamp = Long.decode(result[1]);
    }
}
