import Models.User;
import Views.ConversationWindow;
import Views.WelcomeWindow;

import javax.swing.*;
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

        User localUser = new User(information.get("nickname"));

        User remoteUser = new User("Bob");

        new ConversationWindow(localUser, remoteUser);
    }
}
