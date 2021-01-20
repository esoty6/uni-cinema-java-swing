package Projekt;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class WypozyczoneFilmy {

    public void wyswietl(boolean okno) throws SQLException, ClassNotFoundException {
        DataBaseConnection con = new DataBaseConnection();
        String[][] dataW = con.getWypozyczone(con.getAktualnyUzytkownik());
        String[] colNames = {"Tytuł", "Rok produkcji", "Reżyser", "Data wypożyczenia", "Data zwrotu", "Obsługiwał"};
        JFrame frame = new JFrame("Wypożyczone filmy");

        JTable tableW = new JTable(dataW, colNames);
        tableW.setPreferredScrollableViewportSize(new Dimension(600, 250));

        JPanel panelGora = new JPanel(new GridLayout(10, 1, 10, 10));
        JPanel panelDol = new JPanel(new BorderLayout());
        JPanel contentPane = new JPanel(new GridLayout(2,1,10,10));

        JButton powrotButton = new JButton("Powrót");
        JButton zwrocButton = new JButton("Zwróć film");

        JScrollPane scrollPane = new JScrollPane(tableW);

        if (okno) {
            powrotButton.addActionListener(a -> {
                frame.dispose();
                try {
                    new PanelGlowny().wyswietl();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
        } else {
            powrotButton.addActionListener(a -> {
                frame.dispose();
                try {
                    new WyborFilmow().listaFilmow();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }

        if (okno) {
            zwrocButton.addActionListener(l -> {
                try {
                    con.zwrot(dataW[tableW.getSelectedRow()][0]);
                    JOptionPane.showMessageDialog(null, "Zwrócono film");
                    frame.dispose();
                    new WypozyczoneFilmy().wyswietl(true);
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            });
        } else {
            zwrocButton.addActionListener(l -> {
                try {
                    con.zwrot(dataW[tableW.getSelectedRow()][0]);
                    JOptionPane.showMessageDialog(null, "Zwrócono film");
                    frame.dispose();
                    wyswietl(false);
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            });
        }

        panelGora.add(zwrocButton);
        panelGora.add(powrotButton);
        panelDol.add(panelGora, BorderLayout.EAST);
        panelDol.add(scrollPane);
        contentPane.add(panelDol);

        frame.add(panelDol);
        frame.setVisible(true);
        frame.setMinimumSize(new Dimension(1000, 450));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
