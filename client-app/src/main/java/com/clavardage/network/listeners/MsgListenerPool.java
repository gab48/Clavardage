package com.clavardage.network.listeners;

import com.clavardage.network.handlers.MsgPacketHandler;
import com.clavardage.network.models.MessagePacket;
import com.clavardage.network.sockets.TCPRecvSocket;
import com.clavardage.utils.Config;

public class MsgListenerPool extends ListenerPool {

    private final TCPRecvSocket srvSock;
    public static volatile boolean run = true;

    public MsgListenerPool() {
        super(Integer.parseInt(Config.get("MSG_LISTENER_POOL_SIZE")));
        this.srvSock = new TCPRecvSocket(Short.parseShort(Config.get("NETWORK_TCP_SRV_PORT")));
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
