package com.clavardage.network.handlers;

import com.clavardage.managers.UsersManager;
import com.clavardage.models.User;
import com.clavardage.network.controllers.CCPController;
import com.clavardage.network.models.CCPPacket;

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
                case 0: // DISCOVER package from a remoteUser
                    if (!remoteUser.equals(User.localUser)) {
                        CCPController ccpController = new CCPController();
                        ccpController.sendReplyTo(remoteUser);
                        this.um.addConnectedUser(remoteUser);
                    }
                    break;
                case 1: // REPLY package
                    this.um.addConnectedUser(remoteUser);
                    break;
                case 2: // DELETE package
                    this.um.removeConnectedUser(remoteUser);
                    break;
                default:
                    break;
            }
        }
    }
}
