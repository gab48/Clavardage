package Network.Conversation;

import Models.Message;
import Network.Packet;

public class MessagePacket extends Packet {
    public MessagePacket(Message msg) {
        super(msg.serialize());
    }
}
