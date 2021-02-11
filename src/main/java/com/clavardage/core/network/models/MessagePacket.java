package com.clavardage.core.network.models;

import com.clavardage.core.models.Message;

public class MessagePacket extends Packet implements Storable<MessagePacket> {
    public MessagePacket(Message msg, Address destination) {
        super(msg.serialize());
        this.dest = destination;
    }

    public MessagePacket () {
        super();
    }
}
