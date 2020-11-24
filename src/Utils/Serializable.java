package Utils;

public interface Serializable {

    public static String SEPARATOR = ",";

    public String serialize();
    public void unserialize(String string);
}
