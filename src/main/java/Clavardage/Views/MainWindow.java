package Clavardage.Views;

import Clavardage.Managers.UsersManager;
import Clavardage.Models.User;
import Clavardage.Observers.Listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
                    displayConversation(remoteUser);
                }
            }
        });

        JScrollPane listScroll = new JScrollPane(this.connectedUsersList);
        listScroll.setPreferredSize(new Dimension(250, 80));

        JLabel listLabel = new JLabel("<html><body><b>Connected users:</b></body></html>");

        JPanel listTitle = new JPanel();
        listTitle.add(listLabel);
        listTitle.setMaximumSize(listTitle.getPreferredSize());

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));
        listPanel.add(listTitle);
        listPanel.add(listScroll);

        JPanel subPanel = new JPanel();
        subPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        this.conversations = new TabbedConversations();
        subPanel.add(this.conversations);

        JPanel entirePanel = new JPanel();
        entirePanel.setLayout(new BoxLayout(entirePanel, BoxLayout.LINE_AXIS));
        entirePanel.add(listPanel);
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

    private void addConnectedUser(User u) {
        if (!this.listModel.contains(u)) {
            SwingUtilities.invokeLater(() -> this.listModel.addElement(u));
        }
    }

    private void stop() {
        //TODO: Stop things that need to be stopped
    }

    private void removeConnectedUser(User u) {
        SwingUtilities.invokeLater(() -> this.listModel.removeElement(u));
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
