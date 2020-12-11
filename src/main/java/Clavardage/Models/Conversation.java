package Clavardage.Models;

import java.util.ArrayList;

public class Conversation {
    private int id;
    private String name = null;
    private final ArrayList<User> participants = new ArrayList<>();

    public Conversation(ArrayList<User> users) {
        this.participants.add(LocalUser.getInstance());
        this.participants.addAll(users);
    }

    static ArrayList<User> castUserToArray(User user) {
        ArrayList<User> tmp = new ArrayList<>();
        tmp.add(user);
        return tmp;
    }

    public Conversation(User user) {
        this(castUserToArray(user));
    }

    public int getId() { return id; }
    public ArrayList<User> getParticipants() { return participants; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
