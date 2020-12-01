package Network.CCP;

import Models.User;
import Network.SocketProtocols.UDPsocket;
import Network.Utils.Address;
import Network.Utils.CCPPacketType;

public class CCPController {

    private User me;

    public CCPController(User myself) {
        this.me = myself;
    }

    public void sendDiscovery() {
        CCPPacket discover = new CCPPacket(CCPPacketType.DISCOVER, this.me);
        UDPsocket sock = new UDPsocket();
        sock.connect("127.0.0.1", (short) 1921);
        sock.send(discover);
    }

    public void sendReplyTo(Address dest) {
        CCPPacket reply = new CCPPacket(CCPPacketType.REPLY, this.me);
        reply.setDestAddr(dest);
        System.out.println("Sending REPLY : "+ reply);
        UDPsocket sock = new UDPsocket();
        sock.connect("127.0.0.1", (short) 1922);
        sock.send(reply);
    }
}
