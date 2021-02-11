package com.clavardage.core.network.sockets;

import com.clavardage.core.models.Message;
import com.clavardage.core.network.models.Address;
import com.clavardage.core.network.models.FilePacket;
import com.clavardage.core.network.models.MessagePacket;
import com.clavardage.core.utils.Config;

import java.io.*;
import java.net.ServerSocket;
import java.util.Date;

public class FileSocket extends TCPsocket<FilePacket> {

    private FileStreams fileStreams;
    private ServerSocket srv = null;

    public FileSocket(short port) {
        try {
            this.srv = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileSocket(){}

    @Override
    public java.net.Socket connect(Address addr) {
        this.remoteAddr = addr;
        return this.link;
    }

    @Override
    public int accept() {
        try {
            this.link = this.srv.accept();

        } catch (IOException e) {
            return -1;
        }
        return 0;
    }

    private void sendFileName(FilePacket packet) {
        String filename = packet.getFileName();
        Address destination = new Address(packet.getDest().getIp(), Short.parseShort(Config.get("NETWORK_TCP_SRV_PORT")));
        Address source = packet.getSrc();

        Message filenameMessage = new Message(filename, (new Date()).getTime(), Message.MessageType.FILE);
        MessagePacket filenamePacket = new MessagePacket(filenameMessage, destination);
        filenamePacket.setSrc(source);

        TCPSendSocket socket = new TCPSendSocket();
        socket.connect(destination);
        socket.send(filenamePacket);
        filenamePacket.store();

        // Wait for ack filename ack
        MessagePacket ack = new MessagePacket();
        ack = socket.recv(ack);
        Message ackMessage = new Message();
        ackMessage.unserialize(ack.serialize());
        if (ackMessage.getType().compareTo(Message.MessageType.FILE_ACK) != 0) {
            System.exit(-8000);
        }

        socket.close();
    }

    @Override
    public int send(FilePacket packet) {

        this.sendFileName(packet);

        this.link = super.connect(this.remoteAddr);

        long length = packet.getFileLength();
        byte[] bytes = new byte[1024];

        try {
            this.fileStreams = new FileStreams(packet.getFileStream(), this.link.getOutputStream());

            int count;
            while ((count = this.fileStreams.getInputStream().read(bytes)) > 0) {
                this.fileStreams.getOutputStream().write(bytes, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public FilePacket recv(FilePacket packet) {

        // TODO: Receive Filename
        String filename = packet.getFileName();

        try {
            this.fileStreams = new FileStreams(
                    this.link.getInputStream(),
                    new FileOutputStream(Config.get("FILE_DIRECTORY")+filename));

            byte[] bytes = new byte[1024];

            int count;
            while ((count = this.fileStreams.getInputStream().read(bytes)) > 0) {
                this.fileStreams.getOutputStream().write(bytes, 0, count);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public int close() {
        this.fileStreams.close();
        try {
            if (this.srv != null && !this.srv.isClosed()) this.srv.close();
            if (!this.link.isClosed()) this.link.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
