package com.clavardage.client.views;

import com.clavardage.client.managers.ConversationsManager;
import com.clavardage.core.models.Conversation;
import com.clavardage.core.models.History;
import com.clavardage.core.models.Message;
import com.clavardage.core.models.User;
import com.clavardage.core.network.controllers.ConversationController;
import com.clavardage.core.utils.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

public class ConversationPanel extends JPanel {
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
        this.loadHistory();
        this.add(this.conversationTextArea);

        this.sendTextField = new JTextField("Type a message...");
        this.defaultText = true;
        JButton sendTextButton = new JButton("Send");
        JButton sendFileButton = new JButton("\uD83D\uDCCE"); // Unicode clip
        JButton filesFolderButton = new JButton("Files");

        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new BoxLayout(sendPanel, BoxLayout.LINE_AXIS));
        sendPanel.add(this.sendTextField);
        sendPanel.add(sendTextButton);
        sendPanel.add(sendFileButton);
        sendPanel.add(filesFolderButton);

        this.add(sendPanel);

        sendTextButton.addActionListener(e -> sendMessage());
        sendFileButton.addActionListener(e -> sendFile());
        filesFolderButton.addActionListener(e -> openFilesFolder());

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
            appendMessage(User.localUser, message);
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

    private void sendFile() {
        UIManager.put("FileChooser.openButtonText", "Send");
        UIManager.put("FileChooser.openButtonToolTipText", "Send selected file");
        UIManager.put("FileChooser.cancelButtonToolTipText", "Abort file sending");

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Send file");
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            conversationController.sendFile(file);

            Message message = new Message(
                    User.localUser.getAddress().toString(),
                    file.getName(),
                    new Timestamp(new Date().getTime()),
                    Message.MessageType.FILE);

            this.appendMessage(User.localUser, message);
        } else {
            System.out.println("Open command cancelled by user.");
        }
    }

    private void openFilesFolder() {
        try {
            Desktop.getDesktop().open(new File(Config.getString("FILE_DIRECTORY")));
        } catch (IOException e) {
            AlertWindow.displayError("The FILE_DIRECTORY parameter from the client.config file " +
                "does not point to an accessible folder.");
            System.exit(1);
        }
    }

    private void loadHistory() {
        ConversationsManager conversationsManager = ConversationsManager.getInstance();
        Conversation conversation = conversationsManager.getConversation(this.remoteUser.getAddress());
        History history = conversation.getHistory();
        if (history != null) {
            for (Message message : history.getMessagesHistory()) {
                if (message.getSender().equals(User.localUser.getAddress().toString())) {
                    appendMessage(User.localUser, message);
                } else {
                    appendMessage(this.remoteUser, message);
                }
            }
        }
    }

    private void appendMessage(User sender, Message message) {
        if (message.getType() == Message.MessageType.TEXT) {
            this.conversationTextArea.append("[" + message.getTime() + "] <" + sender.getNickname() + "> "
                + message.getContent() + "\n");
        } else if (message.getType() == Message.MessageType.FILE) {
            this.conversationTextArea.append("[" + message.getTime() + "] ");
            if (sender.equals(this.remoteUser)) {
                this.conversationTextArea.append(sender.getNickname() + " sent the file \"" + message.getContent() + "\".");
            } else {
                this.conversationTextArea.append("You sent the file \"" + message.getContent() + "\".");
            }
            this.conversationTextArea.append("\n");
        }
    }

    public void receiveMessage(Message message) {
        SwingUtilities.invokeLater(() -> appendMessage(remoteUser, message));
    }
}
