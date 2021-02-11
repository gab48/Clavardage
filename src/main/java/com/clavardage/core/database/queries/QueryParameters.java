package com.clavardage.core.database.queries;

import java.util.ArrayList;

public class QueryParameters {
    private final ArrayList<Object> param = new ArrayList<>();

    public QueryParameters() {}

    public ArrayList<Object> getParam() {
        return param;
    }

    public void append(Object arg) {
        this.param.add(arg);
    }
}
