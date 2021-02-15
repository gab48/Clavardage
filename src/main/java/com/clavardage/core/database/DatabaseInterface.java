package com.clavardage.core.database;

import com.clavardage.client.views.AlertWindow;
import com.clavardage.core.utils.Config;

import java.sql.*;

public class DatabaseInterface {
    private static final DatabaseInterface INSTANCE = new DatabaseInterface();

    public static DatabaseInterface getInstance() {
        return INSTANCE;
    }

    private Connection connection = null;
    private String DB_URI;
    private String LOGIN;
    private String PASSWD;

    private DatabaseInterface() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        DB_URI = "jdbc:mysql://" + Config.getString("DB_ADDR") + ":" + Config.getString("DB_PORT") + "/" + Config.getString("DB_DATABASE");
        LOGIN = Config.getString("DB_LOGIN");
        PASSWD = Config.getString("DB_PASSWORD");
    }

    public void connect() throws DatabaseException {
       //String DB_URI = "jdbc:mysql://" + Config.get("DB_ADDR") + ":" + Config.get("DB_PORT") + "/" + Config.get("DB_DATABASE");
       //String LOGIN = Config.get("DB_LOGIN");
       //String PASSWD = Config.get("DB_PASSWORD");
        try {
            this.connection = DriverManager.getConnection(DB_URI, LOGIN, PASSWD);
        } catch (SQLException throwables) {
            AlertWindow.displayError("Unable to connect to database:\n" +
                    "Host: " + DB_URI + "\n" +
                    "Login: " + LOGIN + "\n" +
                    "Password: " + PASSWD);
            System.exit(1);
        }
    }

    public PreparedStatement prepare(String sqlPrep) throws DatabaseException {
        try {
            return this.connection.prepareStatement(sqlPrep);
        } catch (SQLException throwables) {
            throw new DatabaseException("statement preparation failed");
        }
    }

    public PreparedStatement prepareInsert(String sqlPrep) throws DatabaseException {
        try {
            return this.connection.prepareStatement(sqlPrep, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException throwables) {
            throw new DatabaseException("statement preparation failed");
        }
    }

    public ResultSet executeSelect(PreparedStatement statement) throws DatabaseException {
        try {
            return statement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DatabaseException("excute statement failed");
        }
    }

    public int executeUpdate(PreparedStatement statement) throws DatabaseException {
        try {
            return statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DatabaseException("excute statement failed");
        }
    }

    public int executeInsert(PreparedStatement statement) throws DatabaseException {
        try {
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DatabaseException("excute statement failed");
        }
    }

    public void disconnect() throws DatabaseException {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException throwables) {
                throw new DatabaseException("disconnection failed");
            }
        } else {
            throw new DatabaseException("already disconnected");
        }
    }

}
