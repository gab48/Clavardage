import Models.User;
import Network.Controllers.CCPController;
import Network.Listeners.CCPListenerPool;

public class Client {
    public static void main(String[] args) {
        CCPListenerPool srv = new CCPListenerPool((short) 1922);
        Thread srvThread = new Thread(srv);
        srvThread.start();

        User michel = new User("micheldu31");
        CCPController ccpController = new CCPController(michel);
        ccpController.sendDiscovery();
    }
}
