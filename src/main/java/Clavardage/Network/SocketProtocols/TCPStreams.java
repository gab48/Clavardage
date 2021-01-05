package Clavardage.Network.SocketProtocols;

import Clavardage.Network.Models.Packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPStreams<T extends Packet> {

    private PrintWriter     writer = null;
    private BufferedReader  reader = null;

    public TCPStreams(Socket sock) {
        try {
            this.writer = new PrintWriter(sock.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        } catch (IOException e) {
            System.err.println("An error occurred trying to instantiate writer or reader");
            e.printStackTrace();
        }
    }

    public void send(T packet) {
        this.writer.println(new String(packet.serialize()));
    }

    public T recv(T packet) {
        try {
            String buf = this.reader.readLine();
            packet.unserialize(buf.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return packet;
    }

    public void close() {
        try {
            this.reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.writer.close();
    }
}
