import Clavardage.Utils.Config;

public class Test {
    public static void main(String[] args) {
        System.out.println(Short.parseShort(Config.get("NETWORK_TCP_FILE_PORT")));
    }
}
