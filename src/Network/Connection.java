package Network;

import java.net.InetAddress;

public class Connection {

    public static int listen() { // Server
        // Server Thread
        // New thread for each inbound connections
        return 0;
    }

    public Connection(InetAddress remoteUser) {
        System.out.println("Connection to " + remoteUser.toString());
        // Connect to user
    }

    public int send(Packet packet) {
        System.out.println("Sending...");
        return 0;
    }

}
