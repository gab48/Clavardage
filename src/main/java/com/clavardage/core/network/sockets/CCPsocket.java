package com.clavardage.core.network.sockets;

import com.clavardage.core.network.models.Address;
import com.clavardage.core.network.models.Packet;
import com.clavardage.core.network.types.ProtocolType;
import com.clavardage.core.utils.Config;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.Objects;

public class CCPsocket extends Socket<Packet>{

    protected static final int PAYLOAD_MAX_SIZE = 8192;
    protected DatagramSocket socket;

    public CCPsocket(short localPort) {
        super(localPort);
        this.protocol = ProtocolType.UDP;

        try {
            if (this.localPort > 1024) {
                if (Config.getBoolean("MULTICAST")) {
                    this.socket = new MulticastSocket(this.localPort);
                    ((MulticastSocket) this.socket).joinGroup(new InetSocketAddress(Objects.requireNonNull(Address.getMulticast()).getIp(), Config.getShort("NETWORK_CLAVARDAGE_PORT")), Address.getInterface());
                } else {
                    this.socket = new DatagramSocket(this.localPort);
                }
            } else {
                this.socket = new DatagramSocket();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CCPsocket() {
        this((short) 0);
    }

    @Override
    public int send(Packet packet) {
        int byteSent;
        byte[] payload = packet.serialize();

        DatagramPacket datagramPacket = new DatagramPacket(payload, payload.length, packet.getDest().getIp(), packet.getDest().getPort());
        datagramPacket.setData(payload);

        try {
            this.socket.send(datagramPacket);
            byteSent = datagramPacket.getLength();
        } catch (IOException e) {
            e.printStackTrace();
            byteSent = -1;
        }
        return byteSent;
    }

    @Override
    public Packet recv(Packet packet) {
        byte[] payload = new byte[PAYLOAD_MAX_SIZE];
        DatagramPacket datagramPacket = new DatagramPacket(payload, payload.length);

        try {
            this.socket.receive(datagramPacket);
            packet.unserialize(datagramPacket.getData());
            packet.setSrc(new Address(datagramPacket.getAddress()));
            return packet;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int accept() { return 0; }

    @Override
    public int close() {
        return 0;
    }
}
