package com.clavardage.client.managers;

import com.clavardage.core.network.listeners.MsgListenerPool;

public class MessagesManager implements Manager {
    private static final MessagesManager INSTANCE = new MessagesManager();
    public static MessagesManager getInstance() {
        return INSTANCE;
    }

    private MessagesManager() { new Thread(new MsgListenerPool()).start(); }

    @Override
    public void stop() {
        MsgListenerPool.run=false;
    }
}
