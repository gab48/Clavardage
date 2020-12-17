package Clavardage.Network.SocketProtocols;

import Clavardage.Network.Models.Packet;
import Clavardage.Network.Types.ProtocolType;

public abstract class Socket {

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
    public abstract int send(Packet packet);
    public abstract Packet recv(Packet packet);
    public abstract int close();

}
