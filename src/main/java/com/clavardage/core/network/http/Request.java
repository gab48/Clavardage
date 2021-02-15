package com.clavardage.core.network.http;

import com.clavardage.client.views.AlertWindow;
import com.clavardage.core.utils.Config;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

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
            this.response = streamToString(response);

            output.close();
            response.close();
        } catch (UnknownHostException e) {
            AlertWindow.displayError("Unable to access servlet at address " + Config.getString("SERVLET_ADDR"));
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeGet() {
        try {
            URLConnection connection = this.url.openConnection();
            connection.setRequestProperty("Accept-Charset", CHARSET);
            InputStream response = connection.getInputStream();

            this.response = streamToString(response);

            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String streamToString(InputStream is) {
        int ch;
        StringBuilder sb = new StringBuilder();
        try {
            while ((ch = is.read()) != -1)
                sb.append((char) ch);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String getResponse() {
        return response;
    }
}
