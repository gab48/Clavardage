package Clavardage.Network.Models;

import java.io.*;

public class FilePacket extends MessagePacket {

    private InputStream fileStream = null;
    private String fileName;
    private long fileLength;

    public FilePacket(File file, Address destination) {
        this();
        this.fileLength = file.length();
        this.fileName = file.getName();
        try {
            this.fileStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        }
        this.dest = destination;
    }

    public FilePacket () {
        super();
    }

    public InputStream getFileStream() { return this.fileStream; }
    public long getFileLength() { return this.fileLength; }
    public String getFileName() { return fileName; }
}
