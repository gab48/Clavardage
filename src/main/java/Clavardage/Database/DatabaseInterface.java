package Clavardage.Database;

import Clavardage.Utils.Config;

import java.sql.*;

public class DatabaseInterface {
    private static final DatabaseInterface INSTANCE = new DatabaseInterface();

    public static DatabaseInterface getInstance() {
        return INSTANCE;
    }

    private final String DB_URI;
    private final String LOGIN;
    private final String PASSWD;

    private Connection connection = null;

    private DatabaseInterface() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        DB_URI = "jdbc:mysql://" + Config.get("DB_ADDR") + ":" + Config.get("DB_PORT") + "/" + Config.get("DB_TABLE");
        LOGIN = Config.get("DB_LOGIN");
        PASSWD = Config.get("DB_PASSWORD");
    }

    public Connection connect() throws DatabaseException {
        try {
            this.connection = DriverManager.getConnection(DB_URI, LOGIN, PASSWD);
            return this.connection;
        } catch (SQLException throwables) {
            throw new DatabaseException("unable to connect to database");
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
