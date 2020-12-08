import Clavardage.Network.Models.Address;
import Clavardage.Utils.Xor;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test {
    public static void main(String[] args) throws UnknownHostException {

        System.out.println("Int " + 127 + " to Byte "+ (byte) 127);
        byte[] ret = new byte[4];
        byte[] key = new byte[] {(byte) 255, (byte) 255, (byte) 255, (byte) 255};
        byte[] addr = Address.getMyIP().getIp().getAddress();
        System.out.println("My IP address : " + InetAddress.getByAddress(addr));
        byte[] test = Xor.compute(addr, key);
        String id = new String(test);
        System.out.println("Encryption : " + id);
        byte[] out = id.getBytes();
        System.out.println(out.length);
        test = Xor.compute(out, key);
        System.out.println("Decrypted : " + InetAddress.getByAddress(test));


        int i = 0;
        for(byte b : Address.getMyIP().getIp().getAddress()) {
            ret[i] = b;
            i++;
        }
        for(byte a : ret) {
            System.out.println(Integer.valueOf(a));
        }
    }
}
