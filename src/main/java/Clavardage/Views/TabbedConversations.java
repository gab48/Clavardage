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

    public ConversationPanel addConversation(User remoteUser) {
        if (this.conversationsByNickname.containsKey(remoteUser.getNickname())) {
            throw new IllegalArgumentException("Conversation already opened");
        }

        ConversationPanel conversationPanel = new ConversationPanel(remoteUser);
        this.conversationsByNickname.put(remoteUser.getNickname(), conversationPanel);
        this.addTab(remoteUser.getNickname(), conversationPanel);
        return conversationPanel;
    }

    public void focusConversation(User remoteUser) {
        ConversationPanel conversation = this.conversationsByNickname.get(remoteUser.getNickname());
        this.setSelectedComponent(conversation);
    }

    public ConversationPanel getConversation(User remoteUser) {
        ConversationPanel conversation = this.conversationsByNickname.get(remoteUser.getNickname());
        if (conversation != null) {
            return conversation;
        } else {
            return this.addConversation(remoteUser);
        }
    }

    @Override
    public void handle(Object... args) {
        User remoteUser = (User) args[0];
        Message message = (Message) args[1];
        if (message.getType().equals(Message.MessageType.TEXT)) {
            this.getConversation(remoteUser).receiveMessage(message);
        }
    }
}
