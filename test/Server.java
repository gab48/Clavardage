import Network.Listeners.CCPListenerPool;

public class Server {
    public static void main(String[] args) {
        CCPListenerPool srv = new CCPListenerPool((short) 1921);
        Thread srvThread = new Thread(srv);
        srvThread.start();
    }
}
