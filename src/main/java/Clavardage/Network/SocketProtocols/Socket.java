package Clavardage.Network.SocketProtocols;

import Clavardage.Network.Models.Packet;
import Clavardage.Network.Types.ProtocolType;

import java.io.IOException;

public abstract class Socket<T extends Packet> {

    protected ProtocolType protocol;
    protected final short localPort;

    public Socket(short localPort) {
        this.localPort = localPort;
    }

    public Socket(){
        this((short) 0);
    }

    public int accept() {
        return 0;
    }
    public abstract int send(T packet) throws IOException;
    public abstract T recv(T packet) throws IOException;
    public abstract int close();

}
