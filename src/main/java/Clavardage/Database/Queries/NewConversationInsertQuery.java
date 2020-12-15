package Clavardage.Database.Queries;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class NewConversationInsertQuery extends UpdateQuery{
    private static final String QUERY = "INSERT INTO chat_room (name, numOfUsers) VALUES (?,?)";
    private static final int NUMBER_OF_ARGUMENTS = 2;

    public NewConversationInsertQuery(String sql) {
        super(QUERY);
    }

    @Override
    public PreparedStatement setParameters(QueryParameters parameters) {
        ArrayList<Object> parametersList = parameters.getParam();
        if (parametersList.size() != NUMBER_OF_ARGUMENTS) {
            System.err.println(NUMBER_OF_ARGUMENTS + " arguments needed");
        } else {
            try {
                this.statement.setString(2, (String) parametersList.get(0));
                this.statement.setInt(1, (Integer) parametersList.get(1));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }
}
