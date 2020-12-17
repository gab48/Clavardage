package Clavardage.Database.Queries.Inserts;

import Clavardage.Database.Queries.QueryParameters;

import java.sql.SQLException;
import java.util.ArrayList;

public class NewConversationInsertQuery extends InsertQuery {
    private static final String QUERY = "INSERT INTO chat_room (name, users_number) VALUES (?,?)";
    private static final int NUMBER_OF_ARGUMENTS = 2;

    public NewConversationInsertQuery() {
        super(QUERY);
    }

    @Override
    public void setParameters(QueryParameters parameters) {
        ArrayList<Object> parametersList = parameters.getParam();
        if (parametersList.size() != NUMBER_OF_ARGUMENTS) {
            System.err.println(NUMBER_OF_ARGUMENTS + " arguments needed");
        } else {
            try {
                this.statement.setString(1, (String) parametersList.get(0));
                this.statement.setInt(2, (Integer) parametersList.get(1));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
