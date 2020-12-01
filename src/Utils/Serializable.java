package Utils;

public interface Serializable {

    String SEPARATOR = ",";

    byte[] serialize();
    void unserialize(byte[] bytes);
}