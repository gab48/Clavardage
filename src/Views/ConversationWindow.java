package Views;

import javax.swing.*;
import java.awt.event.*;

public class ConversationWindow {
    private static final String MESSAGE_TEXTFIELD_PLACEHOLDER = "Type a message...";

    private JPanel myPanel;
    private JTextArea conversationTextArea;
    private JButton sendButton;
    private JTextField sendTextField;
    private JButton envoyerButton;

    private void sendMessage() {
        String message = sendTextField.getText();

        //TODO: Never trust user input

        conversationTextArea.append(message + "\n");
        System.out.println("User sent " + sendTextField.getText());

        sendTextField.setText("");
    }

    public ConversationWindow() {
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        sendTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (sendTextField.getText().equals(MESSAGE_TEXTFIELD_PLACEHOLDER)) {
                    sendTextField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (sendTextField.getText().isEmpty()) {
                    sendTextField.setText(MESSAGE_TEXTFIELD_PLACEHOLDER);
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
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("Test");
        frame.setContentPane(new ConversationWindow().myPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
