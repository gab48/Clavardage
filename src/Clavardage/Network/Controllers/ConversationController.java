package Clavardage.Network.Controllers;

import Clavardage.Models.Message;
import Clavardage.Models.User;
import Clavardage.Network.Models.MessagePacket;
import Clavardage.Network.SocketProtocols.TCPSendSocket;

public class ConversationController {
    private final User localUser;
    private final User remoteUser;

    public ConversationController(User l, User r) {
        this.localUser = l;
        this.remoteUser = r;
    }

    public int sendMessage(Message msg) {
        MessagePacket msgPckt = new MessagePacket(msg);

        TCPSendSocket socket = new TCPSendSocket();
        socket.connect(remoteUser.getAddr());
        socket.send(msgPckt);
        return 0;
    }

}
