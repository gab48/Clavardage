package Clavardage.Views;

import Clavardage.Models.LocalUser;
import Clavardage.Models.Message;
import Clavardage.Models.User;
import Clavardage.Network.Controllers.ConversationController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConversationPanel extends JPanel{
    private static final String SEND_TEXT_FIELD_PLACEHOLDER = "Type a message...";

    private final User remoteUser;
    private final ConversationController conversationController;

    private final JTextArea conversationTextArea;

    private final JTextField sendTextField;

    private boolean defaultText;

    public ConversationPanel(User remoteUser) {
        this.remoteUser = remoteUser;
        this.conversationController = new ConversationController(this.remoteUser);

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.conversationTextArea = new JTextArea();
        this.conversationTextArea.setEditable(false);
        this.conversationTextArea.setPreferredSize(new Dimension(800, 450));
        this.add(this.conversationTextArea);

        this.sendTextField = new JTextField("Type a message...");
        this.defaultText = true;
        JButton sendButton = new JButton("Send");
        Dimension dim = sendButton.getPreferredSize();
        this.sendTextField.setPreferredSize(new Dimension(700, dim.height));

        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new BoxLayout(sendPanel, BoxLayout.LINE_AXIS));
        sendPanel.add(this.sendTextField);
        sendPanel.add(sendButton);

        this.add(sendPanel);

        sendButton.addActionListener(e -> sendMessage());

        this.sendTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (defaultText && sendTextField.getText().equals(ConversationPanel.SEND_TEXT_FIELD_PLACEHOLDER)) {
                    sendTextField.setText("");
                    defaultText = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (!defaultText && sendTextField.getText().isEmpty()) {
                    sendTextField.setText(ConversationPanel.SEND_TEXT_FIELD_PLACEHOLDER);
                    defaultText = true;
                }
            }
        });

        this.sendTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
    }

    private void sendMessage() {
        String content = this.sendTextField.getText();

        //TODO: Never trust user input (more precisely than empty messages?)
        if (content.length() > 0 && !this.defaultText) {
            Message message = new Message(content);
            appendMessage(LocalUser.getInstance(), message);
            if (this.sendTextField.hasFocus()) {
                this.sendTextField.setText("");
            } else {
                this.sendTextField.setText(ConversationPanel.SEND_TEXT_FIELD_PLACEHOLDER);
                this.defaultText = true;
            }

            Runnable code = () -> this.conversationController.sendMessage(message);
            new Thread(code).start();
        }
    }

    private void appendMessage(User sender, Message message) {
        conversationTextArea.append("[" + message.getTime() + "] <" + sender.getNickname() + "> "
                + message.getContent() + "\n");
    }

    public void receiveMessage(Message message) {
        SwingUtilities.invokeLater(() -> appendMessage(remoteUser, message));
    }
}
