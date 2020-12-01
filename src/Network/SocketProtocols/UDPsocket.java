package Network.SocketProtocols;

import Network.CCP.CCPPacket;
import Network.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPsocket extends Socket{

    private static final int PAYLOAD_MAX_SIZE = 8192;
    private DatagramSocket socket;

    public UDPsocket(short localPort) {
        super(localPort);
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
    }

    @Override
    public int accept() { return 0; }

    @Override
    public int send(Packet packet) {
        int byteSent;
        byte[] payload = packet.serialize();

        DatagramPacket datagramPacket = new DatagramPacket(payload, payload.length, this.remoteAddr.getIp(), this.remoteAddr.getPort());
        datagramPacket.setData(payload);

        try {
            this.socket.send(datagramPacket);
            byteSent = datagramPacket.getLength();
            System.out.println(byteSent+" bytes sent !");
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
            return packet; // return build(Class, bytes)
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int close() {
        return 0;
    }
}
