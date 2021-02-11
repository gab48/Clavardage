package com.clavardage.core.database.queries;

import com.clavardage.core.database.DatabaseException;
import com.clavardage.core.database.DatabaseInterface;

import java.sql.PreparedStatement;

public abstract class DatabaseQuery {
    protected final String sqlQuery;
    protected final DatabaseInterface db = DatabaseInterface.getInstance();
    protected PreparedStatement statement;

    public DatabaseQuery(String sql) {
        this.sqlQuery = sql;
    }

    public void prepare() {
        try {
            this.db.connect();
            this.statement = this.db.prepare(this.sqlQuery);
        } catch (DatabaseException throwables) {
            throwables.printStackTrace();
        }
    }

    public abstract void setParameters(QueryParameters parameters);

    public void close() {
        try {
            this.db.disconnect();
        } catch (DatabaseException throwables) {
            throwables.printStackTrace();
        }
    }
}
