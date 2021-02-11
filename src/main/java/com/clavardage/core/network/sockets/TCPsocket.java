package com.clavardage.core.network.sockets;

import com.clavardage.core.network.models.Address;
import com.clavardage.core.network.models.Packet;

public abstract class TCPsocket<T extends Packet> extends Socket<T> implements Connectable {

    protected java.net.Socket   link    = null;
    protected Address           remoteAddr;
    protected TCPStreams<T> streams = null;

    @Override
    public int send(T packet) {
        this.streams.send(packet);
        return 0;
    }

    @Override
    public T recv(T packet) {
        packet = this.streams.recv(packet);
        packet.setSrc(new Address(this.link.getInetAddress()));
        return packet;
    }
}
