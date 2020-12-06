package Clavardage.Network.Handlers;

import Clavardage.Managers.UsersManager;
import Clavardage.Models.LocalUser;
import Clavardage.Models.User;
import Clavardage.Network.Controllers.CCPController;
import Clavardage.Network.Models.CCPPacket;

public class CCPPacketHandler extends PacketHandler<CCPPacket> {

    private UsersManager um = UsersManager.getInstance();

    public CCPPacketHandler(int id, CCPPacket packet) {
        super(id, packet);
    }

    @Override
    public void run() {
        System.out.println("Processing this packet : " + this.packet);
        User remoteUser = this.packet.getUserFromCCP();
        if(remoteUser != null) {
            switch (this.packet.getType()) {
                case 0: // DISCOVER package from a remoteUser
                    if (!remoteUser.equals(LocalUser.getInstance())) {
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
        this.um.showList();
        }
    }
}
