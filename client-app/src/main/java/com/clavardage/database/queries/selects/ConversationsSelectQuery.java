package com.clavardage.database.queries.selects;

import com.clavardage.database.queries.QueryParameters;

import java.sql.SQLException;
import java.util.ArrayList;

public class ConversationsSelectQuery extends SelectQuery {
    private static final String QUERY = "SELECT room_id FROM chat_participant WHERE user_id=?";
    private static final int NUMBER_OF_ARGUMENTS = 1;

    public ConversationsSelectQuery() {
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
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
