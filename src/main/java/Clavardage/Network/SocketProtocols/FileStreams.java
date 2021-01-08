package Clavardage.Network.SocketProtocols;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileStreams {

    private final InputStream inputStream;
    private final OutputStream outputStream;

    public FileStreams(InputStream input, OutputStream output) {
        this.inputStream = input;
        this.outputStream = output;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void close() {
        try {
            this.inputStream.close();
            this.outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
