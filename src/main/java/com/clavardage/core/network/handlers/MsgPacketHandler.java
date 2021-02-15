package com.clavardage.core.network.handlers;

import com.clavardage.client.managers.ConversationsManager;
import com.clavardage.client.managers.UsersManager;
import com.clavardage.core.models.Message;
import com.clavardage.core.models.User;
import com.clavardage.core.network.models.MessagePacket;
import com.clavardage.core.network.sockets.TCPRecvSocket;
import com.clavardage.core.observers.FileRecvListener;
import com.clavardage.client.views.MainWindow;

public class MsgPacketHandler extends PacketHandler<MessagePacket>{

    private final TCPRecvSocket link;

    public MsgPacketHandler(int id, MessagePacket packet, TCPRecvSocket link) {
        super(id, packet);
        this.link = link;
        this.addListener(MainWindow.INSTANCE.conversations);
        this.addListener(FileRecvListener.getINSTANCE());
    }

    @Override
    public void run() {

        Message msg = new Message();
        msg.unserialize(this.packet.serialize());

        for(User connectedUser : UsersManager.getInstance().getConnectedUsers()) {
            if (connectedUser.correspondTo(ConversationsManager.getInstance().getConversation(this.packet.getSrc()).getParticipants().get(1))) {
                this.notifyAll(connectedUser, msg, this.link);
            }
        }
    }
}
