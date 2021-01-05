package Clavardage.Models;

import Clavardage.Database.Queries.QueryParameters;
import Clavardage.Database.Queries.Selects.HistorySelectQuery;
import Clavardage.Network.Models.Address;

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
                        resultSet.getTimestamp("created_at")
                ));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        query.close();

    }

    public void append(Message msg) {
        this.messagesHistory.add(msg);

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
