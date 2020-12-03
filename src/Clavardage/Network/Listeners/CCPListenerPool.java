package Clavardage.Network.Listeners;

import Clavardage.Env;
import Clavardage.Network.Handlers.CCPPacketHandler;
import Clavardage.Network.Models.CCPPacket;
import Clavardage.Network.SocketProtocols.UDPsocket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CCPListenerPool extends ListenerPool {

    private UDPsocket srvSock;

    public CCPListenerPool() {
        super(Env.CCP_LISTENER_POOL_SIZE);
        this.srvSock = new UDPsocket(Env.NETWORK_UDP_SRV_PORT);
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
