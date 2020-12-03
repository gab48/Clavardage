package Clavardage.Views;

import Clavardage.Models.User;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public final TabbedConversations conversations;

    public MainWindow() {
        super("Clavardage");

        JLabel titleLabel = new JLabel();
        //TODO: Make the connected user list
        titleLabel.setText("<html><p style='text-align: center;'>Placeholder for<br />connected user list</p></html>");

        JPanel subPanel = new JPanel();
        subPanel.setBackground(Color.GREEN);
        subPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        this.conversations = new TabbedConversations();
        subPanel.add(this.conversations);

        JPanel entirePanel = new JPanel();
        entirePanel.setLayout(new BoxLayout(entirePanel, BoxLayout.LINE_AXIS));

        entirePanel.add(titleLabel);
        entirePanel.add(subPanel);

        this.setContentPane(entirePanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void addConversation(User remoteUser) {
        SwingUtilities.invokeLater(() -> {
            conversations.addConversation(remoteUser);
            pack();
        });
    }

    public ConversationPanel getConversation() { return this.conversations.getConversation(); }
}
