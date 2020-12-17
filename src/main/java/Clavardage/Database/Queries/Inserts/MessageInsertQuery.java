package Clavardage.Database.Queries.Inserts;

import Clavardage.Database.Queries.QueryParameters;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class MessageInsertQuery extends InsertQuery {
    private static final String QUERY = "INSERT INTO chat_message (room_id, sender, message, created_at) VALUES (?,?,?,?)";
    private static final int NUMBER_OF_ARGUMENTS = 4;
    public MessageInsertQuery() {
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
                this.statement.setString(2, (String) parametersList.get(1));
                this.statement.setString(3, (String) parametersList.get(2));
                this.statement.setTimestamp(4, (Timestamp) parametersList.get(3));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}

