package com.clavardage.database;

import java.sql.SQLException;

public class DatabaseException extends SQLException {
    public DatabaseException(String str) {
        super(str);
        System.err.println("Database error : " + str);
    }
}
