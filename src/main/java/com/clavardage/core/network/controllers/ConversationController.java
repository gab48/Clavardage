package com.clavardage.core.network.controllers;

import com.clavardage.client.managers.ConversationsManager;
import com.clavardage.core.models.Conversation;
import com.clavardage.core.models.Message;
import com.clavardage.core.models.User;
import com.clavardage.core.network.models.Address;
import com.clavardage.core.network.models.FilePacket;
import com.clavardage.core.network.models.MessagePacket;
import com.clavardage.core.network.sockets.FileSocket;
import com.clavardage.core.network.sockets.TCPSendSocket;
import com.clavardage.core.utils.Config;

import java.io.File;

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

    public void sendFile(File file) {
        FileSocket fs = new FileSocket();
        Address remoteFileBuffer = new Address(this.remoteUser.getAddress().getIp(), Config.getShort("NETWORK_FILE_TRANSFERT_PORT"));
        FilePacket filePacket = new FilePacket(file, remoteFileBuffer);
        filePacket.setSrc(User.localUser.getAddress());
        fs.connect(remoteFileBuffer);
        fs.send(filePacket);
        fs.close();
    }

}
