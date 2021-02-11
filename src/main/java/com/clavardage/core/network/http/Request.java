package com.clavardage.core.network.http;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public abstract class Request {

    private URL url;
    protected static final String CHARSET = "UTF-8";
    protected String query;
    private String response;

    public Request(String targetURL) {
        try {
            this.url = new URL(targetURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void executePost() {
        try {
            URLConnection connection = this.url.openConnection();
            connection.setDoOutput(true); // Triggers POST.
            connection.setRequestProperty("Accept-Charset", CHARSET);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + CHARSET);

            OutputStream output = connection.getOutputStream();
            output.write(this.query.getBytes(CHARSET));

            InputStream response = connection.getInputStream();
            this.response = IOUtils.toString(response, CHARSET);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeGet() {
        try {
            URLConnection connection = this.url.openConnection();
            connection.setRequestProperty("Accept-Charset", CHARSET);
            InputStream response = connection.getInputStream();

            this.response = IOUtils.toString(response, CHARSET);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getResponse() {
        return response;
    }
}
