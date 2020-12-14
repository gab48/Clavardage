package Clavardage.Network.Handlers;

import Clavardage.Models.Message;
import Clavardage.Models.User;
import Clavardage.Network.Controllers.CCPController;
import Clavardage.Network.Models.Address;
import Clavardage.Network.Models.MessagePacket;
import Clavardage.Views.MainWindow;

public class MsgPacketHandler extends PacketHandler<MessagePacket>{

    public MsgPacketHandler(int id, MessagePacket packet) {
        super(id, packet);
        this.addListener(MainWindow.getInstance().conversations);
    }

    @Override
    public void run() {
        System.out.println("Processing this packet : " + this.packet);

        Message msg = new Message();
        msg.unserialize(this.packet.serialize());

        User remoteUser = new User("BobDuPacket", Address.getMyIP());

        System.out.println("Message received : "+msg.getContent()+" at "+msg.getTime());
        this.notifyAll(remoteUser, msg);
    }
}
