package com.clavardage.core.network.models;

import com.clavardage.client.views.AlertWindow;
import com.clavardage.core.utils.Config;

import java.io.IOException;
import java.net.*;
import java.util.Objects;

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
        this(addr, Config.getShort("NETWORK_CLAVARDAGE_PORT"));
    }

    public Address(String str) {
        this(str.split(":")[0], Short.parseShort(str.split(":")[1]));
    }

    public static Address getMyIP() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            if (ip.isLoopbackAddress()) { //TODO: Find a better way
                try {
                    Socket s = new Socket("8.8.8.8", 53);
                    ip = s.getLocalAddress();
                    s.close();
                } catch (NoRouteToHostException e) {
                    AlertWindow.displayError("No Internet connection");
                    System.exit(1);
                }
            }
            return new Address(ip);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Address getMulticast() {
        if (Config.getBoolean("MULTICAST")) {
            try {
                return new Address(InetAddress.getByName(Config.getString("MULTICAST_GROUP")));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        } else {
            try {
                InetAddress localHost = Objects.requireNonNull(getMyIP()).getIp();
                if (localHost != null) {
                    NetworkInterface myInterface = NetworkInterface.getByInetAddress(localHost);
                    for (InterfaceAddress iAddr : myInterface.getInterfaceAddresses()) {
                        InetAddress bc = iAddr.getBroadcast();
                        if (bc != null) {
                            return new Address(bc);
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static NetworkInterface getInterface() {
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByInetAddress(Objects.requireNonNull(getMyIP()).getIp());
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return networkInterface;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return this.port == address.port && Objects.equals(this.ip, address.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
}
