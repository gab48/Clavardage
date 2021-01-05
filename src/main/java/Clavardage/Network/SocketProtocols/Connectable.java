package Clavardage.Network.SocketProtocols;

import Clavardage.Network.Models.Address;

import java.io.IOException;
import java.net.Socket;

public interface Connectable {
    default Socket connect(Address addr) {
        try {
            return new Socket(addr.getIp(), addr.getPort());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Connection refused");
        }
        return null;
    }
}
