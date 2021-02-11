package com.clavardage.core.database.queries.updates;

import com.clavardage.core.database.queries.QueryParameters;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserStatusUpdateQuery extends UpdateQuery {
    private static final String QUERY = "INSERT INTO user_status (id, status) VALUES (?, ?) ON DUPLICATE KEY UPDATE status=VALUES(status)";
    private static final int NUMBER_OF_ARGUMENTS = 2; // user_id and new_status

    public UserStatusUpdateQuery() { super(QUERY); }

    @Override
    public void setParameters(QueryParameters parameters) {
        ArrayList<Object> parametersList = parameters.getParam();
        if (parametersList.size() != NUMBER_OF_ARGUMENTS) {
            System.err.println(NUMBER_OF_ARGUMENTS + " arguments needed");
        } else {
            try {
                String test = (String) parametersList.get(0);
                this.statement.setString(1,
                        (String) parametersList
                                .get(0));
                this.statement.setString(2, (String) parametersList.get(1));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}

