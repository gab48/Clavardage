package Clavardage;

import Clavardage.Managers.MessagesManager;
import Clavardage.Managers.UsersManager;
import Clavardage.Models.User;
import Clavardage.Network.Listeners.MsgListenerPool;
import Clavardage.Network.Models.Address;
import Clavardage.Views.ConversationWindow;
import Clavardage.Views.WelcomeWindow;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Keys are: "nickname", "localPort", "remoteAddress" and "remotePort"
        Map<String, String> information = WelcomeWindow.askInformation();

        User.current = new User(information.get("nickname"));
        System.out.println(User.current);

        User remoteUser = null;
        try {
            Address remoteAddr = new Address(InetAddress.getByName(information.get("remoteAddress")));
            remoteUser = new User("Bob", remoteAddr);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Env.setCw(new ConversationWindow(remoteUser));
        MessagesManager mm = MessagesManager.getInstance();
        UsersManager um = UsersManager.getInstance();
    }
}
