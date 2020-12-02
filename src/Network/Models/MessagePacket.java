package Network.Models;

import Models.Message;
import Network.Models.Packet;

public class MessagePacket extends Packet {
    public MessagePacket(Message msg) {
        super(msg.serialize());
    }
}
