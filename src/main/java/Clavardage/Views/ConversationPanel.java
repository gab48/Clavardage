package Clavardage.Views;

import Clavardage.Models.Message;
import Clavardage.Models.User;
import Clavardage.Network.Controllers.ConversationController;
import Clavardage.Network.Models.Address;
import Clavardage.Network.Models.FilePacket;
import Clavardage.Network.SocketProtocols.FileSocket;

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

            //TODO: Send the file
            FileSocket fs = new FileSocket();
            Address remoteFileBuffer = new Address(this.remoteUser.getAddress().getIp(), (short) 1922);
            fs.connect(remoteFileBuffer);
            fs.send(new FilePacket(file, remoteFileBuffer));
            fs.close();

            System.out.println("Hypothetically sending: " + file.getName() + ".");
        } else {
            System.out.println("Open command cancelled by user.");
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
