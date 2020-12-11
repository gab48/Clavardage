package Clavardage.Database.Queries;

import Clavardage.Database.DatabaseException;

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
