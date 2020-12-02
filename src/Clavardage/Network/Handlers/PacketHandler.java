package Clavardage.Network.Handlers;

import Clavardage.Network.Models.Packet;

public abstract class PacketHandler<T extends Packet> implements Runnable {

    protected int id;
    protected T packet;

    public PacketHandler (int id, T packet){
        this.id = id;
        this.packet = packet;
    }

    public int getId() {
        return this.id;
    }

}
