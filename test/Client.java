import Clavardage.Models.LocalUser;
import Clavardage.Models.User;
import Clavardage.Network.Controllers.CCPController;
import Clavardage.Network.Listeners.CCPListenerPool;

public class Client {
    public static void main(String[] args) {
        LocalUser.instanciate("micheldu31");
        CCPController ccpController = new CCPController();
        ccpController.sendDiscovery();
    }
}
