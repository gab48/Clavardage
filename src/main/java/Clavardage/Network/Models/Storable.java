package Clavardage.Network.Models;

import Clavardage.Database.Queries.Inserts.MessageInsertQuery;
import Clavardage.Database.Queries.QueryParameters;
import Clavardage.Managers.ConversationsManager;
import Clavardage.Models.Conversation;
import Clavardage.Models.Message;

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
