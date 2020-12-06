package Clavardage.Network.SocketProtocols;

import Clavardage.Models.User;
import Clavardage.Network.Models.Address;
import Clavardage.Network.Models.Packet;

public abstract class TCPsocket extends Socket{

    protected java.net.Socket link    = null;
    protected Address remoteAddr;
    protected TCPStreams      streams = null;

    public int connect(Address addr) { return 0; }
    public int connect(String ip, short port) {
        return connect(new Address(ip, port));
    }
    public int connect(User remoteUser) {
        return connect(remoteUser.getAddr());
    }

    @Override
    public int send(Packet packet) {
        this.streams.send(packet);
        return 0;
    }

    @Override
    public Packet recv(Packet packet) {
        packet = this.streams.recv(packet);
        packet.setSrc(new Address(this.link.getInetAddress()));
        return packet;
    }

}
