package com.clavardage.core.database.queries.selects;

import com.clavardage.core.database.DatabaseException;
import com.clavardage.core.database.queries.DatabaseQuery;

import java.sql.ResultSet;

public abstract class SelectQuery extends DatabaseQuery {
    public SelectQuery(String sql) {
        super(sql);
    }

    public ResultSet execute() {
        try {
            return this.db.executeSelect(this.statement);
        } catch (DatabaseException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
