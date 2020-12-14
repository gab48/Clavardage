package Clavardage.Views;

import Clavardage.Managers.UsersManager;
import Clavardage.Models.User;
import Clavardage.Network.Models.Address;
import Clavardage.Observers.Listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class MainWindow extends JFrame implements Listener {

    private static volatile MainWindow INSTANCE = null;

    public static void instantiate() {
        if (MainWindow.INSTANCE == null) {
            synchronized (MainWindow.class) {
                if (MainWindow.INSTANCE == null) {
                    try {
                        SwingUtilities.invokeAndWait(() -> MainWindow.INSTANCE = new MainWindow());
                    } catch (InvocationTargetException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static MainWindow getInstance() {
        return MainWindow.INSTANCE;
    }

    public final TabbedConversations conversations;

    private final UsersManager usersManager = UsersManager.getInstance();

    private final JList connectedUsersList = new JList();

    private MainWindow() {
        super("Clavardage");

        DefaultListModel model = new DefaultListModel();
        this.connectedUsersList.setModel(model);
        model.addAll(usersManager.getConnectedUsers());
        this.usersManager.addListener(this);

        this.connectedUsersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.connectedUsersList.setLayoutOrientation(JList.VERTICAL);
        this.connectedUsersList.setVisibleRowCount(10);

        JScrollPane listScroll = new JScrollPane(this.connectedUsersList);
        listScroll.setPreferredSize(new Dimension(250, 80));

        this.connectedUsersList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList list = (JList) e.getSource();
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() >= 2) {
                    User remoteUser = (User) list.getSelectedValue();
                    displayConversation(remoteUser);
                }
            }
        });

        JPanel subPanel = new JPanel();
        subPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        this.conversations = new TabbedConversations();
        subPanel.add(this.conversations);

        JPanel entirePanel = new JPanel();
        entirePanel.setLayout(new BoxLayout(entirePanel, BoxLayout.LINE_AXIS));

        entirePanel.add(listScroll);
        entirePanel.add(subPanel);

        this.setContentPane(entirePanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void displayConversation(User remoteUser) {
        ConversationPanel conversation = this.conversations.getConversation(remoteUser);
        if (conversation == null) {
            this.conversations.addConversation(remoteUser);
        }
        this.conversations.focusConversation(remoteUser);
    }

    private void addConversation(User remoteUser) {
        SwingUtilities.invokeLater(() -> {
            conversations.addConversation(remoteUser);
            pack();
        });
    }

    private void addConnectedUser(User u) {
        DefaultListModel model = (DefaultListModel) this.connectedUsersList.getModel();
        SwingUtilities.invokeLater(() -> {
            model.addElement(u);
        });
    }

    private void removeConnectedUser(User u) {
        DefaultListModel model = (DefaultListModel) this.connectedUsersList.getModel();
        SwingUtilities.invokeLater(() -> {
            model.removeElement(u);
        });
    }

    @Override
    public void handle(Object... args) {
        String operation = (String) args[0];
        User user = (User) args[1];

        if (operation.equals("add")) {
            this.addConnectedUser(user);
        } else if (operation.equals("remove")) {
            this.removeConnectedUser(user);
        } else {
            System.err.println("MainWindow.handle(): unknown operation");
        }
    }
}
