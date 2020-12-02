package Clavardage.Network.Handlers;

import Clavardage.Models.User;
import Clavardage.Network.Controllers.CCPController;
import Clavardage.Network.Models.Address;
import Clavardage.Network.Models.CCPPacket;

public class CCPPacketHandler extends PacketHandler<CCPPacket> {

    public CCPPacketHandler(int id, CCPPacket packet) {
        super(id, packet);
    }

    @Override
    public void run() {
        System.out.println("Processing this packet : " + this.packet);


        if (this.packet.getType() == 0) {
            //Get my infos
            User me = new User("XxRené.Ga56xX", Address.getMyIP());
            CCPController ccpController = new CCPController(me);
            ccpController.sendReplyTo(this.packet.getSrc());
        }


        //TODO: Save remoteUser
        User remoteUser = this.packet.getUserFromDiscover();
        System.out.println("Update user list with this one : " + remoteUser);
    }
}
