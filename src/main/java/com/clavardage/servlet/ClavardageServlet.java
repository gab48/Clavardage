package com.clavardage.servlet;

import com.clavardage.core.database.queries.selects.UsersStatusSelectQuery;
import com.clavardage.core.database.queries.updates.UserStatusUpdateQuery;
import com.clavardage.core.database.queries.QueryParameters;
import com.clavardage.core.utils.Config;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Properties;
import org.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

@WebServlet(name="ClavardageServlet", value="/edit")
public class ClavardageServlet extends HttpServlet {
    public static final String USER_FIELD = "user";
    public static final String STATUS_FIELD = "status";
    private static final String CONFIG_FILE_NAME = ".config";
    private String message;
    private final JSONObject json;

    public ClavardageServlet() throws IOException {
        String root = this.getClass().getResource("/").toString().substring("file:".length()) ;

        this.json = new JSONObject();

        Properties conf = new Properties();
        FileInputStream confFile = new FileInputStream(root+CONFIG_FILE_NAME);
        conf.load(confFile);
        Config.getInstance().setProperties(conf);
    }

    public void init() {}

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String user = request.getParameter(USER_FIELD);
        String status = request.getParameter(STATUS_FIELD);
        //TODO: Sanitize inputs

        UserStatusUpdateQuery updateQuery = new UserStatusUpdateQuery();
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.append(user);
        queryParameters.append(status);

        updateQuery.prepare();
        updateQuery.setParameters(queryParameters);
        this.message = String.valueOf(updateQuery.execute());
        updateQuery.close();

        this.makeResponse(response);
    }

    private void updateJson() {
        UsersStatusSelectQuery selectQuery = new UsersStatusSelectQuery();
        selectQuery.prepare();
        selectQuery.setParameters(null);
        ResultSet resultSet = selectQuery.execute();

        String id;
        int status;

        try {
            while(resultSet.next()) {
                id = resultSet.getString("id");
                status = Integer.parseInt(resultSet.getString("status"));
                this.json.put(id, status);
            }
            resultSet.close();
        } catch (SQLException ignore) {}
        selectQuery.close();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json") ;
        updateJson();
        PrintWriter out = response.getWriter();
        out.println(this.json);
    }

    private void makeResponse(HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(this.message);
    }

    public void destroy() {}
}
