package Projekt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class DodajFilm {

    private ImageIcon image;

    public void wyswietl() {
        JFrame frame = new JFrame("Panel dodawania filmu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        try {
            image = new ImageIcon(ImageIO.read(new File("src/main/java/img/form.jpg")));
        } catch(IOException mue) {
            JOptionPane.showMessageDialog(null, "Błąd wczytywania tła");
        }
        JLabel imageLabel = new JLabel(image);

        JPanel panelPodst = new JPanel();
        panelPodst.setOpaque(false);
        panelPodst.setLayout(new BorderLayout(5, 5));

        JPanel panelGora = new JPanel();
        panelGora.setOpaque(false);
        panelGora.setLayout(new GridLayout(8, 2, 5, 5));

        JPanel panelDol = new JPanel();
        panelDol.setOpaque(false);

        JTextField tytulTxt = new JTextField();
        JTextField rokProdukcjiTxt = new JTextField();
        JTextField cenaTxt = new JTextField();
        JTextField opisTxt = new JTextField();
        JTextField rezyserImieTxt = new JTextField();
        JTextField rezyserNazTxt = new JTextField();
        JTextField iloscTxt = new JTextField();

        JLabel tytulLabel = new JLabel(("Tytuł:"), JLabel.CENTER);
        JLabel rokProdukcjiLabel = new JLabel(("Rok produkcji:"), JLabel.CENTER);
        JLabel cenaLabel = new JLabel(("Cena wypożyczenia:"), JLabel.CENTER);
        JLabel opisLabel = new JLabel(("Opis:"), JLabel.CENTER);
        JLabel rezyserImieLabel = new JLabel(("Imię rezysera:"), JLabel.CENTER);
        JLabel rezyserNazLabel = new JLabel(("Nazwisko rezysera:"), JLabel.CENTER);
        JLabel iloscLabel = new JLabel(("Ilość egzemplarzy:"), JLabel.CENTER);

        JButton potwierdzButton = new JButton("Potwierdź");
        JButton anulujButton = new JButton("Anuluj");

        panelGora.add(tytulLabel);
        panelGora.add(tytulTxt);

        panelGora.add(rokProdukcjiLabel);
        panelGora.add(rokProdukcjiTxt);

        panelGora.add(cenaLabel);
        panelGora.add(cenaTxt);

        panelGora.add(opisLabel);
        panelGora.add(opisTxt);

        panelGora.add(rezyserImieLabel);
        panelGora.add(rezyserImieTxt);

        panelGora.add(rezyserNazLabel);
        panelGora.add(rezyserNazTxt);

        panelGora.add(iloscLabel);
        panelGora.add(iloscTxt);

        panelDol.add(potwierdzButton);
        panelDol.add(anulujButton);

        panelPodst.add(panelGora, BorderLayout.CENTER);
        panelPodst.add(panelDol, BorderLayout.PAGE_END);

        imageLabel.setLayout(new GridBagLayout());
        imageLabel.add(panelPodst);

        contentPane.add(imageLabel, BorderLayout.CENTER);

        potwierdzButton.addActionListener(e -> {
            DataBaseConnection con = new DataBaseConnection();
            boolean dodano;
            String[] dane = {tytulTxt.getText(), rokProdukcjiTxt.getText(), cenaTxt.getText(), opisTxt.getText(),
                    rezyserImieTxt.getText(), rezyserNazTxt.getText(), iloscTxt.getText()};
            try {
                con.dodajFilm(dane);
                dodano = true;
            } catch (ClassNotFoundException | SQLException throwables) {
                JOptionPane.showMessageDialog(null, "Nie udało się dodać filmu");
                System.out.println(throwables);
                dodano = false;
            }
            if (dodano) {
                new PanelGlownyAdministratora().wyswietl();
                frame.dispose();
            }
        });

        anulujButton.addActionListener(e -> {
            new PanelGlownyAdministratora().wyswietl();
            frame.dispose();
        });

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
