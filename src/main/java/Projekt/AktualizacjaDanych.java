package Projekt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class AktualizacjaDanych {

    private ImageIcon image;

    public void wyswietl() throws SQLException, ClassNotFoundException {
        DataBaseConnection con = new DataBaseConnection();
        String[] dane = con.aktualizacjaDanych();

        JFrame frame = new JFrame("Panel aktualizacji");
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

        JTextField loginTxt = new JTextField(dane[0]);
        JTextField passTxt = new JPasswordField(dane[1]);
        JTextField imieTxt = new JTextField(dane[2]);
        JTextField nazwiskoTxt = new JTextField(dane[3]);
        JTextField numerTxt = new JTextField(dane[4]);
        JTextField adresTxt = new JTextField(dane[5]);

        JLabel userLabel = new JLabel(("Login/email:"), JLabel.CENTER);
        JLabel passwordLabel = new JLabel(("Hasło:"), JLabel.CENTER);
        JLabel imieLabel = new JLabel(("Imię:"), JLabel.CENTER);
        JLabel nazwiskoLabel = new JLabel(("Nazwisko:"), JLabel.CENTER);
        JLabel numerLabel = new JLabel(("Numer telefonu:"), JLabel.CENTER);
        JLabel adresLabel = new JLabel(("Adres:"), JLabel.CENTER);

        JButton potwierdzButton = new JButton("Potwierdź");
        JButton anulujButton = new JButton("Anuluj");

        panelGora.add(userLabel);
        panelGora.add(loginTxt);

        panelGora.add(passwordLabel);
        panelGora.add(passTxt);

        panelGora.add(imieLabel);
        panelGora.add(imieTxt);

        panelGora.add(nazwiskoLabel);
        panelGora.add(nazwiskoTxt);

        panelGora.add(numerLabel);
        panelGora.add(numerTxt);

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
            String[] daneNowe = {loginTxt.getText(), passTxt.getText(), imieTxt.getText(), nazwiskoTxt.getText(), numerTxt.getText(), adresTxt.getText()};
            try {
                l.updateClient(daneNowe);
                if (!con.login.equals("admin"))
                    new PanelGlowny(loginTxt.getText()).wyswietl();
                else new PanelGlownyAdministratora().wyswietl();
            } catch (ClassNotFoundException | SQLException throwables) {
                throwables.printStackTrace();
            }
            frame.dispose();
        });

        anulujButton.addActionListener(e -> {
            frame.dispose();
            try {
                if (!con.login.equals("admin"))
                    new PanelGlowny(dane[0]).wyswietl();
                else new PanelGlownyAdministratora().wyswietl();
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
