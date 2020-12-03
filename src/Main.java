import Models.User;
import Views.MainWindow;
import Views.WelcomeWindow;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class Main {
    private static MainWindow myWindow;

    public static void main(String[] args) throws InterruptedException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Keys are: "nickname", "localPort", "remoteAddress" and "remotePort"
        Map<String, String> information = WelcomeWindow.askInformation();

        User localUser = new User(information.get("nickname"));

        try {
            SwingUtilities.invokeAndWait(() -> Main.myWindow = new MainWindow(localUser));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        User remoteUser = new User("Bob");

        Main.myWindow.addConversation(remoteUser);
    }
}
