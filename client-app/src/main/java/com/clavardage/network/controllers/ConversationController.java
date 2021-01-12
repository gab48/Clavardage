package com.clavardage.network.controllers;

import com.clavardage.managers.ConversationsManager;
import com.clavardage.models.Conversation;
import com.clavardage.models.Message;
import com.clavardage.models.User;
import com.clavardage.network.models.MessagePacket;
import com.clavardage.network.sockets.TCPSendSocket;

public class ConversationController {
    private final User remoteUser;
    private final Conversation currentConversation;

    public ConversationController(User r) {
        this.remoteUser = r;
        this.currentConversation = ConversationsManager.getInstance().getConversation(this.remoteUser.getAddress());
    }

    public void sendMessage(Message msg) {
        MessagePacket msgPckt = new MessagePacket(msg, this.remoteUser.getAddress());
        msgPckt.setSrc(User.localUser.getAddress());

        TCPSendSocket socket = new TCPSendSocket();
        socket.connect(remoteUser.getAddress());
        int res = socket.send(msgPckt);
        socket.close();

        msgPckt.store();
    }

}
