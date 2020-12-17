package Clavardage.Database.Queries.Updates;

import Clavardage.Database.DatabaseException;
import Clavardage.Database.Queries.DatabaseQuery;

import java.sql.Statement;

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
