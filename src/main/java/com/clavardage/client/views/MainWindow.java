package com.clavardage.client.views;

import com.clavardage.client.managers.MessagesManager;
import com.clavardage.client.managers.UsersManager;
import com.clavardage.core.models.User;
import com.clavardage.core.observers.Listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class MainWindow extends JFrame implements Listener {

    private static boolean isInstantiated = false;
    public static MainWindow INSTANCE = null;

    public static void instantiate() {
        if (isInstantiated) {
            throw new IllegalStateException("MainWindow already instantiated");
        } else {
            MainWindow.INSTANCE = new MainWindow();
            MainWindow.isInstantiated = true;
        }
    }

    public final TabbedConversations conversations;

    private final JList<User> connectedUsersList = new JList<>();
    private final DefaultListModel<User> listModel = new DefaultListModel<>();

    private MainWindow() {
        super("Clavardage");
        this.connectedUsersList.setModel(this.listModel);

        UsersManager usersManager = UsersManager.getInstance();
        this.listModel.addAll(usersManager.getConnectedUsers());
        usersManager.addListener(this);

        this.connectedUsersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.connectedUsersList.setLayoutOrientation(JList.VERTICAL);
        this.connectedUsersList.setVisibleRowCount(10);

        this.connectedUsersList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() >= 2) {
                    User remoteUser = connectedUsersList.getSelectedValue();
                    if (remoteUser != null) {
                        displayConversation(remoteUser);
                    }
                }
            }
        });

        JScrollPane listScroll = new JScrollPane(this.connectedUsersList);
        listScroll.setPreferredSize(new Dimension(250, 80));

        JLabel listLabel = new JLabel("<html><body><b>Connected users:</b></body></html>");

        JPanel listTitle = new JPanel();
        listTitle.add(listLabel);
        listTitle.setMaximumSize(listTitle.getPreferredSize());

        JLabel statusLabel = new JLabel("<html><body><b>Status:</b></body></html>");
        JPanel statusTitle = new JPanel();
        statusTitle.add(statusLabel);
        statusTitle.setMaximumSize(statusTitle.getPreferredSize());

        String[] statuses = {"Online", "Away", "Invisible"};
        JComboBox<String> statusList = new JComboBox<>(statuses);
        statusList.addActionListener(e -> {
            JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
            String selectedString = (String) comboBox.getSelectedItem();

            HashMap<String, User.UserStatus> statusMap = new HashMap<>();
            statusMap.put("Online", User.UserStatus.CONNECTED);
            statusMap.put("Away", User.UserStatus.IDLE);
            statusMap.put("Invisible", User.UserStatus.DISCONNECTED);

            User.UserStatus selectedStatus = statusMap.get(selectedString);

            User.UserStatus currentStatus = User.localUser.getStatus();

            if (selectedStatus.equals(currentStatus)
                    || currentStatus.equals(User.UserStatus.UNKNOWN)) {
                return;
            }

            new Thread(() -> {
                if (selectedStatus.equals(User.UserStatus.DISCONNECTED)) {
                    User.disconnectLocalUser();
                } else if (currentStatus.equals(User.UserStatus.DISCONNECTED)) {
                    // selectedStatus can only be CONNECTED or IDLE
                    User.connectLocalUser();
                }
                User.updateLocalUserStatus(selectedStatus);
            }).start();
        });
        statusList.setMaximumSize(new Dimension(200, 30));
        statusList.setPreferredSize(new Dimension(200, 30));
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.LINE_AXIS));
        statusPanel.add(statusTitle);
        statusPanel.add(statusList);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
        leftPanel.add(listTitle);
        leftPanel.add(listScroll);
        leftPanel.add(statusPanel);

        JPanel subPanel = new JPanel();
        subPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        this.conversations = new TabbedConversations();
        subPanel.add(this.conversations);

        JPanel entirePanel = new JPanel();
        entirePanel.setLayout(new BoxLayout(entirePanel, BoxLayout.LINE_AXIS));
        entirePanel.add(leftPanel);
        entirePanel.add(subPanel);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stop();
                System.exit(0);
            }
        });

        this.setContentPane(entirePanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void displayConversation(User remoteUser) {
        ConversationPanel conversationPanel = this.conversations.getConversation(remoteUser);
        if (conversationPanel == null) {
            this.conversations.addConversation(remoteUser);
        }
        this.conversations.focusConversation(remoteUser);
    }

    public void refreshConnectedUsersList() {
        SwingUtilities.invokeLater(() -> {
            this.listModel.removeAllElements();
            this.listModel.addAll(UsersManager.getInstance().getConnectedUsers());
        });
    }

    public void stop() {
        MessagesManager.getInstance().stop();
        UsersManager.getInstance().stop();
        User.disconnectLocalUser();
        User.updateLocalUserStatus(User.UserStatus.DISCONNECTED);
    }

    @Override
    public void handle(Object... args) {
        this.refreshConnectedUsersList();
    }
}
