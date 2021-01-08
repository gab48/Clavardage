package Clavardage.Network.Handlers;

import Clavardage.Managers.ConversationsManager;
import Clavardage.Managers.UsersManager;
import Clavardage.Models.Message;
import Clavardage.Models.User;
import Clavardage.Network.Models.MessagePacket;
import Clavardage.Network.SocketProtocols.TCPRecvSocket;
import Clavardage.Observers.FileRecvListener;
import Clavardage.Views.MainWindow;

public class MsgPacketHandler extends PacketHandler<MessagePacket>{

    private final TCPRecvSocket link;

    public MsgPacketHandler(int id, MessagePacket packet, TCPRecvSocket link) {
        super(id, packet);
        this.link = link;
        this.addListener(MainWindow.INSTANCE.conversations);
        this.addListener(FileRecvListener.getINSTANCE());
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

        this.notifyAll(remoteUser, msg, this.link);
    }
}
