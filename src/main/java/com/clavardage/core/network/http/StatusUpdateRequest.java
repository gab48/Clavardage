package com.clavardage.core.network.http;

import com.clavardage.servlet.ClavardageServlet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StatusUpdateRequest extends Request {

    public StatusUpdateRequest(String targetURL, String user_id, int status) {
        super(targetURL);
        try {
            this.query= String.format(ClavardageServlet.USER_FIELD+"=%s&"+ ClavardageServlet.STATUS_FIELD+"=%s",
                    URLEncoder.encode(user_id, CHARSET),
                    URLEncoder.encode(String.valueOf(status), CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
