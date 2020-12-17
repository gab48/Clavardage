package Clavardage.Managers;

import Clavardage.Models.User;
import Clavardage.Network.Controllers.CCPController;
import Clavardage.Network.Listeners.CCPListenerPool;
import Clavardage.Network.Models.Address;
import Clavardage.Observers.Listener;
import Clavardage.Observers.Observable;

import java.util.ArrayList;

public class UsersManager implements Manager, Observable {
    private static final UsersManager INSTANCE = new UsersManager();

    private final ArrayList<User> connectedUsers = new ArrayList<>();
    private final ArrayList<Listener> listeners = new ArrayList<>();

    public ArrayList<User> getConnectedUsers() { return this.connectedUsers; }

    public void addConnectedUser(User u) {
        if (!this.connectedUsers.contains(u)) {
            this.connectedUsers.add(u);
            this.notifyAll("add", u);
        }
    }

    public void removeConnectedUser(User u) {
        this.connectedUsers.remove(u);
        this.notifyAll("remove", u);
        System.out.println("Removing "+ u);
    }

    public void showList() {
        System.out.println("Updated List :");
        System.out.println(this.connectedUsers);
    }

    private UsersManager () {
        (new Thread(new CCPListenerPool())).start();
        new CCPController().sendDiscovery();
    }

    public static UsersManager getInstance() {
        return INSTANCE;
    }

    public User getUser(Address address) {
        for(User connectedUser : this.connectedUsers) {
            if(connectedUser.getAddress().equals(address)) {
                return connectedUser;
            }
        }
        return null;
    }

    @Override
    public void addListener(Listener l) {
        this.listeners.add(l);
    }

    @Override
    public void removeListener(Listener l) {
        this.listeners.remove(l);
    }

    @Override
    public void notifyAll(Object... args) {
        for (Listener l : this.listeners) {
            l.handle(args);
        }
    }
}
