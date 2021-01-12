package com.clavardage.network.handlers;

import com.clavardage.managers.ConversationsManager;
import com.clavardage.managers.UsersManager;
import com.clavardage.models.Message;
import com.clavardage.models.User;
import com.clavardage.network.models.MessagePacket;
import com.clavardage.network.sockets.TCPRecvSocket;
import com.clavardage.observers.FileRecvListener;
import com.clavardage.views.MainWindow;

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

        User remoteUser = null;
        for(User connectedUser : UsersManager.getInstance().getConnectedUsers()) {
            if (connectedUser.correspondTo(ConversationsManager.getInstance().getConversation(this.packet.getSrc()).getParticipants().get(1))) {
                remoteUser = connectedUser;
            }
        }

        this.notifyAll(remoteUser, msg, this.link);
    }
}
