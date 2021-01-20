package Projekt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class AktualizacjaDanychPracownika {

    private ImageIcon image;

    public void wyswietl(int idPracownika) throws SQLException, ClassNotFoundException {
        DataBaseConnection con = new DataBaseConnection();
        String[] dane = con.aktualizacjaDanychPracownika(idPracownika);

        JFrame frame = new JFrame("Panel aktualizacji danych pracownika");
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
        panelGora.setLayout(new GridLayout(6, 2, 5, 5));

        JPanel panelDol = new JPanel();
        panelDol.setOpaque(false);

        JTextField imieTxt = new JTextField(dane[0]);
        JTextField nazwiskoTxt = new JTextField(dane[1]);
        JTextField zarobkiTxt = new JTextField(dane[2]);
        JTextField dataTxt = new JTextField(dane[3]);
        JTextField adresTxt = new JTextField(dane[4]);

        JLabel imieLabel = new JLabel(("Imię:"), JLabel.CENTER);
        JLabel nazwiskoLabel = new JLabel(("Nazwisko:"), JLabel.CENTER);
        JLabel zarobkiLabel = new JLabel(("Zarobki:"), JLabel.CENTER);
        JLabel dataLabel = new JLabel(("Data zatrudnienia:"), JLabel.CENTER);
        JLabel adresLabel = new JLabel(("Adres:"), JLabel.CENTER);

        JButton potwierdzButton = new JButton("Potwierdź");
        JButton anulujButton = new JButton("Anuluj");

        panelGora.add(imieLabel);
        panelGora.add(imieTxt);

        panelGora.add(nazwiskoLabel);
        panelGora.add(nazwiskoTxt);

        panelGora.add(zarobkiLabel);
        panelGora.add(zarobkiTxt);

        panelGora.add(dataLabel);
        panelGora.add(dataTxt);

        panelGora.add(adresLabel);
        panelGora.add(adresTxt);

        panelDol.add(potwierdzButton);
        panelDol.add(anulujButton);

        panelPodst.add(panelGora, BorderLayout.CENTER);
        panelPodst.add(panelDol, BorderLayout.PAGE_END);

        imageLabel.setLayout(new GridBagLayout());
        imageLabel.add(panelPodst);

        contentPane.add(imageLabel, BorderLayout.CENTER);

        potwierdzButton.addActionListener(e -> {
            DataBaseConnection l = new DataBaseConnection();
            String[] daneNowe = {imieTxt.getText(), nazwiskoTxt.getText(), zarobkiTxt.getText(), dataTxt.getText(), adresTxt.getText()};
            try {
                l.aktualizacjaDanychPracownika(daneNowe, idPracownika);
                new ListaPracownikow().listaPracownikow();
            } catch (ClassNotFoundException | SQLException throwables) {
                throwables.printStackTrace();
            }
            frame.dispose();
        });

        anulujButton.addActionListener(e -> {
            frame.dispose();
            try {
                new ListaPracownikow().listaPracownikow();
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
