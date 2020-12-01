import Models.User;
import Views.ConversationWindow;
import Views.WelcomeWindow;

import javax.swing.*;
import java.util.Map;

public class Main {

    private static final User localUser = new User();

    public static void main(String[] args) throws InterruptedException {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Map<String, String> information = WelcomeWindow.askInformation();

        localUser.setNickname(information.get("nickname"));

        User remoteUser = new User();
        remoteUser.setNickname(information.get("remoteAddress"));

        new ConversationWindow(localUser, remoteUser);
    }
}
