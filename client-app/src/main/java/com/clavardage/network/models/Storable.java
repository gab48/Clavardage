package com.clavardage.network.models;

import com.clavardage.database.queries.inserts.MessageInsertQuery;
import com.clavardage.database.queries.QueryParameters;
import com.clavardage.managers.ConversationsManager;
import com.clavardage.models.Conversation;
import com.clavardage.models.Message;

public interface Storable<T extends MessagePacket> extends PacketBasics {
    default void store() {
        MessageInsertQuery msgInQ = new MessageInsertQuery();
        QueryParameters queryParameters = new QueryParameters();
        Conversation conversation = ConversationsManager.getInstance().getConversation(this.getDest());
        Message msg = new Message();
        msg.unserialize(this.getPayload().getBytes());  //TODO: Change that

        queryParameters.append(conversation.getId());
        queryParameters.append(this.getSrc().toString());
        queryParameters.append(msg.getContent());
        queryParameters.append(msg.getTimestamp());
        queryParameters.append(msg.getType());

        msgInQ.prepare();
        msgInQ.setParameters(queryParameters);
        msgInQ.execute();
        msgInQ.close();
    }
}
