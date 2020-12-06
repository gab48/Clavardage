package Clavardage.Network.Listeners;

import Clavardage.Env;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class ListenerPool implements Runnable {
    protected ExecutorService pool;

    public ListenerPool(int poolSize) {
        this.pool   = Executors.newFixedThreadPool(Env.CCP_LISTENER_POOL_SIZE);
    }
}
