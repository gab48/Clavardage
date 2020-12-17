package Clavardage.Network.Listeners;

import Clavardage.Network.Handlers.CCPPacketHandler;
import Clavardage.Network.Models.CCPPacket;
import Clavardage.Network.SocketProtocols.CCPsocket;
import Clavardage.Utils.Config;

public class CCPListenerPool extends ListenerPool {

    private final CCPsocket srvSock;

    public CCPListenerPool() {
        super(Integer.parseInt(Config.get("CCP_LISTENER_POOL_SIZE")));
        this.srvSock = new CCPsocket(Short.parseShort(Config.get("NETWORK_UDP_SRV_PORT")));
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        int handlerId = 0;
        CCPPacketHandler currentHandler;
        System.out.println("CCPListenerPool running...");

        //TODO: Closing application => exit loop
        while (true) {
            handlerId++;
            CCPPacket recvPacket = new CCPPacket();
            recvPacket = (CCPPacket) this.srvSock.recv(recvPacket);

            currentHandler = new CCPPacketHandler(handlerId, recvPacket);
            this.pool.execute(currentHandler);
        }
    }
}
