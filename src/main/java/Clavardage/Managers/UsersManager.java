package Clavardage.Managers;

import Clavardage.Models.User;
import Clavardage.Network.Controllers.CCPController;
import Clavardage.Network.Listeners.CCPListenerPool;

import java.util.ArrayList;

public class UsersManager extends Manager {
    private static final UsersManager INSTANCE = new UsersManager();

    private static final ArrayList<User> connectedUsers = new ArrayList<>();

    public static ArrayList<User> getConnectedUsers() { return connectedUsers; }
    public static void addConnectedUser(User u) {
        if (!connectedUsers.contains(u)) {
            connectedUsers.add(u);
            System.out.println("Adding " + u);
        }
    }
    public static void removeConnectedUser(User u) {
        connectedUsers.remove(u);
        System.out.println("Removing "+ u);
    }
    public static void showList() {
        System.out.println("Updated List :");
        System.out.println(connectedUsers);
    }

    private UsersManager () {
        (new Thread(new CCPListenerPool())).start();
        new CCPController().sendDiscovery();
    }

    public static UsersManager getInstance() {
        return INSTANCE;
    }
}
