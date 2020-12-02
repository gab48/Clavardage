package Clavardage.Views;

import Clavardage.Models.Message;
import Clavardage.Models.User;
import Clavardage.Network.Controllers.ConversationController;

import javax.swing.*;
import java.awt.event.*;

public class ConversationWindow {
    private static final String SEND_TEXT_FIELD_PLACEHOLDER = "Type a message...";

    private JPanel entireWindowPanel;
    private JTextArea conversationTextArea;
    private JButton sendButton;
    private JTextField sendTextField;

    private final User localUser;
    private final User remoteUser;
    private final ConversationController convController;

    private boolean defaultText;

    private void appendMessage(String from, Message message) {
        conversationTextArea.append("[" + message.getTime() + "] <" + from + "> " + message.getContent() + "\n");
    }

    private void sendMessage() {
        String content = sendTextField.getText();

        //TODO: Never trust user input (more precisely than empty messages?)
        if (content.length() > 0 && !defaultText) {
            Message message = new Message(content);
            appendMessage(localUser.getNickname(), message);
            sendTextField.setText("");

            Runnable run = () -> ConversationWindow.this.convController.sendMessage(message);
            new Thread(run).start();
        }
    }

    public ConversationWindow(User localUser, User remoteUser) {
        this.localUser = localUser;
        this.remoteUser = remoteUser;
        this.convController = new ConversationController(localUser, remoteUser);

        JFrame frame = new JFrame(remoteUser.getNickname());

        sendTextField.setText("Type a message...");
        defaultText = true;

        sendButton.setText("Send");
        sendButton.setMnemonic(KeyEvent.VK_ENTER);

        frame.setContentPane(this.entireWindowPanel);

        sendButton.addActionListener(e -> sendMessage());

        sendTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (defaultText && sendTextField.getText().equals(SEND_TEXT_FIELD_PLACEHOLDER)) {
                    sendTextField.setText("");
                    defaultText = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (!defaultText && sendTextField.getText().isEmpty()) {
                    sendTextField.setText(SEND_TEXT_FIELD_PLACEHOLDER);
                    defaultText = true;
                }
            }
        });
        sendTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void receiveMessage(Message message) {
        appendMessage(remoteUser.getNickname(), message);
    }
}
