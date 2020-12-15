package Clavardage.Models;

import Clavardage.Database.Queries.ConversationMemberSelectQuery;
import Clavardage.Database.Queries.QueryParameters;
import Clavardage.Network.Models.Address;
import Clavardage.Utils.Storable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Conversation implements Storable {
    private int id;
    private String name = null;
    private final ArrayList<User> participants = new ArrayList<>();

    public Conversation(ArrayList<User> users) {
        this.participants.add(0, User.localUser);
        this.participants.addAll(users);
    }

    private static ArrayList<User> castUserToArray(User user) {
        ArrayList<User> tmp = new ArrayList<>();
        tmp.add(user);
        return tmp;
    }

    public Conversation(User user) {
        this(castUserToArray(user));
    }

    public Conversation(int id) {
        this.id = id;
        this.participants.add(0, User.localUser);

        ConversationMemberSelectQuery query = new ConversationMemberSelectQuery();
        QueryParameters parameters = new QueryParameters();

        parameters.append(id);
        query.prepare();
        query.setParameters(parameters);
        ResultSet resultSet = query.execute();
        ArrayList<User> remoteParticipants = new ArrayList<>();
        try {
            while(resultSet.next()) {
                remoteParticipants.add(new User(new Address(resultSet.getString("user_id"))));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        query.close();

        this.participants.addAll(remoteParticipants);
    }

    public int getId() { return id; }
    public ArrayList<User> getParticipants() { return participants; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public void store() {
        System.out.println("Storing new conversation with : " + participants.get(1));
    }
}
