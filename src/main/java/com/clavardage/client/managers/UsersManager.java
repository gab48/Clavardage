package com.clavardage.client.managers;

import com.clavardage.core.models.User;
import com.clavardage.core.network.controllers.CCPController;
import com.clavardage.core.network.http.StatusSelectRequest;
import com.clavardage.core.network.listeners.CCPListenerPool;
import com.clavardage.core.network.models.Address;
import com.clavardage.core.observers.Listener;
import com.clavardage.core.observers.Observable;
import com.clavardage.core.utils.Config;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class UsersManager implements Manager, Observable {
    private static final UsersManager INSTANCE = new UsersManager();
    public static volatile boolean run = true;

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

    private void statusUpdater() {
        String url = Config.get("SERVLET_ADDR");
        while (run) {
            try {
                TimeUnit.SECONDS.sleep(10);
                StatusSelectRequest request = new StatusSelectRequest(url);
                request.executeGet();
                JSONObject json = new JSONObject(request.getResponse().trim());
                Iterator<String> keys = json.keys();
                HashMap<String, User.UserStatus> usersStatus= new HashMap<>();

                while(keys.hasNext()) {
                    String key = keys.next();
                    if (json.get(key) instanceof Integer) {
                        User.UserStatus status = User.UserStatusToInt(json.getInt(key));
                        usersStatus.put(key, status);
                    }
                }

                for(User user : this.connectedUsers) {
                    if(usersStatus.containsKey(user.getAddress().toString())) {
                        user.updateStatus(usersStatus.get(user.getAddress().toString()));
                    }
                }
            } catch (InterruptedException ignore) {}
        }
    }

    private UsersManager () {
        new Thread(new CCPListenerPool()).start();
        new CCPController().sendDiscovery();

        // Status Updater
        new Thread(this::statusUpdater).start();
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

    @Override
    public void stop() {
        //TODO : Send disconnect
        run=false;
        CCPListenerPool.run=false;
    }
}
