package com.clavardage.views;

import javax.swing.*;

public class AlertWindow {
    private static final String TITLE = "Clavardage";

    public static void displayInformation(String message) {
        displayDialog(message, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void displayWarning(String message) {
        displayDialog(message, JOptionPane.WARNING_MESSAGE);
    }

    public static void displayError(String message) {
        displayDialog(message, JOptionPane.ERROR_MESSAGE);
    }

    private static void displayDialog(String message, int messageType) {
        JOptionPane.showMessageDialog(MainWindow.INSTANCE, message, AlertWindow.TITLE, messageType);
    }
}
