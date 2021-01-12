package com.clavardage.network.controllers;

import com.clavardage.models.User;
import com.clavardage.network.models.CCPPacket;
import com.clavardage.network.sockets.CCPsocket;
import com.clavardage.network.types.CCPPacketType;

public class CCPController {

    public CCPController() {}

    public void sendDiscovery() {
        CCPPacket discover = new CCPPacket(CCPPacketType.DISCOVER, User.localUser);
        CCPsocket sock = new CCPsocket();
        sock.send(discover);
        sock.close();
    }

    public void sendReplyTo(User remoteUser) {
        CCPPacket reply = new CCPPacket(CCPPacketType.REPLY, User.localUser);
        reply.setDestAddr(remoteUser.getAddress());
        CCPsocket sock = new CCPsocket();
        sock.send(reply);
        sock.close();
    }
}
