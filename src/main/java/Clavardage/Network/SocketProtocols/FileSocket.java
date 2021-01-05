package Clavardage.Network.SocketProtocols;

import Clavardage.Network.Models.Address;
import Clavardage.Network.Models.FilePacket;
import Clavardage.Utils.Config;

import java.io.*;
import java.net.ServerSocket;

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
        this.link = super.connect(addr);
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

    @Override
    public int send(FilePacket packet) {

        // TODO: Send FileName

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
        String filename = "testcopy.txt";

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
