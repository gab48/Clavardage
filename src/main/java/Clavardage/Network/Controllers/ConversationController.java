package Clavardage.Network.Controllers;

import Clavardage.Models.Message;
import Clavardage.Models.User;
import Clavardage.Network.Models.MessagePacket;
import Clavardage.Network.SocketProtocols.TCPSendSocket;

public class ConversationController {
    private final User remoteUser;

    public ConversationController(User r) {
        this.remoteUser = r;
    }

    public int sendMessage(Message msg) {
        MessagePacket msgPckt = new MessagePacket(msg);
        msgPckt.setSrc(User.localUser.getAddress());



        TCPSendSocket socket = new TCPSendSocket();
        socket.connect(remoteUser.getAddress());
        int res = socket.send(msgPckt);
        socket.close();

        msgPckt.store();

        return res;
    }

}
