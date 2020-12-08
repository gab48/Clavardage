import Clavardage.Network.Models.Address;
import Clavardage.Utils.Xor;

public class Test {
    public static void main(String[] args) {
        byte[] test = Xor.compute(Address.getMyIP().getIp().getAddress(),Address.getMulticast().getIp().getAddress());
        System.out.println(Address.getMyIP().getIp().getHostAddress());
        for(byte b : Address.getMyIP().getIp().getAddress()) {
            int tmp = Integer.valueOf(b)+128;
            System.out.print(tmp + " ");
        }
    }
}
