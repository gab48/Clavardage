package Clavardage.Network.Models;

import Clavardage.Database.Queries.MessageInsertQuery;
import Clavardage.Database.Queries.QueryParameters;

public interface Storable<T extends MessagePacket> extends PacketBasics {
    default void store() {
        MessageInsertQuery msgInQ = new MessageInsertQuery();
        QueryParameters queryParameters = new QueryParameters();

        queryParameters.append(1);      //TODO: Get room_id
        queryParameters.append(this.getSrc().toString());
        queryParameters.append(this.getPayload());

        msgInQ.prepare();
        msgInQ.setParameters(queryParameters);
        msgInQ.execute();
        msgInQ.close();
    }
}
