package com.clavardage.network.listeners;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class ListenerPool implements Runnable {
    protected final ExecutorService pool;

    public ListenerPool(int poolSize) {
        this.pool   = Executors.newFixedThreadPool(poolSize);
    }
}
