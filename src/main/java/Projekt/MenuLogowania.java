package Projekt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class MenuLogowania {

    private ImageIcon image;

    public void wyswietl() {
        JFrame frame = new JFrame("Logowanie do wypożyczalni");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        try {
            image = new ImageIcon(ImageIO.read(new File("src/main/java/img/cinema.jpg")));
        } catch (IOException mue) {
            JOptionPane.showMessageDialog(null, "Błąd wczytywania tła");
        }

        JLabel imageLabel = new JLabel(image);

        JPanel panelPodst = new JPanel();
        panelPodst.setOpaque(false);
        panelPodst.setLayout(new BorderLayout(5, 5));

        JPanel panelGora = new JPanel();
        panelGora.setOpaque(false);
        panelGora.setLayout(new GridLayout(2, 2, 5, 5));

        JLabel uzytkownikLabel = new JLabel("Login: ", JLabel.CENTER);
        uzytkownikLabel.setForeground(Color.WHITE);
        JTextField userField = new JTextField(12);
        JLabel passLabel = new JLabel("Hasło: ", JLabel.CENTER);
        passLabel.setForeground(Color.WHITE);
        JPasswordField passField = new JPasswordField(12);

        panelGora.add(uzytkownikLabel);
        panelGora.add(userField);
        panelGora.add(passLabel);
        panelGora.add(passField);

        JPanel panelDol = new JPanel(new GridLayout(1 ,2, 10, 2));
        panelDol.setOpaque(false);
        JButton loginButton = new JButton("ZALOGUJ");
        JButton registerButton = new JButton("REJESTRACJA");
        panelDol.add(registerButton);
        panelDol.add(loginButton);

        panelPodst.add(panelGora, BorderLayout.NORTH);
        panelPodst.add(panelDol, BorderLayout.SOUTH);

        imageLabel.setLayout(new GridBagLayout());
        imageLabel.add(panelPodst);

        contentPane.add(imageLabel, BorderLayout.CENTER);

        loginButton.addActionListener(l -> {
            DataBaseConnection polacz = new DataBaseConnection();
            boolean logowanie = false;
            if (!userField.getText().isBlank() && (passField.getPassword().length >= 1)) {
                try {
                    logowanie = polacz.logowanie(userField.getText(), String.valueOf(passField.getPassword()));
                } catch (ClassNotFoundException | SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

            if (logowanie) {
                JOptionPane.showMessageDialog(null, "Pomyślnie zalogowano się");
                if (userField.getText().equals("admin")) {
                    new PanelGlownyAdministratora("admin").wyswietl();
                } else {
                    try {
                        PanelGlowny p = new PanelGlowny(userField.getText());
                        p.wyswietl();
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                frame.dispose();
            } else JOptionPane.showMessageDialog(null, "Nie udało się zalogować");
        });

        registerButton.addActionListener(l -> {
            Rejestracja rej = new Rejestracja();
            rej.wyswietl();
            frame.dispose();
        });

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
