package com.clavardage;

import com.clavardage.managers.ConversationsManager;
import com.clavardage.managers.MessagesManager;
import com.clavardage.managers.UsersManager;
import com.clavardage.models.User;
import com.clavardage.views.AlertWindow;
import com.clavardage.views.MainWindow;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        boolean validNickname = false;
        String message;
        String nickname = null;
        final String title = "Clavardage";

        while (!validNickname) {
            message = "Welcome! Please enter your nickname:";
            nickname = JOptionPane.showInputDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);

            if (nickname == null) {
                // Window closed, or cancel button pressed
                System.exit(0);
            } else if (nickname.length() == 0 || nickname.contains(" ")) {
                AlertWindow.displayError("Your nickname cannot be empty nor contain any spaces");
            } else {
                validNickname = true;
            }
        }

        User.instantiateLocalUser(nickname);

        MainWindow.instantiate();

        MessagesManager         mm = MessagesManager.getInstance();
        UsersManager            um = UsersManager.getInstance();
        ConversationsManager    cm = ConversationsManager.getInstance();
    }
}
