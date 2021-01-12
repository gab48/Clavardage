package com.clavardage.database.queries.updates;

import com.clavardage.database.DatabaseException;
import com.clavardage.database.queries.DatabaseQuery;

public abstract class UpdateQuery extends DatabaseQuery {
    public UpdateQuery(String sql) {
        super(sql);
    }

    public int execute() {
        try {
            return this.db.executeUpdate(this.statement);
        } catch (DatabaseException throwables) {
            throwables.printStackTrace();
            return -1;
        }
    }
}
