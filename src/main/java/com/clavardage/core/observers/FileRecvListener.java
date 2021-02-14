package com.clavardage.core.observers;

import com.clavardage.core.models.Message;
import com.clavardage.core.models.User;
import com.clavardage.core.network.models.FilePacket;
import com.clavardage.core.network.models.MessagePacket;
import com.clavardage.core.network.sockets.FileSocket;
import com.clavardage.core.network.sockets.TCPRecvSocket;
import com.clavardage.core.utils.Config;

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

            FileSocket fileSocket = new FileSocket(Short.parseShort(Config.get("NETWORK_FILE_TRANSFERT_PORT")));

            new Thread(() -> {
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
