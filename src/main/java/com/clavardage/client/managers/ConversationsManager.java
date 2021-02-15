package com.clavardage.client.managers;

import com.clavardage.core.database.queries.selects.ConversationsSelectQuery;
import com.clavardage.core.database.queries.QueryParameters;
import com.clavardage.core.models.Conversation;
import com.clavardage.core.models.User;
import com.clavardage.core.network.models.Address;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConversationsManager implements Manager {
    private static final ConversationsManager INSTANCE = new ConversationsManager();

    private final ArrayList<Conversation> startedConversations = new ArrayList<>();

    private ConversationsManager() {
        this.updateStartedConversations();
    }

    private void updateStartedConversations() {
        ConversationsSelectQuery query = new ConversationsSelectQuery();
        QueryParameters parameters = new QueryParameters();

        parameters.append(User.localUser.getAddress().toString());
        query.prepare();
        query.setParameters(parameters);
        ResultSet resultSet = query.execute();

        try {
            while(resultSet.next()) {
                Conversation conversation = new Conversation(resultSet.getInt("room_id"));
                if (!this.startedConversations.contains(conversation)) {
                    this.startedConversations.add(conversation);
                }
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

    public Conversation createConversation(User remoteUser) {
        Conversation newConversation = new Conversation(remoteUser);
        newConversation.store();
        User.localUser.join(newConversation);
        remoteUser.join(newConversation);
        this.startedConversations.add(newConversation);
        return newConversation;
    }

    public Conversation getConversation(Address remoteUserAddr) {
        this.updateStartedConversations();
        for(Conversation conv : this.startedConversations) {
            if(conv.getParticipants().size() == 2) {
                if(conv.getParticipants().get(1).getAddress().equals(remoteUserAddr)) {
                    return conv;
                }
            }
        }

        // If conversation not found, we create a new one
        User newUser = UsersManager.getInstance().getUser(remoteUserAddr);
        return this.createConversation(newUser);
    }

    public ArrayList<Conversation> getStartedConversations() {
        return startedConversations;
    }

    @Override
    public void stop() {}
}
