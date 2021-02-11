package com.clavardage.client.views;

import com.clavardage.client.managers.ConversationsManager;
import com.clavardage.core.models.Conversation;
import com.clavardage.core.models.History;
import com.clavardage.core.models.Message;
import com.clavardage.core.models.User;
import com.clavardage.core.network.controllers.ConversationController;
import com.clavardage.core.network.models.Address;
import com.clavardage.core.network.models.FilePacket;
import com.clavardage.core.network.sockets.FileSocket;
import com.clavardage.core.utils.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

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
        JButton sendButton = new JButton("Send");
        JButton fileButton = new JButton("File");

        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new BoxLayout(sendPanel, BoxLayout.LINE_AXIS));
        sendPanel.add(this.sendTextField);
        sendPanel.add(sendButton);
        sendPanel.add(fileButton);

        this.add(sendPanel);

        sendButton.addActionListener(e -> sendMessage());
        fileButton.addActionListener(e -> chooseFile());

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

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            FileSocket fs = new FileSocket();
            Address remoteFileBuffer = new Address(this.remoteUser.getAddress().getIp(), Short.parseShort(Config.get("NETWORK_TCP_FILE_PORT")));
            FilePacket filePacket = new FilePacket(file, remoteFileBuffer);
            filePacket.setSrc(User.localUser.getAddress());
            fs.connect(remoteFileBuffer);
            fs.send(filePacket);
            fs.close();

            System.out.println("Sended file : " + file.getName() + ".");
        } else {
            System.out.println("Open command cancelled by user.");
        }
    }

    private void loadHistory() {
        ConversationsManager conversationsManager = ConversationsManager.getInstance();
        Conversation conversation = conversationsManager.getConversation(this.remoteUser.getAddress());
        History history = conversation.getHistory();
        for (Message message : history.getMessagesHistory()) {
            //System.out.printf("sender: %s\n", message.getSender());
            //System.out.printf("local:  %s\n", User.localUser.getAddress().toString());
            if (message.getSender().equals(User.localUser.getAddress().toString())) {
                appendMessage(User.localUser, message);
            } else {
                appendMessage(this.remoteUser, message);
            }
        }
    }

    private void appendMessage(User sender, Message message) {
        conversationTextArea.append("[" + message.getTime() + "] <" + sender.getNickname() + "> "
                + message.getContent() + "\n");
    }

    public void receiveMessage(Message message) {
        //TODO: Take care of the type message.getType()

        SwingUtilities.invokeLater(() -> appendMessage(remoteUser, message));
    }
}
