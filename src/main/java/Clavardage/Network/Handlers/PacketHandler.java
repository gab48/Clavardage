package Clavardage.Network.Handlers;

import Clavardage.Network.Models.Packet;
import Clavardage.Observers.Listener;
import Clavardage.Observers.Observable;

import java.util.ArrayList;

public abstract class PacketHandler<T extends Packet> implements Runnable, Observable {

    protected final int id;
    protected final T packet;
    protected final ArrayList<Listener> listeners = new ArrayList<>();

    public PacketHandler (int id, T packet){
        this.id = id;
        this.packet = packet;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public void addListener(Listener l) {
        listeners.add(l);
    }

    @Override
    public void removeListener(Listener l) {
        listeners.remove(l);
    }

    @Override
    public void notifyAll(Object... args) {
        for (Listener l: this.listeners) {
            l.handle(args);
        }
    }

}
