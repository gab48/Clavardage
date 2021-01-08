package Clavardage.Database.Queries.Selects;

import Clavardage.Database.Queries.QueryParameters;

import java.sql.SQLException;
import java.util.ArrayList;

public class HistorySelectQuery extends SelectQuery {
    private static final String QUERY = "SELECT sender,message,created_at,type FROM chat_message WHERE room_id=? ORDER BY created_at";
    private static final int NUMBER_OF_ARGUMENTS = 1;

    public HistorySelectQuery() {
        super(QUERY);
    }

    @Override
    public void setParameters(QueryParameters parameters) {
        ArrayList<Object> parametersList = parameters.getParam();
        if (parametersList.size() != NUMBER_OF_ARGUMENTS) {
            System.err.println(NUMBER_OF_ARGUMENTS + " arguments needed");
        } else {
            try {
                this.statement.setInt(1, (Integer) parametersList.get(0));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
