package Projekt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class WyborFilmow {

    public WyborFilmow(){}

    public void listaFilmow() throws SQLException, ClassNotFoundException {
        DataBaseConnection con = new DataBaseConnection();

        JFrame frame = new JFrame("Wybor filmów");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String[] colNames = {"Tytuł", "Rok produkcji", "Reżyser", "Opis", "Cena wypożyczenia", "Ilość"};

        DefaultTableModel tableModel = new DefaultTableModel(con.getMovies(), colNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[][] data = con.getMovies();
        JTable table = new JTable(data, colNames);
        table.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(new Dimension(1100, 500));

        JPanel panelGora = new JPanel(new GridLayout(2, 2, 5, 5));
        JPanel panelDol = new JPanel(new FlowLayout());
        JPanel contentPane = new JPanel(new BorderLayout());

        JButton wypozyczButton = new JButton("Wypożycz film");
        JButton opisButton = new JButton("Zobacz opis filmu");
        JButton posiadaneButton = new JButton("Twoje filmy");
        JButton powrotButton = new JButton("Wróć do panelu głównego");

        opisButton.addActionListener(l -> {
            if (table.getSelectedRow() > -1 && data[table.getSelectedRow()][3] != null)
                JOptionPane.showMessageDialog(null, data[table.getSelectedRow()][3]);
        });

        wypozyczButton.addActionListener(a -> {
            if (table.getSelectedRow() > -1 && data[table.getSelectedRow()][0] != null){
                String tytul = data[table.getSelectedRow()][0];
                try {
                    boolean wypozyczony = con.wypozycz(tytul, con.getAktualnyUzytkownik());
                    if (wypozyczony) {
                        JOptionPane.showMessageDialog(null, "Dodano film do wypozyczonych");
                        frame.dispose();
                        new WyborFilmow().listaFilmow();
                    }
                } catch (ClassNotFoundException | SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        panelGora.add(wypozyczButton);
        panelGora.add(opisButton);
        panelGora.add(posiadaneButton);
        panelGora.add(powrotButton);
        panelDol.add(panelGora);
        contentPane.add(panelDol);
        contentPane.add(scrollPane, BorderLayout.SOUTH);

        posiadaneButton.addActionListener(e -> {
            WypozyczoneFilmy wf = new WypozyczoneFilmy();
            try {
                frame.dispose();
                wf.wyswietl(false);
            } catch (SQLException | ClassNotFoundException throwables) {
                JOptionPane.showMessageDialog(null, "Błąd połączenia");
            }
        });

        powrotButton.addActionListener(e -> {
            frame.dispose();
            try {
            if (!con.login.equals("admin")) {
                new PanelGlowny(new PanelGlowny().login).wyswietl();
            } else
                new PanelGlownyAdministratora().wyswietl();
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
