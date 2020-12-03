package Clavardage.Network.Controllers;

import Clavardage.Models.LocalUser;
import Clavardage.Models.User;
import Clavardage.Network.Models.CCPPacket;
import Clavardage.Network.SocketProtocols.UDPsocket;
import Clavardage.Network.Models.Address;
import Clavardage.Network.Types.CCPPacketType;

public class CCPController {

    public CCPController() {}

    public void sendDiscovery() { //TODO: Improve Discovery sending using Multicast (Less trafic and better efficiency)
        CCPPacket discover = new CCPPacket(CCPPacketType.DISCOVER, LocalUser.getInstance());
        UDPsocket sock = new UDPsocket();
        sock.connect(Address.getBroadcast());
        sock.send(discover);
        sock.close();
        System.out.println("Broadcast : "+ discover);
    }

    public void sendReplyTo(User remoteUser) {
        CCPPacket reply = new CCPPacket(CCPPacketType.REPLY, LocalUser.getInstance());
        reply.setDestAddr(remoteUser.getAddr());
        System.out.println("Sending REPLY : " + reply);
        UDPsocket sock = new UDPsocket();
        sock.connect(remoteUser.getAddr());
        sock.send(reply);
        sock.close();
    }
}
