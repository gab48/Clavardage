package Clavardage.Observers;

import Clavardage.Models.Message;
import Clavardage.Models.User;
import Clavardage.Network.Models.FilePacket;
import Clavardage.Network.Models.MessagePacket;
import Clavardage.Network.SocketProtocols.FileSocket;
import Clavardage.Network.SocketProtocols.TCPRecvSocket;
import Clavardage.Utils.Config;

import java.util.Date;

public class FileRecvListener implements Listener{
    private static final FileRecvListener INSTANCE = new FileRecvListener();
    private FileRecvListener() {}
    public static FileRecvListener getINSTANCE() { return INSTANCE; }

    @Override
    public void handle(Object... args) {
        Message msg = (Message) args[1];
        TCPRecvSocket socket = (TCPRecvSocket) args[2];
        if (msg.getType().equals(Message.MessageType.FILE)) {
            FilePacket filePacket = new FilePacket();
            filePacket.setFileName(msg.getContent());

            FileSocket fileSocket = new FileSocket(Short.parseShort(Config.get("NETWORK_TCP_FILE_PORT")));

            new Thread( () -> {
                fileSocket.accept();
                fileSocket.recv(filePacket);
                fileSocket.close();
            }).start();

            //Send FILE_ACK
            Message messageACK = new Message("", new Date().getTime(), Message.MessageType.FILE_ACK);
            MessagePacket ack = new MessagePacket(messageACK, User.localUser.getAddress());
            socket.send(ack);
        }
    }
}
