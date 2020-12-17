package Clavardage.Database.Queries.Selects;

import Clavardage.Database.DatabaseException;
import Clavardage.Database.Queries.DatabaseQuery;

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
