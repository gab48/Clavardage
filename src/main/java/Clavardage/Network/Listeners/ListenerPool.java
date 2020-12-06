package Clavardage.Network.Listeners;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class ListenerPool implements Runnable {
    protected ExecutorService pool;

    public ListenerPool(int poolSize) {
        this.pool   = Executors.newFixedThreadPool(poolSize);
    }
}
