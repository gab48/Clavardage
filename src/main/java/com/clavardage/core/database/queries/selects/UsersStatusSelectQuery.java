package com.clavardage.core.database.queries.selects;

import com.clavardage.core.database.queries.QueryParameters;

import java.util.ArrayList;

public class UsersStatusSelectQuery extends SelectQuery{
    private static final String QUERY = "SELECT * FROM user_status";
    private static final int NUMBER_OF_ARGUMENTS = 0;

    public UsersStatusSelectQuery() {
        super(QUERY);
    }

    @Override
    public void setParameters(QueryParameters parameters) {
        if (parameters != null) {
            System.err.println(NUMBER_OF_ARGUMENTS + " arguments needed");
        }
    }
}
