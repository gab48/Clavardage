package com.clavardage.network.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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

    public void setFileName(String fileName) { this.fileName = fileName; }
}
