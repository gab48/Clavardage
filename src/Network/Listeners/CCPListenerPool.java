package Network.Listeners;

import Network.Handlers.CCPPacketHandler;
import Network.Models.CCPPacket;
import Network.SocketProtocols.UDPsocket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CCPListenerPool implements Runnable {

    public static final short   CCP_SRV_PORT   = 1921;
    public static final int     POOL_SIZE      = 5;
    private UDPsocket srvSock;
    private ExecutorService pool;

    public CCPListenerPool() {
        this.srvSock = new UDPsocket(CCP_SRV_PORT);
        this.pool   = Executors.newFixedThreadPool(POOL_SIZE);
    }

    public CCPListenerPool(short port) {
        this.srvSock = new UDPsocket(port);
        this.pool   = Executors.newFixedThreadPool(POOL_SIZE);
    }

    public void run() {
        int handlerId = 0;
        CCPPacketHandler currentHandler;

        while (true) {
            handlerId++;
            CCPPacket recvPacket = new CCPPacket();
            recvPacket = (CCPPacket) this.srvSock.recv(recvPacket);

            currentHandler = new CCPPacketHandler(handlerId, recvPacket);
            this.pool.execute(currentHandler);
        }
    }
}
