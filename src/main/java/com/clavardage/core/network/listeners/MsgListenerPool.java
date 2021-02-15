package com.clavardage.core.network.listeners;

import com.clavardage.core.network.handlers.MsgPacketHandler;
import com.clavardage.core.network.models.MessagePacket;
import com.clavardage.core.network.sockets.TCPRecvSocket;
import com.clavardage.core.utils.Config;

public class MsgListenerPool extends ListenerPool {

    private final TCPRecvSocket srvSock;
    public static volatile boolean run = true;

    public MsgListenerPool() {
        super(Config.getInteger("MSG_LISTENER_POOL_SIZE"));
        this.srvSock = new TCPRecvSocket(Config.getShort("NETWORK_CLAVARDAGE_PORT"));
    }

    @Override
    public void run() {
        int handlerId = 0;
        MsgPacketHandler currentHandler;
        System.out.println("MsgListenerPool running...");

        //TODO: Closing application => exit loop
        while (run) {
            handlerId++;
            MessagePacket recvPacket = new MessagePacket();
            this.srvSock.accept();
            recvPacket = this.srvSock.recv(recvPacket);
            currentHandler = new MsgPacketHandler(handlerId, recvPacket, this.srvSock);
            this.pool.execute(currentHandler);
        }
    }
}
