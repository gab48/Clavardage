package Clavardage.Managers;

import Clavardage.Database.Queries.ConversationsSelectQuery;
import Clavardage.Database.Queries.QueryParameters;
import Clavardage.Models.Conversation;
import Clavardage.Models.User;
import Clavardage.Network.Models.Address;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConversationsManager implements Manager {
    private static final ConversationsManager INSTANCE = new ConversationsManager();

    private final ArrayList<Conversation> startedConversations = new ArrayList<>();

    private ConversationsManager() {
        ConversationsSelectQuery query = new ConversationsSelectQuery();
        QueryParameters parameters = new QueryParameters();

        parameters.append(User.localUser.getAddress().toString());
        query.prepare();
        query.setParameters(parameters);
        ResultSet resultSet = query.execute();
        try {
            while(resultSet.next()) {
                this.startedConversations.add(new Conversation(resultSet.getInt("room_id")));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        query.close();
    }

    public static ConversationsManager getInstance() {
        return INSTANCE;
    }

    public void createConversation(User remoteUser) {
        Conversation newConversation = new Conversation(remoteUser);
        newConversation.store();
        this.newConversation(remoteUser);
    }

    public void newConversation(User remoteUser) {
        this.startedConversations.add(new Conversation(remoteUser));
    }

    public Conversation getConversation(Address remoteUserAddr) {
        for(Conversation conv : this.startedConversations) {
            if(conv.getParticipants().size() == 2) {
                if(conv.getParticipants().get(1).getAddress().equals(remoteUserAddr)) {
                    return conv;
                }
            }
        }
        return null;
    }

    public ArrayList<Conversation> getStartedConversations() {
        return startedConversations;
    }
}
