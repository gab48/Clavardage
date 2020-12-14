package Clavardage.Managers;

import Clavardage.Network.Listeners.MsgListenerPool;

public class MessagesManager implements Manager {
    private static final MessagesManager INSTANCE = new MessagesManager();

    public static MessagesManager getInstance() {
        return INSTANCE;
    }

    private MessagesManager() {
        (new Thread(new MsgListenerPool())).start();
    }
}
