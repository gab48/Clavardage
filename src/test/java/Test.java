import Clavardage.Database.Queries.Inserts.NewConversationInsertQuery;
import Clavardage.Database.Queries.QueryParameters;

public class Test {
    public static void main(String[] args) {
        NewConversationInsertQuery query = new NewConversationInsertQuery();
        QueryParameters parameters = new QueryParameters();

        parameters.append("toto");
        parameters.append(2);
        query.prepare();
        query.setParameters(parameters);
        int id = query.execute();
        query.close();
    }
}
