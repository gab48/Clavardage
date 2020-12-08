package Clavardage.Models;

import java.util.ArrayList;

public class History {

    private final LocalUser localUser = LocalUser.getInstance();
    private final User remoteUser;

    private final ArrayList<Message> messagesHistory = new ArrayList<>();

    public History(User remote) {
        this.remoteUser = remote;
    }

    public void append(Message msg) {
        this.messagesHistory.add(msg);
    }

    public ArrayList<Message> getMessagesHistory() {
        return this.messagesHistory;
    }
}
