package Projekt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class PanelGlowny {

    public static String login = null;
    private ImageIcon image, imageLbl;

    public PanelGlowny() {}

    public PanelGlowny(Object login){
        this.login = (String) login;
    }

    public void wyswietl() throws SQLException, ClassNotFoundException {
        DataBaseConnection con = new DataBaseConnection();

        JFrame frame = new JFrame("Panel klienta");
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
        panelDol.setLayout(new GridLayout(6,1, 10, 15));
        panelDol.setOpaque(false);

        JLabel uzytkownikLabel;
        if (con.login.equals("admin")) {
            uzytkownikLabel = new JLabel(("Zalogowany: " + con.login), JLabel.CENTER);
        } else {
            uzytkownikLabel = new JLabel(("Zalogowany: " + con.getClient(login)), JLabel.CENTER);
        }
        uzytkownikLabel.setForeground(Color.WHITE);
        uzytkownikLabel.setFont(new Font("Arial, Verdana", Font.BOLD, 14));
        uzytkownikLabel.setIcon(imageLbl);
        uzytkownikLabel.setHorizontalTextPosition(JLabel.CENTER);
        uzytkownikLabel.setVerticalTextPosition(JLabel.CENTER);

        JButton wypozyczButton = new JButton("Wypożycz film");
        wypozyczButton.setFocusable(false);

        JButton posiadaneButton = new JButton("Twoje filmy");
        posiadaneButton.setFocusable(false);

        JButton wylogujButton = new JButton("Wyloguj");
        wylogujButton.setFocusable(false);

        JButton aktualizujButton = new JButton("Aktualizacja danych");
        aktualizujButton.setFocusable(false);

        JButton usunButton = new JButton("Usuń konto");
        usunButton.setFocusable(false);

        usunButton.setBackground(Color.red);

        panelGora.add(uzytkownikLabel);
        panelDol.add(wypozyczButton);
        panelDol.add(posiadaneButton);
        panelDol.add(aktualizujButton);
        panelDol.add(wylogujButton);
        panelDol.add(usunButton);

        panelPodst.add(panelGora, BorderLayout.CENTER);
        panelPodst.add(panelDol, BorderLayout.PAGE_END);

        imageLabel.setLayout(new GridBagLayout());
        imageLabel.add(panelPodst);

        contentPane.add(imageLabel, BorderLayout.CENTER);

      wypozyczButton.addActionListener(e -> {
            WyborFilmow filmy = new WyborFilmow();
            try {
                filmy.listaFilmow();
                frame.setVisible(false);
            } catch (SQLException | ClassNotFoundException throwables) {
                JOptionPane.showMessageDialog(null, "Błąd połączenia");
            }
        });

      aktualizujButton.addActionListener(l -> {
          try {
              AktualizacjaDanych akt = new AktualizacjaDanych();
              akt.wyswietl();
              frame.dispose();
          } catch (ClassNotFoundException | SQLException e) {
              e.printStackTrace();
          }
      });

        posiadaneButton.addActionListener(e -> {
            WypozyczoneFilmy wf = new WypozyczoneFilmy();
            try {
                wf.wyswietl(true);
                frame.setVisible(false);
            } catch (SQLException | ClassNotFoundException throwables) {
                JOptionPane.showMessageDialog(null, "Brak wypożyczonych filmów");
            }
        });

        wylogujButton.addActionListener(e -> {
            Wylogowanie wylogowanie = new Wylogowanie();
            wylogowanie.wyswietl();
            frame.dispose();
        });

        usunButton.addActionListener(l -> {
            int answ = JOptionPane.showConfirmDialog(null, "Napewno chcesz usunąć konto?", "Usuwanie konta", JOptionPane.YES_NO_OPTION);
            if (answ == 0) {
                try {
                    boolean usuniete = con.usun();
                    if (usuniete) {
                        frame.dispose();
                        JOptionPane.showMessageDialog(null, "Pomyślnie usunięto konto");
                        MenuLogowania log = new MenuLogowania();
                        log.wyswietl();
                    }
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
