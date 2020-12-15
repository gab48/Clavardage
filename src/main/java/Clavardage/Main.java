package Clavardage;

import Clavardage.Managers.MessagesManager;
import Clavardage.Managers.UsersManager;
import Clavardage.Models.User;
import Clavardage.Network.Models.Address;
import Clavardage.Views.MainWindow;
import Clavardage.Views.NicknameSelectionWindow;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        String nickname = NicknameSelectionWindow.askNickname();

        User.instantiateLocalUser(nickname);

        MainWindow.instantiate();

        MessagesManager mm = MessagesManager.getInstance();
        UsersManager um = UsersManager.getInstance();

        um.addConnectedUser(new User("BobDuPacket", Address.getMyIP()));
    }
}
