package Clavardage.Views;

import Clavardage.Models.User;

import javax.swing.*;
import java.awt.*;

public class TabbedConversations extends JTabbedPane {

    public TabbedConversations() {
        this.setPreferredSize(new Dimension(805, 501));
    }

    public void addConversation(User remoteUser) {
        if (this.getTabCount() > 1) {
            System.err.println("Only one conversation allowed at the moment.");
            System.exit(1);
        } else {
            ConversationPanel conversationPanel = new ConversationPanel(remoteUser);
            this.addTab(remoteUser.getNickname(), conversationPanel);
        }
    }

    public ConversationPanel getConversation() {
        return (ConversationPanel) this.getComponentAt(0);
    }
}
