package com.clavardage.core.network.handlers;

import com.clavardage.client.managers.UsersManager;
import com.clavardage.core.models.User;
import com.clavardage.core.network.controllers.CCPController;
import com.clavardage.core.network.models.CCPPacket;

public class CCPPacketHandler extends PacketHandler<CCPPacket> {

    private final UsersManager um = UsersManager.getInstance();

    public CCPPacketHandler(int id, CCPPacket packet) {
        super(id, packet);
    }

    @Override
    public void run() {
        User remoteUser = this.packet.getUserFromCCP();
        if(remoteUser != null) {
            switch (this.packet.getType()) {
                case 0: // DISCOVER packet from a remoteUser
                    if (!remoteUser.equals(User.localUser)) {
                        CCPController ccpController = new CCPController();
                        ccpController.sendReplyTo(remoteUser);
                        this.um.addConnectedUser(remoteUser);
                    }
                    break;
                case 1: // REPLY packet
                    this.um.addConnectedUser(remoteUser);
                    break;
                case 2: // DISCONNECT
                    this.um.removeConnectedUser(remoteUser);
                    break;
                default:
                    break;
            }
        }
    }
}
