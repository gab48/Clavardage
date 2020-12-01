import Models.User;
import Network.CCP.CCPPacket;
import Network.Utils.CCPPacketType;

public class Test {
    public static void main(String[] args) {
        String res = "[micheldu31=192.168.2.140]";
        System.out.println(res.matches("\\[(.*)]"));
    }
}
