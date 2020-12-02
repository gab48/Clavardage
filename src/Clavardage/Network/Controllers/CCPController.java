package Clavardage.Network.Controllers;

import Clavardage.Models.User;
import Clavardage.Network.Models.CCPPacket;
import Clavardage.Network.SocketProtocols.UDPsocket;
import Clavardage.Network.Models.Address;
import Clavardage.Network.Types.CCPPacketType;

public class CCPController {

    private User me;

    public CCPController(User myself) {
        this.me = myself;
    }

    public void sendDiscovery() {
        CCPPacket discover = new CCPPacket(CCPPacketType.DISCOVER, this.me);
        UDPsocket sock = new UDPsocket();
        sock.connect(Address.getBroadcast());
        sock.send(discover);
        System.out.println("Broadcast : "+ discover);
    }

    public void sendReplyTo(Address dest) {
        CCPPacket reply = new CCPPacket(CCPPacketType.REPLY, this.me);
        reply.setDestAddr(dest);
        System.out.println("Sending REPLY : "+ reply);
        UDPsocket sock = new UDPsocket();
        sock.connect(dest);
        sock.send(reply);
    }
}
