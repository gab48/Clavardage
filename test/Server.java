import Clavardage.Network.Listeners.MsgListenerPool;

public class Server {
    public static void main(String[] args) {
        MsgListenerPool srv = new MsgListenerPool();
        Thread srvThread = new Thread(srv);
        srvThread.start();
    }
}
