package com.clavardage.core.database.queries.inserts;

import com.clavardage.core.database.DatabaseException;
import com.clavardage.core.database.queries.DatabaseQuery;

public abstract class InsertQuery extends DatabaseQuery {
    public InsertQuery(String sql) {
        super(sql);
    }

    public void prepare() {
        try {
            this.db.connect();
            this.statement = this.db.prepareInsert(this.sqlQuery);
        } catch (DatabaseException throwables) {
            throwables.printStackTrace();
        }
    }

    public int execute() {
        try {
            return this.db.executeInsert(this.statement);
        } catch (DatabaseException throwables) {
            throwables.printStackTrace();
            return -1;
        }
    }
}
