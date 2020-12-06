package Clavardage;

public class Env {
    public static final short NETWORK_TCP_SRV_PORT = 1921;
    public static final short NETWORK_UDP_SRV_PORT = 1921;

    public static final int CCP_LISTENER_POOL_SIZE = 5;
    public static final int MSG_LISTENER_POOL_SIZE = 5;

    public static final boolean MULTICAST = true;
    public static final String MULTICAST_GROUP = "224.0.0.121"; // Set to null if you want to use broadcast
}
