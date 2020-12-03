package Views;

import Models.User;

import javax.swing.*;
import java.awt.*;

public class TabbedConversations extends JTabbedPane {
    private final User localUser;

    public TabbedConversations(User localUser) {
        this.localUser = localUser;
        this.setPreferredSize(new Dimension(805, 501));
    }

    public void addConversation(User remoteUser) {
        ConversationPanel conversationPanel = new ConversationPanel(localUser, remoteUser);
        this.addTab(remoteUser.getNickname(), conversationPanel);
    }
}
