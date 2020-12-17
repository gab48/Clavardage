package Clavardage.Network.Listeners;

import Clavardage.Network.Handlers.MsgPacketHandler;
import Clavardage.Network.Models.MessagePacket;
import Clavardage.Network.SocketProtocols.TCPRecvSocket;
import Clavardage.Utils.Config;

public class MsgListenerPool extends ListenerPool {

    private final TCPRecvSocket srvSock;

    public MsgListenerPool() {
        super(Integer.parseInt(Config.get("MSG_LISTENER_POOL_SIZE")));
        this.srvSock = new TCPRecvSocket(Short.parseShort(Config.get("NETWORK_TCP_SRV_PORT")));
    }

    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        int handlerId = 0;
        MsgPacketHandler currentHandler;
        System.out.println("MsgListenerPool running...");

        //TODO: Closing application => exit loop
        while (true) {
            handlerId++;
            MessagePacket recvPacket = new MessagePacket();
            this.srvSock.accept();
            recvPacket = (MessagePacket) this.srvSock.recv(recvPacket);
            currentHandler = new MsgPacketHandler(handlerId, recvPacket);
            this.pool.execute(currentHandler);
        }
    }
}
