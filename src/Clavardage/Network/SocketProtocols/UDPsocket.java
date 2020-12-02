package Clavardage.Network.SocketProtocols;

import Clavardage.Network.Models.Address;
import Clavardage.Network.Models.Packet;
import Clavardage.Network.Types.ProtocolType;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPsocket extends Socket{

    private static final int PAYLOAD_MAX_SIZE = 8192;
    private DatagramSocket socket;

    public UDPsocket(short localPort) {
        super(localPort);
        this.protocol = ProtocolType.UDP;
        try {
            if (this.localPort > 1024) {
                this.socket = new DatagramSocket(this.localPort);
            } else {
                this.socket = new DatagramSocket();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public UDPsocket() {
        this((short) 0);
        this.protocol = ProtocolType.UDP;
    }

    @Override
    public int send(Packet packet) {
        int byteSent;
        byte[] payload = packet.serialize();

        DatagramPacket datagramPacket = new DatagramPacket(payload, payload.length, this.remoteAddr.getIp(), this.remoteAddr.getPort());
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
