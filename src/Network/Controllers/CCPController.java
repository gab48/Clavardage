package Network.Controllers;

import Models.User;
import Network.Models.CCPPacket;
import Network.SocketProtocols.UDPsocket;
import Network.Models.Address;
import Network.Types.CCPPacketType;

import java.sql.SQLOutput;
import java.util.Objects;

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
