package Clavardage.Network.Models;

import Clavardage.Database.Queries.Inserts.MessageInsertQuery;
import Clavardage.Database.Queries.QueryParameters;
import Clavardage.Managers.ConversationsManager;
import Clavardage.Models.Conversation;

public interface Storable<T extends MessagePacket> extends PacketBasics {
    default void store() {
        MessageInsertQuery msgInQ = new MessageInsertQuery();
        QueryParameters queryParameters = new QueryParameters();
        Conversation conversation = ConversationsManager.getInstance().getConversation(this.getDest());

        queryParameters.append(conversation.getId());      //TODO: Get room_id
        queryParameters.append(this.getSrc().toString());
        queryParameters.append(this.getPayload());

        msgInQ.prepare();
        msgInQ.setParameters(queryParameters);
        msgInQ.execute();
        msgInQ.close();
    }
}
