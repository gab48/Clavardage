package com.clavardage.models;

import com.clavardage.database.queries.QueryParameters;
import com.clavardage.database.queries.selects.HistorySelectQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class History {

    private final int conversationId;
    private final ArrayList<Message> messagesHistory = new ArrayList<>();

    public History(int room_id) {
        this.conversationId = room_id;
        HistorySelectQuery query = new HistorySelectQuery();
        QueryParameters parameters = new QueryParameters();

        parameters.append(this.conversationId);
        query.prepare();
        query.setParameters(parameters);
        ResultSet resultSet = query.execute();
        try {
            while(resultSet.next()) {
                this.messagesHistory.add(new Message(
                        resultSet.getString("sender"),
                        resultSet.getString("message"),
                        resultSet.getTimestamp("created_at"),
                        Message.MessageType.valueOf(resultSet.getString("type"))
                ));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        query.close();
    }

    public ArrayList<Message> getMessagesHistory() {
        return this.messagesHistory;
    }

    public void printHistory(){
        System.out.println("History of conversation " + this.conversationId);
        for (Message message : this.messagesHistory) {
            System.out.println("\t" + message);
        }
    }
}
