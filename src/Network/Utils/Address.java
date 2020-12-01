package Network.Utils;

import Network.Packet;

import java.net.*;
import java.util.Enumeration;

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
            return new Address(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Address getBroadcast() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while(interfaces.hasMoreElements()) {
                NetworkInterface currentInterface = interfaces.nextElement();
                if(!currentInterface.isLoopback()) {
                    for(InterfaceAddress interfaceAddress : currentInterface.getInterfaceAddresses()) {
                        Address addr = new Address(interfaceAddress.getAddress());
                        if (addr == getMyIP()) {
                            addr = new Address(interfaceAddress.getBroadcast());
                            return addr;
                        }
                    }
                }
            }
            return null;
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }
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
        return this.ip.toString()+':'+this.port;
    }
}
