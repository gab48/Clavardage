package Network.SocketProtocols;

import Network.Models.Packet;
import Network.Models.Address;
import Network.Types.ProtocolType;

public abstract class Socket {

    protected ProtocolType protocol;
    protected Address remoteAddr;
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

    public int connect(String ip, short port) {
        return connect(new Address(ip, port));
    }

    public int connect(Address addr) {
        this.remoteAddr = addr;
        return 0;
    }

    public abstract int accept();
    public abstract int send(Packet packet);
    public abstract Packet recv(Packet packet);
    public abstract int close();

}
