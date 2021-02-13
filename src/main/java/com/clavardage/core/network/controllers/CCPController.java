package com.clavardage.core.network.controllers;

import com.clavardage.core.models.User;
import com.clavardage.core.network.models.Address;
import com.clavardage.core.network.models.CCPPacket;
import com.clavardage.core.network.sockets.CCPsocket;
import com.clavardage.core.network.types.CCPPacketType;

public class CCPController {

    public CCPController() {}

    private void muticast(CCPPacket packet) {
        CCPsocket sock = new CCPsocket();
        sock.send(packet);
        sock.close();
    }

    private void unicast(CCPPacket packet, Address address) {
        packet.setDestAddr(address);
        CCPsocket sock = new CCPsocket();
        sock.send(packet);
        sock.close();
    }

    public void sendDiscovery() {
        CCPPacket discover = new CCPPacket(CCPPacketType.DISCOVER, User.localUser);
        this.muticast(discover);
    }

    public void sendDisconnect() {
        CCPPacket disconnect = new CCPPacket(CCPPacketType.DISCONNECT, User.localUser);
        this.muticast(disconnect);
    }

    public void sendReplyTo(User remoteUser) {
        CCPPacket reply = new CCPPacket(CCPPacketType.REPLY, User.localUser);
        this.unicast(reply, remoteUser.getAddress());
    }
}
