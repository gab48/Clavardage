package Clavardage.Network.SocketProtocols;

import Clavardage.Models.User;
import Clavardage.Network.Models.Packet;
import Clavardage.Network.Models.Address;
import Clavardage.Network.Types.ProtocolType;

public abstract class Socket {

    protected ProtocolType protocol;
    protected short localPort;

    public static int listen() { // Server
        // Server Thread
        // New thread for each inbound connections
        return 0;
    }

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
