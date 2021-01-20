package Projekt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class UsunFilm {

    public UsunFilm(){}

    public void listaFilmow() throws SQLException, ClassNotFoundException {
        DataBaseConnection con = new DataBaseConnection();

        JFrame frame = new JFrame("Usuwanie filmów");
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

        JPanel panelGora = new JPanel(new GridLayout(1, 3, 5, 5));
        JPanel panelDol = new JPanel(new FlowLayout());
        JPanel contentPane = new JPanel(new BorderLayout());

        JButton opisButton = new JButton("Zobacz opis filmu");
        JButton usunButton = new JButton("Usuń film");
        usunButton.setBackground(Color.red);
        JButton powrotButton = new JButton("Wróć do panelu głównego");

        opisButton.addActionListener(l -> {
            if (table.getSelectedRow() > -1 && data[table.getSelectedRow()][3] != null)
                JOptionPane.showMessageDialog(null, data[table.getSelectedRow()][3]);
        });

        usunButton.addActionListener(a -> {
            if (table.getSelectedRow() > -1 && data[table.getSelectedRow()][0] != null){
                String tytul = data[table.getSelectedRow()][0];
                try {
                    if (!con.usunFilm(tytul)) {
                        JOptionPane.showMessageDialog(null, "Usunięto film");
                        frame.dispose();
                        new UsunFilm().listaFilmow();
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Film jest wypożyczony i nie można go usunąć");
                        frame.dispose();
                        new UsunFilm().listaFilmow();
                    }
                } catch (ClassNotFoundException | SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        panelGora.add(usunButton);
        panelGora.add(opisButton);
        panelGora.add(powrotButton);
        panelDol.add(panelGora);
        contentPane.add(panelDol);
        contentPane.add(scrollPane, BorderLayout.SOUTH);

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
