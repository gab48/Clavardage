package Clavardage.Views;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.Map;

public class WelcomeWindow {
    private final JFrame frame = new JFrame("Clavardage");
    private JPanel entireWindowPanel;
    private JButton submitButton;
    private JTextField nicknameField;
    private JTextField localPortField;
    private JTextField remoteAddressField;
    private JTextField remotePortField;
    private JLabel localPortLabel;
    private JLabel remoteAddressLabel;
    private JLabel remotePortLabel;
    private JLabel nicknameLabel;

    private boolean informationReady = false;
    private Map<String, String> information;

    private void prepareInformation() {
        synchronized (this) {
            //TODO: Never trust user input
            information = new LinkedHashMap<>();
            information.put("nickname", nicknameField.getText());
            information.put("localPort", localPortField.getText());
            information.put("remoteAddress", remoteAddressField.getText());
            information.put("remotePort", remotePortField.getText());
            frame.dispose();
            informationReady = true;
            this.notifyAll();
        }
    }

    private WelcomeWindow() {
        nicknameLabel.setText("Your nickname :");
        nicknameLabel.setLabelFor(nicknameField);

        localPortLabel.setText("Local TCP port ");
        localPortLabel.setLabelFor(localPortField);

        remoteAddressLabel.setText("Remote IP address :");
        remoteAddressLabel.setLabelFor(remoteAddressField);

        remotePortLabel.setText("Remote TCP port :");
        remotePortLabel.setLabelFor(remotePortField);

        submitButton.setText("Open conversation");
        submitButton.setMnemonic(KeyEvent.VK_ENTER);

        submitButton.addActionListener(e -> prepareInformation());

        KeyAdapter enterKeyPreparesInformation = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    prepareInformation();
                }
            }
        };

        nicknameField.addKeyListener(enterKeyPreparesInformation);
        localPortField.addKeyListener(enterKeyPreparesInformation);
        remoteAddressField.addKeyListener(enterKeyPreparesInformation);
        remotePortField.addKeyListener(enterKeyPreparesInformation);
    }

    private Map<String, String> getInformation() throws InterruptedException {
        while (!informationReady) {
            synchronized (this) {
                this.wait();
            }
        }
        return information;
    }

    public static Map<String, String> askInformation() throws InterruptedException {
        WelcomeWindow welcomeWindow = new WelcomeWindow();


        SwingUtilities.invokeLater(() -> {
            welcomeWindow.frame.setContentPane(welcomeWindow.entireWindowPanel);
            welcomeWindow.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            welcomeWindow.frame.pack();
            welcomeWindow.frame.setLocationRelativeTo(null);
            welcomeWindow.frame.setVisible(true);
        });

        return welcomeWindow.getInformation();
    }

}
