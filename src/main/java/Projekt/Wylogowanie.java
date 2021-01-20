package Projekt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Wylogowanie {

    private ImageIcon image;

    public void wyswietl(){

        JFrame frame = new JFrame("Wylogowywanie...");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        try {
            image = new ImageIcon(ImageIO.read(new File("src/main/java/img/logout.png")));
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
        panelDol.setOpaque(false);

        JLabel logOutLabel = new JLabel("Pomyślnie wylogowano", JLabel.CENTER);
        logOutLabel.setForeground(Color.WHITE);

        JButton zamknijBtn = new JButton("Zamknij");

        panelGora.add(logOutLabel);
        panelDol.add(zamknijBtn);

        panelPodst.add(panelGora, BorderLayout.CENTER);
        panelPodst.add(panelDol, BorderLayout.PAGE_END);

        imageLabel.setLayout(new GridBagLayout());
        imageLabel.add(panelPodst);

        contentPane.add(imageLabel, BorderLayout.CENTER);

        zamknijBtn.addActionListener(e -> {
            frame.dispose();
            new MenuLogowania().wyswietl();
        }
        );

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
