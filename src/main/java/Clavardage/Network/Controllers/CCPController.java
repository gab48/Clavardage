package Clavardage.Network.Controllers;

import Clavardage.Models.User;
import Clavardage.Network.Models.CCPPacket;
import Clavardage.Network.SocketProtocols.CCPsocket;
import Clavardage.Network.Types.CCPPacketType;

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
