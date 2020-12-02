package Clavardage.Network.Listeners;

import Clavardage.Env;
import Clavardage.Network.Handlers.MsgPacketHandler;
import Clavardage.Network.Models.MessagePacket;
import Clavardage.Network.SocketProtocols.TCPRecvSocket;

public class MsgListenerPool extends ListenerPool {

    private TCPRecvSocket srvSock;

    public MsgListenerPool() {
        super(Env.MSG_LISTENER_POOL_SIZE);
        this.srvSock = new TCPRecvSocket(Env.NETWORK_TCP_SRV_PORT);
    }

    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        int handlerId = 0;
        MsgPacketHandler currentHandler;

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
