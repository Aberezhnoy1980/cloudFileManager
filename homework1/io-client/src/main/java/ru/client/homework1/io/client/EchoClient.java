package ru.client.homework1.io.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class EchoClient extends JFrame {
    JTextField textField;
    JTextArea textArea;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public EchoClient() {
        openConnection();
        prepareGUI();
    }

    public void openConnection() {
        try {
            socket = new Socket("Localhost", 9001);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    while (true) {
                        String msgFromServer = in.readUTF();
                        if (msgFromServer.equalsIgnoreCase("/end")) {
                            break;
                        }
                        textArea.append(msgFromServer + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            throw new RuntimeException("Unable to connect to the server at [ localhost:port 9000 ]");
        }
    }

    public void closeConnection() {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        if (!textField.getText().trim().isEmpty()) {
            try {
                out.writeUTF(textField.getText());
                textField.setText("");
                textField.grabFocus();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Send message error");
            }
        }
    }

    public void prepareGUI() {
        setBounds(600, 300, 500, 300);
        setTitle("Echo server");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        textField = new JTextField();
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3));
        JButton sendTestMessage = new JButton("Send test message");
//        JButton stopButton = new JButton("Stop and leave");
        bottomPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(sendTestMessage, BorderLayout.EAST);
//        bottomPanel.add(stopButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

//        stopButton.addActionListener(e -> closeConnection());
        textField.addActionListener(e -> sendMessage());
        sendTestMessage.addActionListener(e -> sendMessage());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    out.writeUTF("/end");
                    closeConnection();
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        });

        this.setVisible(true);
    }
}
