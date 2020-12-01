import Network.CCP.CCPListenerPool;
import Network.CCP.CCPPacket;
import Network.Packet;
import Network.SocketProtocols.UDPsocket;

public class Server {
    public static void main(String[] args) {
        CCPListenerPool srv = new CCPListenerPool((short) 1921);
        Thread srvThread = new Thread(srv);
        srvThread.start();
    }
}
