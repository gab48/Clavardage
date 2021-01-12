package com.clavardage.network.sockets;

import com.clavardage.network.models.Address;
import com.clavardage.network.models.Packet;
import com.clavardage.network.types.ProtocolType;
import com.clavardage.utils.Config;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.Objects;

public class CCPsocket extends Socket<Packet>{

    protected static final int PAYLOAD_MAX_SIZE = 8192;
    protected MulticastSocket socket;

    public CCPsocket(short localPort) {
        super(localPort);
        this.protocol = ProtocolType.UDP;

        try {
            if (this.localPort > 1024) {
                this.socket = new MulticastSocket(this.localPort);
                this.socket.joinGroup(new InetSocketAddress(Objects.requireNonNull(Address.getMulticast()).getIp(), Short.parseShort(Config.get("NETWORK_UDP_SRV_PORT"))), Address.getInterface());
            } else if (this.localPort == 0) {
                this.socket = new MulticastSocket();
            } else {
                System.err.println("Please use a port >1024");
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
