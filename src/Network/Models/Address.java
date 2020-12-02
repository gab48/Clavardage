package Network.Models;

import java.io.IOException;
import java.net.*;

public class Address {

    private InetAddress ip;
    private short port;

    public Address(String ip, short port) {
        try {
            this.ip = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.port = port;
    }

    public Address(InetAddress addr, short port) {
        this.ip = addr;
        this.port = port;
    }

    public Address(InetAddress addr) {
        this(addr, Packet.DEFAULT_SRC_PORT);
    }

    public static Address getMyIP() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            if (ip.isLoopbackAddress()) { //TODO: Find a better way
                Socket s = new Socket("8.8.8.8", 53);
                ip = s.getLocalAddress();
                s.close();
            }
            return new Address(ip);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Address getBroadcast() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            if(localHost != null) {
                NetworkInterface myInterface = NetworkInterface.getByInetAddress(localHost);
                for(InterfaceAddress iAddr : myInterface.getInterfaceAddresses()) {
                    InetAddress bc = iAddr.getBroadcast();
                    if(bc != null) {
                        return new Address(bc);
                    }
                }
            }
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }

        return null;
    }

    public InetAddress getIp() {
        return ip;
    }
    public short getPort() {
        return port;
    }

    public void setPort(short port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return this.ip.getHostAddress()+':'+this.port;
    }
}
