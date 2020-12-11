package Clavardage.Network.Handlers;

import Clavardage.Models.Message;
import Clavardage.Models.User;
import Clavardage.Network.Controllers.CCPController;
import Clavardage.Network.Models.Address;
import Clavardage.Network.Models.MessagePacket;

public class MsgPacketHandler extends PacketHandler<MessagePacket>{

    public MsgPacketHandler(int id, MessagePacket packet) {
        super(id, packet);
    }

    @Override
    public void run() {
        System.out.println("Processing this packet : " + this.packet);

        Message msg = new Message();
        msg.unserialize(this.packet.serialize());

        System.out.println("Message received : "+msg.getContent()+" at "+msg.getTime());
        this.notifyAll(msg);
    }
}