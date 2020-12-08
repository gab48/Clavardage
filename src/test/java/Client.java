import Clavardage.Utils.Config;

public class Client {

    public static void main(String[] args) {
        System.out.println(Boolean.parseBoolean(Config.get("MULTICAST")));
    }
}