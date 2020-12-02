package Clavardage.Network.SocketProtocols;

import Clavardage.Network.Models.Address;
import Clavardage.Network.Models.Packet;

public abstract class TCPsocket extends Socket{

    protected java.net.Socket link    = null;
    protected TCPStreams      streams = null;

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
