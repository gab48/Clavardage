package Clavardage.Utils;

import java.nio.charset.StandardCharsets;

public class Xor {

    public static byte[] compute(byte[] A, byte[] B) {
        byte[] res = null;
        int size = A.length;
        if (size != B.length) {
            System.err.println("Xor error");
        } else {
            res = new byte[size];
            int i = 0;
            for(byte b : A) {
                res[i] = (byte) (b ^ B[i++]);
            }
        }

        return res;
    }
}
