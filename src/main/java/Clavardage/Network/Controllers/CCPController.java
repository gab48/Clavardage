package Clavardage.Network.Controllers;

import Clavardage.Models.User;
import Clavardage.Network.Models.CCPPacket;
import Clavardage.Network.SocketProtocols.CCPsocket;
import Clavardage.Network.Types.CCPPacketType;

public class CCPController {

    public CCPController() {}

    public void sendDiscovery() { //TODO: Improve Discovery sending using Multicast (Less trafic and better efficiency)
        CCPPacket discover = new CCPPacket(CCPPacketType.DISCOVER, User.localUser);
        CCPsocket sock = new CCPsocket();
        sock.send(discover);
        sock.close();
        System.out.println("Sending discovery message : "+ discover);
    }

    public void sendReplyTo(User remoteUser) {
        CCPPacket reply = new CCPPacket(CCPPacketType.REPLY, User.localUser);
        reply.setDestAddr(remoteUser.getAddress());
        System.out.println("Sending REPLY : " + reply);
        CCPsocket sock = new CCPsocket();
        sock.send(reply);
        sock.close();
    }
}
