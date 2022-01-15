package ru.client.homework1.io.client.temp;

import javax.swing.*;
import java.awt.*;

public class MainForm extends JFrame {

    public MainForm() throws HeadlessException {
        this.setBounds(600, 300, 500, 300);
        this.setTitle("Echo test time server");
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        JScrollPane jsp = new JScrollPane(textArea);
        this.add(jsp, BorderLayout.CENTER);

        JTextField textField = new JTextField();
        textField.addActionListener(e -> {
                textArea.append(textField.getText() + '\n');
                textField.setText("");});

        JPanel bottomPanel = new JPanel(new GridLayout(1, 3));
        JButton sendTestMessage = new JButton("Send test message");
        JButton stopButton = new JButton("Stop and leave");
        bottomPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(sendTestMessage, BorderLayout.EAST);
        bottomPanel.add(stopButton, BorderLayout.EAST);
        this.add(bottomPanel, BorderLayout.SOUTH);

        stopButton.addActionListener(e -> System.exit(0));
        sendTestMessage.addActionListener(e ->
                System.out.println("sdsd"));

        this.setVisible(true);
    }



}
