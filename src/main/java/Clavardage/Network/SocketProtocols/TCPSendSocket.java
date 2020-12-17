package Clavardage.Network.SocketProtocols;

import Clavardage.Network.Models.Address;

import java.io.IOException;

public class TCPSendSocket extends TCPsocket{

    @Override
    public void connect(Address addr) {
        this.remoteAddr = addr;
        try {
            this.link = new java.net.Socket(this.remoteAddr.getIp(), this.remoteAddr.getPort());
            this.streams = new TCPStreams(this.link);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Connection refused");
        }
    }

    @Override
    public int close() {
        this.streams.close();
        try {
            this.link.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
