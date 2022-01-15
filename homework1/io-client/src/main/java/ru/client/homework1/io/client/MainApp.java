package ru.client.homework1.io.client;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EchoClient());
    }
}

