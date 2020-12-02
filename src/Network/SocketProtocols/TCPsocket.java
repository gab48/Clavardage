package Network.SocketProtocols;

import Network.Models.Packet;

public class TCPsocket extends Socket{

    @Override
    public int accept() {
        return 0;
    }

    @Override
    public int send(Packet packet) {
        return 0;
    }

    @Override
    public Packet recv(Packet packet) {
        return null;
    }

    @Override
    public int close() {
        return 0;
    }
}
