package Projekt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class ListaPracownikow {

    public ListaPracownikow(){}

    public void listaPracownikow() throws SQLException, ClassNotFoundException {
        DataBaseConnection con = new DataBaseConnection();

        JFrame frame = new JFrame("Lista pracownikow");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String[] colNames = {"ID pracownika", "Imie", "Nazwisko", "Zarobki", "Data zatrudnienia", "Adres"};

        DefaultTableModel tableModel = new DefaultTableModel(con.getPracownicy(), colNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[][] data = con.getPracownicy();
        JTable table = new JTable(data, colNames);
        table.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(new Dimension(1100, 500));

        JPanel panelGora = new JPanel(new GridLayout(2, 2, 5, 5));
        JPanel panelDol = new JPanel(new FlowLayout());
        JPanel contentPane = new JPanel(new BorderLayout());

        JButton dodajPracownikaButton = new JButton("Dodaj pracownika");
        JButton aktualizujButton = new JButton("Aktualizuj dane pracownika");
        JButton zwolnijButton = new JButton("Zwolnij pracownika");
        JButton powrotButton = new JButton("Wróć do panelu głównego");

        dodajPracownikaButton.addActionListener(l -> {
            new DodajPracownika().wyswietl();
            frame.dispose();
        });

        aktualizujButton.addActionListener(l -> {
            if (data[table.getSelectedRow()][0] != null) {
                int id = Integer.parseInt(data[table.getSelectedRow()][0]);
                try {
                    new AktualizacjaDanychPracownika().wyswietl(id);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                frame.dispose();
            }
        });

        zwolnijButton.addActionListener(a -> {
            if (data[table.getSelectedRow()][0] != null) {
                if ( JOptionPane.showConfirmDialog(null, "Czy jeseś pewnien że chcesz zwolnić pracownika?", "Potwierdzenie zwolnienia", JOptionPane.YES_NO_OPTION) == 0) {
                    int id = Integer.parseInt(data[table.getSelectedRow()][0]);
                    try {
                        new DataBaseConnection().zwolnijPracownika(id);
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    frame.dispose();
                }
            }
        });

        panelGora.add(dodajPracownikaButton);
        panelGora.add(aktualizujButton);
        panelGora.add(zwolnijButton);
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
