package Clavardage.Network.Models;

import Clavardage.Models.Message;

public class MessagePacket extends Packet implements Storable<MessagePacket> {
    public MessagePacket(Message msg) {
        super(msg.serialize());
    }

    public MessagePacket () {
        super();
    }
}
