package Clavardage.Network.Handlers;

import Clavardage.Managers.ConversationsManager;
import Clavardage.Managers.UsersManager;
import Clavardage.Models.Message;
import Clavardage.Models.User;
import Clavardage.Network.Models.MessagePacket;
import Clavardage.Views.MainWindow;

public class MsgPacketHandler extends PacketHandler<MessagePacket>{

    public MsgPacketHandler(int id, MessagePacket packet) {
        super(id, packet);
        this.addListener(MainWindow.INSTANCE.conversations);
    }

    @Override
    public void run() {

        Message msg = new Message();
        msg.unserialize(this.packet.serialize());

        User remoteUser = null;
        for(User connectedUser : UsersManager.getInstance().getConnectedUsers()) {
            if (connectedUser.correspondTo(ConversationsManager.getInstance().getConversation(this.packet.getSrc()).getParticipants().get(1))) {
                remoteUser = connectedUser;
            }
        }

        this.notifyAll(remoteUser, msg);
    }
}
