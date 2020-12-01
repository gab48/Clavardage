package Views;

import Models.Message;
import Models.User;

import javax.swing.*;
import java.awt.event.*;

public class ConversationWindow {
    private static final String MESSAGE_TEXT_FIELD_PLACEHOLDER = "Type a message...";

    private JPanel entireWindowPanel;
    private JTextArea conversationTextArea;
    private JButton sendButton;
    private JTextField sendTextField;

    private final User localUser;
    private final User remoteUser;

    private void appendMessage(String from, Message message) {

        conversationTextArea.append("[" + message.getTime() + "] <" + from + "> " + message.getContent() + "\n");
    }

    private void sendMessage() {
        Message message = new Message(sendTextField.getText());

        //TODO: Never trust user input

        appendMessage(localUser.getNickname(), message);

        //TODO: Really send the message (notify a listener?)
    }

    public ConversationWindow(User localUser, User remoteUser) {
        this.localUser = localUser;
        this.remoteUser = remoteUser;
        JFrame frame = new JFrame(remoteUser.getNickname());

        sendTextField.setText("Type a message...");
        sendButton.setText("Send");
        sendButton.setMnemonic(KeyEvent.VK_ENTER);

        frame.setContentPane(this.entireWindowPanel);

        sendButton.addActionListener(e -> sendMessage());

        sendTextField.addFocusListener(new FocusAdapter() {
            //TODO: Prevent default text from being sent,
            //  while also allowing the user to type and send it
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (sendTextField.getText().equals(MESSAGE_TEXT_FIELD_PLACEHOLDER)) {
                    sendTextField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (sendTextField.getText().isEmpty()) {
                    sendTextField.setText(MESSAGE_TEXT_FIELD_PLACEHOLDER);
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

    public void receiveMessage(Message message)
    {
        appendMessage(remoteUser.getNickname(), message);
    }
}
