package com.clavardage.core.network.listeners;

import com.clavardage.core.network.handlers.CCPPacketHandler;
import com.clavardage.core.network.models.CCPPacket;
import com.clavardage.core.network.sockets.CCPsocket;
import com.clavardage.core.utils.Config;

public class CCPListenerPool extends ListenerPool {

    private final CCPsocket srvSock;
    public static volatile boolean run = true;

    public CCPListenerPool() {
        super(Integer.parseInt(Config.get("CCP_LISTENER_POOL_SIZE")));
        this.srvSock = new CCPsocket(Short.parseShort(Config.get("NETWORK_CLAVARDAGE_PORT")));
    }

    @Override
    public void run() {
        int handlerId = 0;
        CCPPacketHandler currentHandler;
        System.out.println("CCPListenerPool running...");

        //TODO: Closing application => exit loop
        while (run) {
            handlerId++;
            CCPPacket recvPacket = new CCPPacket();
            recvPacket = (CCPPacket) this.srvSock.recv(recvPacket);

            currentHandler = new CCPPacketHandler(handlerId, recvPacket);
            this.pool.execute(currentHandler);
        }
    }
}
