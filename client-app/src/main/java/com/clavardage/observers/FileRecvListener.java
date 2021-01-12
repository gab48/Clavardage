package com.clavardage.observers;

import com.clavardage.models.Message;
import com.clavardage.models.User;
import com.clavardage.network.models.FilePacket;
import com.clavardage.network.models.MessagePacket;
import com.clavardage.network.sockets.FileSocket;
import com.clavardage.network.sockets.TCPRecvSocket;
import com.clavardage.utils.Config;

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
