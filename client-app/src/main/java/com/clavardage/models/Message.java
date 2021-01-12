package com.clavardage.models;

import com.clavardage.utils.Serializable;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {

    public enum MessageType { TEXT, FILE, FILE_ACK }

    private MessageType type;
    private String content;
    private String sender;
    private Timestamp timestamp;

    public Message(String sender, String content, Timestamp created_at, MessageType type) {
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.timestamp = created_at;
    }

    public Message(String content, Long timestampL, MessageType type) {
        this.content = content;
        this.type = type;
        if (timestampL != null) {
            this.timestamp = new Timestamp(timestampL);
        }
    }

    public Message (String content, Long timestampL) {
        this(content, timestampL, MessageType.TEXT);
    }

    public Message (String content) {
        this(content, (new Date()).getTime());
    }
    public Message () {
        this(null, null);
    }

    public MessageType getType()    { return type; }
    public String getContent()      { return content; }
    public String getSender()       { return sender; }
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
                ", type=" + type +
                '}';
    }

    @Override
    public byte[] serialize() {
        return (this.content + Serializable.SEPARATOR + this.type + Serializable.SEPARATOR + this.timestamp.getTime()).getBytes();
    }

    @Override
    public void unserialize(byte[] bytes) {
        String string = new String(bytes);
        String regex = Serializable.SEPARATOR+"(?!.*"+Serializable.SEPARATOR+")";

        String[] result = string.split(regex);
        this.timestamp = new Timestamp(Long.decode(result[1]));

        result = result[0].split(regex);
        this.type = MessageType.valueOf(result[1]);

        this.content = result[0];
    }
}
