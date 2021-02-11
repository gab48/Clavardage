package com.clavardage.core.database.queries.selects;

import com.clavardage.core.database.queries.QueryParameters;
import com.clavardage.core.models.User;

import java.sql.SQLException;
import java.util.ArrayList;

public class ConversationMemberSelectQuery extends SelectQuery {
    private static final String QUERY = "SELECT user_id FROM chat_participant WHERE room_id=? AND user_id<>?";
    private static final int NUMBER_OF_ARGUMENTS = 1;

    public ConversationMemberSelectQuery() {
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
                this.statement.setString(2, User.localUser.getAddress().toString());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
