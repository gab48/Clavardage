package Clavardage.Views;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;

public class NicknameSelectionWindow extends JFrame {

    private static volatile NicknameSelectionWindow INSTANCE = null;

    private final JTextField nicknameField;
    private String nickname;

    private NicknameSelectionWindow() {
        super("Clavardage");

        this.nickname = null;

        JPanel innerPanel = new JPanel();
        innerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.PAGE_AXIS));
        innerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));

        JPanel nicknamePanel = new JPanel();
        nicknamePanel.setLayout(new BoxLayout(nicknamePanel, BoxLayout.LINE_AXIS));

        JLabel nicknameLabel = new JLabel("Nickname: ");
        nicknamePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        nicknamePanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.nicknameField = new JTextField();
        this.nicknameField.setPreferredSize(new Dimension(100, 20));
        this.nicknameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submitNickname();
                }
            }
        });

        nicknamePanel.add(nicknameLabel);
        nicknamePanel.add(this.nicknameField);
        innerPanel.add(nicknamePanel);

        JPanel buttonPanel = new JPanel();
        JButton connectButton = new JButton("Connect");
        connectButton.setPreferredSize(new Dimension(200, 25));
        connectButton.addActionListener(e -> {
            submitNickname();
        });

        buttonPanel.add(connectButton);
        innerPanel.add(buttonPanel);

        JPanel borderPanel = new JPanel();
        TitledBorder border = BorderFactory.createTitledBorder("Welcome to Clavardage!");
        border.setTitleJustification(TitledBorder.CENTER);
        borderPanel.setBorder(border);
        borderPanel.add(innerPanel);

        JPanel outerPanel = new JPanel();
        outerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        outerPanel.add(borderPanel);


        this.setContentPane(outerPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void submitNickname() {
        synchronized (this) {
            this.nickname = this.nicknameField.getText();
            this.dispose();
            this.notifyAll();
        }
    }

    private String getNickname() {
        while (this.nickname == null) {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return this.nickname;
    }

    public static String askNickname() {
        if (NicknameSelectionWindow.INSTANCE == null) {
            try {
                SwingUtilities.invokeAndWait(() -> NicknameSelectionWindow.INSTANCE = new NicknameSelectionWindow());
            } catch (InvocationTargetException | InterruptedException e) {
                e.printStackTrace();
            }

            return NicknameSelectionWindow.INSTANCE.getNickname();
        } else {
            throw new IllegalStateException("Nickname should not be asked more than once.");
        }
    }

    public static void main(String[] args) {
        System.out.println(NicknameSelectionWindow.askNickname());
    }

}
