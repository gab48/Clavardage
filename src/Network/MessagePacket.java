package Network;

import Models.Message;

public class MessagePacket extends Packet {
    public MessagePacket(Message msg) {
        super(msg.serialize());
    }
}
