package Clavardage.Network.SocketProtocols;

import Clavardage.Network.Models.Address;
import Clavardage.Network.Models.MessagePacket;

import java.io.IOException;
import java.net.Socket;

public class TCPSendSocket extends TCPsocket<MessagePacket> {

    public Socket connect(Address addr) {
        this.remoteAddr = addr;
        this.link = super.connect(addr);
        this.streams = new TCPStreams<>(this.link);
        return this.link;
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
