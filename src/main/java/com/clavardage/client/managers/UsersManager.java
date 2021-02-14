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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class UsersManager implements Manager, Observable {
    private static final UsersManager INSTANCE = new UsersManager();
    public static volatile boolean run = true;

    //private final ArrayList<User> connectedUsers = new ArrayList<>();
    private final ArrayList<Listener> listeners = new ArrayList<>();
    private final HashMap<String, User> connectedUsers = new HashMap<>();

    public ArrayList<User> getConnectedUsers() {
        return new ArrayList<>(connectedUsers.values());
    }

    public void addConnectedUser(User u) {
        if (!this.connectedUsers.containsKey(u.getAddress().toString())) {
            this.connectedUsers.put(u.getAddress().toString(), u);
            this.notifyAll((Object) null);
        }
    }

    public void removeConnectedUser(User u) {
        this.connectedUsers.remove(u.getAddress().toString());
        this.notifyAll((Object) null);
    }

    public void showList() {
        System.out.println("Updated List :");
        System.out.println(this.connectedUsers);
    }

    private void statusUpdater() {
        String url = Config.get("SERVLET_ADDR");
        byte[] userStatusHash = null;
        byte[] tmpHash;
        while (run) {
            try {
                TimeUnit.SECONDS.sleep(10);
                StatusSelectRequest request = new StatusSelectRequest(url);
                request.executeGet();

                String result = request.getResponse().trim();
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                tmpHash = md5.digest(result.getBytes());

                if(!Arrays.equals(userStatusHash, tmpHash)) {
                    userStatusHash = tmpHash;

                    JSONObject json = new JSONObject(request.getResponse().trim());
                    Iterator<String> keys = json.keys();
                    HashMap<String, User.UserStatus> usersStatus = new HashMap<>();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        if (json.get(key) instanceof Integer) {
                            User.UserStatus status = User.IntToUserStatus(json.getInt(key));
                            usersStatus.put(key, status);
                        }
                    }

                    for (User user : this.connectedUsers.values()) {
                        if (usersStatus.containsKey(user.getAddress().toString())) {
                            user.setStatus(usersStatus.get(user.getAddress().toString()));
                        }
                    }
                }
                this.notifyAll((Object) null);
            } catch (InterruptedException ignore) {} catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    private UsersManager () {
        new Thread(new CCPListenerPool()).start();
        User.connectLocalUser();
        User.updateLocalUserStatus(User.UserStatus.CONNECTED);

        // Status Updater
        new Thread(this::statusUpdater).start();
    }

    public static UsersManager getInstance() {
        return INSTANCE;
    }

    public User getUser(Address address) {
        for(User connectedUser : this.connectedUsers.values()) {
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
