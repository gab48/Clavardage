package Clavardage;

import Clavardage.Managers.MessagesManager;
import Clavardage.Managers.UsersManager;
import Clavardage.Models.LocalUser;
import Clavardage.Models.User;
import Clavardage.Network.Models.Address;
import Clavardage.Views.ConversationPanel;
import Clavardage.Views.MainWindow;
import Clavardage.Views.WelcomeWindow;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class Main {

    //TODO: Don't make this a global variable
    public static MainWindow mainWindow;

    //TODO: Allow multiple conversations
    public static ConversationPanel getConversation() { return Main.mainWindow.getConversation(); }

    public static void main(String[] args) throws InterruptedException {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Keys are: "nickname", "localPort", "remoteAddress" and "remotePort"
        Map<String, String> information = WelcomeWindow.askInformation();

        LocalUser.instanciate(information.get("nickname"));

        User remoteUser = null;
        try {
            Address remoteAddr = new Address(InetAddress.getByName(information.get("remoteAddress")));
            remoteUser = new User("Bob", remoteAddr);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            SwingUtilities.invokeAndWait(() -> Main.mainWindow = new MainWindow());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        Main.mainWindow.addConversation(remoteUser);
        MessagesManager mm = MessagesManager.getInstance();
        UsersManager um = UsersManager.getInstance();
    }
}
