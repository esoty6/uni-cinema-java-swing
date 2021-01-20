package Projekt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class PanelGlownyAdministratora {

    public static String login = null;
    private ImageIcon image, imageLbl;

    public PanelGlownyAdministratora() {}

    public PanelGlownyAdministratora(Object login){
        this.login = (String) login;
    }

    public void wyswietl() {
        JFrame frame = new JFrame("Panel administratora");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        try {
            image = new ImageIcon(ImageIO.read(new File("src/main/java/img/bg.jpg")));
            imageLbl = new ImageIcon(ImageIO.read(new File("src/main/java/img/bgLabel.jpg")));
        } catch(IOException mue) {
            JOptionPane.showMessageDialog(null, "Błąd wczytywania tła");
        }
        JLabel imageLabel = new JLabel(image);
        JPanel panelPodst = new JPanel();
        panelPodst.setOpaque(false);
        panelPodst.setLayout(new BorderLayout(5, 5));

        JPanel panelGora = new JPanel();
        panelGora.setOpaque(false);
        panelGora.setLayout(new GridLayout(2, 2, 5, 5));

        JPanel panelDol = new JPanel();
        panelDol.setLayout(new GridLayout(8,1, 10, 15));
        panelDol.setOpaque(false);

        JLabel uzytkownikLabel = new JLabel("Zalogowano jako administrator", JLabel.CENTER);
        uzytkownikLabel.setForeground(Color.WHITE);
        uzytkownikLabel.setFont(new Font("Arial, Verdana", Font.BOLD, 14));
        uzytkownikLabel.setIcon(imageLbl);
        uzytkownikLabel.setHorizontalTextPosition(JLabel.CENTER);
        uzytkownikLabel.setVerticalTextPosition(JLabel.CENTER);

        JButton dodajFilmButton = new JButton("Dodaj nowy film");
        dodajFilmButton.setFocusable(false);

        JButton usunFilmButton = new JButton("Usun istniejący film");
        usunFilmButton.setFocusable(false);

        JButton listaPracownikowButton = new JButton("Zarządzanie pracownikami");
        listaPracownikowButton.setFocusable(false);

        JButton wylogujButton = new JButton("Wyloguj");
        wylogujButton.setFocusable(false);


        panelGora.add(uzytkownikLabel);
        panelDol.add(dodajFilmButton);
        panelDol.add(usunFilmButton);
        panelDol.add(listaPracownikowButton);
        panelDol.add(wylogujButton);

        panelPodst.add(panelGora, BorderLayout.CENTER);
        panelPodst.add(panelDol, BorderLayout.PAGE_END);

        imageLabel.setLayout(new GridBagLayout());
        imageLabel.add(panelPodst);

        contentPane.add(imageLabel, BorderLayout.CENTER);

        dodajFilmButton.addActionListener(l -> {
            DodajFilm df = new DodajFilm();
            df.wyswietl();
            frame.dispose();
        });

        usunFilmButton.addActionListener(l -> {
            try {
                new UsunFilm().listaFilmow();
                frame.dispose();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        listaPracownikowButton.addActionListener(l -> {
            try {
                new ListaPracownikow().listaPracownikow();
                frame.dispose();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        wylogujButton.addActionListener(e -> {
            Wylogowanie wylogowanie = new Wylogowanie();
            wylogowanie.wyswietl();
            frame.dispose();
        });

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
