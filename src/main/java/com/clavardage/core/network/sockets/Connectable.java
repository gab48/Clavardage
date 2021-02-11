package com.clavardage.core.network.sockets;

import com.clavardage.core.network.models.Address;

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
