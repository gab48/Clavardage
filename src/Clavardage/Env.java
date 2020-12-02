package Clavardage;

import Clavardage.Views.ConversationWindow;

public class Env {
    public static final short NETWORK_TCP_SRV_PORT = 1921;
    public static final short NETWORK_UDP_SRV_PORT = 1921;

    public static final int CCP_LISTENER_POOL_SIZE = 5;
    public static final int MSG_LISTENER_POOL_SIZE = 5;

    private static ConversationWindow cw;

    public static void setCw(ConversationWindow cw) {
        Env.cw = cw;
    }

    public static ConversationWindow getCw() {
        return cw;
    }
}
