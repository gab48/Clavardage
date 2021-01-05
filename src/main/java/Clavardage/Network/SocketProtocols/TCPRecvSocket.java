package Clavardage.Network.SocketProtocols;

import Clavardage.Network.Models.MessagePacket;

import java.io.IOException;
import java.net.ServerSocket;

public class TCPRecvSocket extends TCPsocket<MessagePacket> {

    private ServerSocket    srv     = null;

    public TCPRecvSocket(short port) {
        try {
            this.srv = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int accept() {
        try {
            this.link = this.srv.accept();
            this.streams = new TCPStreams<>(this.link);
        } catch (IOException e) {
            return -1;
        }
        return 0;
    }

    @Override
    public int close() {
        this.streams.close();
        try {
            if (!this.srv.isClosed()) {
                this.srv.close();
            }
            if (!this.link.isClosed()) {
                this.link.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }
}
