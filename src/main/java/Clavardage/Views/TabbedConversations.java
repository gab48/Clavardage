package Clavardage.Views;

import Clavardage.Models.Message;
import Clavardage.Models.User;
import Clavardage.Observers.Listener;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TabbedConversations extends JTabbedPane implements Listener {

    private final Map<String, ConversationPanel> conversationsByNickname = new HashMap<>();

    public TabbedConversations() {
        this.setPreferredSize(new Dimension(805, 501));
    }

    public void addConversation(User remoteUser) {
        if (this.conversationsByNickname.containsKey(remoteUser.getNickname())) {
            throw new IllegalArgumentException("Conversation already opened");
        }

        ConversationPanel conversationPanel = new ConversationPanel(remoteUser);
        this.conversationsByNickname.put(remoteUser.getNickname(), conversationPanel);
        this.addTab(remoteUser.getNickname(), conversationPanel);
    }

    public void focusConversation(User remoteUser) {
        ConversationPanel conversation = this.conversationsByNickname.get(remoteUser.getNickname());
        this.setSelectedComponent(conversation);
    }

    public ConversationPanel getConversation(User remoteUser) {
        return this.conversationsByNickname.get(remoteUser.getNickname());
    }

    @Override
    public void handle(Object... args) {
        User remoteUser = (User) args[0];
        Message message = (Message) args[1];
        this.getConversation(remoteUser).receiveMessage(message);
    }
}
