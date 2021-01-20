package Projekt;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Random;

public class DataBaseConnection {

    public static int aktualnyUzytkownik;
    public static String login;
    Statement stmt;
    ResultSet result;
    ResultSetMetaData meta;
    String url = "jdbc:sqlite:WYPOZYCZALNIA.db3";

    public int getAktualnyUzytkownik() {
        return aktualnyUzytkownik;
    }

    public void setAktualnyUzytkownik(int aktualnyUzytkownik) {
        this.aktualnyUzytkownik = aktualnyUzytkownik;
    }

    public boolean logowanie(Object login, Object haslo) throws ClassNotFoundException, SQLException {
        this.login = (String) login;
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        stmt = con.createStatement();
        String query = "select haslo from UZYTKOWNIK where email = '"+login+"'";
        result = stmt.executeQuery(query);
        meta = result.getMetaData();

        String hasloBaza = null;
        while (result.next())
            hasloBaza = result.getString(1);
        con.close();
        stmt.close();
        if (hasloBaza != null && hasloBaza.equals(haslo)) return true;
        else return false;
    }

    public String getClient(String login) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        stmt = con.createStatement();

        String who = "select id_klienta from UZYTKOWNIK where email = '"+login+"'";
        result = stmt.executeQuery(who);
        int me = result.getInt(1);
        String query = "select imie, nazwisko from KLIENT where id_klienta = '" + me + "'";
        setAktualnyUzytkownik(me);
        result = stmt.executeQuery(query);
        StringBuilder user = new StringBuilder();
        meta = result.getMetaData();

        while (result.next()) {
            for (int i = 0; i < meta.getColumnCount(); i++) {
                user.append(result.getString(i + 1)).append(" ");
            }
        }
        con.close();
        stmt.close();
        return user.toString();
    }

    public String[] aktualizacjaDanych() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        stmt = con.createStatement();
        String[] dane = new String[6];

        if (login.equals("admin")) setAktualnyUzytkownik(0);

        String who = "select email, haslo, imie, nazwisko, numer_tel, adres from UZYTKOWNIK " +
                "inner join KLIENT " +
                "on KLIENT.id_klienta=UZYTKOWNIK.id_uzytkownika " +
                "where KLIENT.id_klienta='"+getAktualnyUzytkownik()+"'";
        result = stmt.executeQuery(who);

        meta = result.getMetaData();

        while (result.next()) {
            for (int i = 0; i < meta.getColumnCount(); i++) {
                dane[i] = result.getString(i+1);
            }
        }
        con.close();
        stmt.close();
        return dane;
    }

    public void addClient(String[] dane) throws ClassNotFoundException, SQLException {
        String email = dane[0];
        String haslo = dane[1];
        String imie = dane[2];
        String nazwisko = dane[3];
        String numer = dane[4];
        String adres = dane[5];

        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        stmt = con.createStatement();
        ResultSet idB = stmt.executeQuery("select id_klienta from KLIENT");
        int id = 0;
        while (idB.next())
            id = idB.getInt(1)+1;
        String klient = "insert into KLIENT values (?,?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(klient);
        pstmt.setInt(1, id);
        pstmt.setString(2, imie);
        pstmt.setString(3, nazwisko);
        pstmt.setString(4, numer);
        pstmt.setString(5, adres);
        pstmt.executeUpdate();
        pstmt.close();

        String uzytkownik = "insert into UZYTKOWNIK values (?,?,?,?)";
        pstmt = con.prepareStatement(uzytkownik);
        pstmt.setInt(1, id);
        pstmt.setString(2, email);
        pstmt.setString(3, haslo);
        pstmt.setInt(4, id);
        pstmt.executeUpdate();
        pstmt.close();
        con.close();
        stmt.close();
    }

    public void addPracownik(String[] dane) throws ClassNotFoundException, SQLException {
        String imie = dane[0];
        String nazwisko = dane[1];
        String zarobki = dane[2];
        String adres = dane[3];

        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        stmt = con.createStatement();
        ResultSet idB = stmt.executeQuery("select id_prac from PRACOWNIK");
        int id = 0;
        while (idB.next())
            id = idB.getInt(1)+1;
        String klient = "insert into PRACOWNIK values (?,?,?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(klient);
        pstmt.setInt(1, id);
        pstmt.setString(2, imie);
        pstmt.setString(3, nazwisko);
        pstmt.setString(4, zarobki);
        pstmt.setString(5, String.valueOf(LocalDate.now()));
        pstmt.setString(6, adres);
        pstmt.executeUpdate();
        pstmt.close();
        stmt.close();
    }

    public void updateClient(String[] dane) throws ClassNotFoundException, SQLException {
        String email = dane[0];
        String haslo = dane[1];
        String imie = dane[2];
        String nazwisko = dane[3];
        String numer = dane[4];
        String adres = dane[5];

        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        stmt = con.createStatement();
        String klient = "update KLIENT set imie=?, nazwisko=?, numer_tel=?, adres=? where id_klienta='"+getAktualnyUzytkownik()+"'";
        PreparedStatement pstmt = con.prepareStatement(klient);
        pstmt.setString(1, imie);
        pstmt.setString(2, nazwisko);
        pstmt.setString(3, numer);
        pstmt.setString(4, adres);
        pstmt.executeUpdate();
        pstmt.close();

        String uzytkownik = "update UZYTKOWNIK set email=?, haslo=? where id_uzytkownika='"+getAktualnyUzytkownik()+"'";
        pstmt = con.prepareStatement(uzytkownik);
        pstmt.setString(1, email);
        pstmt.setString(2, haslo);
        pstmt.executeUpdate();
        pstmt.close();
        con.close();
        stmt.close();
    }

    public String[][] getMovies() throws ClassNotFoundException, SQLException {
        System.out.println();
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        stmt = con.createStatement();
        ResultSet result = stmt.executeQuery("select FILM.tytul, FILM.rok_produkcji, REZYSER.imie, REZYSER.nazwisko, FILM.opis, FILM.cena_wypozyczenia, EGZEMPLARZ.ilosc from FILM " +
                "inner join REZYSER " +
                "on FILM.id_rezysera = REZYSER.id_rezysera " +
                "inner join EGZEMPLARZ " +
                "on FILM.id_filmu = EGZEMPLARZ.id_egzemplarza");
        meta = result.getMetaData();
        String[][] data = new String[100][meta.getColumnCount()];
        int j = 0;
        while (result.next()) {
            data[j][0] = result.getString("tytul");
            data[j][1] = result.getString("rok_produkcji");
            data[j][2] = result.getString("imie") + " " + result.getString("nazwisko");
            data[j][3] = result.getString("opis");
            data[j][4] = result.getString("cena_wypozyczenia");
            data[j][5] = result.getString("ilosc");
            j++;
        }
        con.close();
        stmt.close();
        return data;
    }

    public String[][] getWypozyczone(int id_akt) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        this.stmt = con.createStatement();
        ResultSet result = stmt.executeQuery("select FILM.tytul, FILM.rok_produkcji, REZYSER.imie, REZYSER.nazwisko, WYPOZYCZENIA.data_wypozyczenia, WYPOZYCZENIA.data_zwrotu from FILM " +
                "inner join REZYSER " +
                "on FILM.id_rezysera = REZYSER.id_rezysera " +
                "inner join EGZEMPLARZ " +
                "on EGZEMPLARZ.id_filmu = FILM.id_filmu " +
                "inner join WYPOZYCZENIA " +
                "on WYPOZYCZENIA.id_egzemplarza = EGZEMPLARZ.id_egzemplarza " +
                "where WYPOZYCZENIA.id_klienta = "+id_akt+"");
        meta = result.getMetaData();
        String[][] dataW = new String[50][6];
        int j = 0;
        while (result.next()) {
            dataW[j][0] = result.getString("tytul");
            dataW[j][1] = result.getString("rok_produkcji");
            dataW[j][2] = result.getString("imie") + " " + result.getString("nazwisko");
            dataW[j][3] = result.getString("data_wypozyczenia");
            dataW[j][4] = result.getString("data_zwrotu");
            j++;
        }
        result = stmt.executeQuery("select imie, nazwisko from PRACOWNIK " +
                "inner join WYPOZYCZENIA " +
                "on WYPOZYCZENIA.id_prac = PRACOWNIK.id_prac " +
                "where WYPOZYCZENIA.id_klienta = "+id_akt+"");
        j = 0;
        while (result.next()) {
            dataW[j][5] = result.getString("imie") + " " + result.getString("nazwisko");
            j++;
        }
        con.close();
        stmt.close();
        return dataW;
    }

    public boolean wypozycz(String tytul, int id_klienta) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        this.stmt = con.createStatement();

        ResultSet idB = stmt.executeQuery("select id_wypozyczenia from WYPOZYCZENIA");
        int id = 0;
        while (idB.next())
            id = idB.getInt(1)+1;

        idB = stmt.executeQuery("select id_filmu from FILM where tytul = '"+tytul+"'");
        int id_egzemplarza = 0;
        while (idB.next())
            id_egzemplarza = idB.getInt(1);

        idB = stmt.executeQuery("select id_klienta, id_egzemplarza from WYPOZYCZENIA where id_egzemplarza='"+id_egzemplarza+"' and id_klienta='"+getAktualnyUzytkownik()+"'");
        boolean wypozyczony = true;
        int ilosc = 0;
        while(idB.next()) {
            ilosc++;
            if (ilosc > 0) {
                wypozyczony = false;
                break;
            }
        }

        if (wypozyczony) {
            String klient = "insert into WYPOZYCZENIA values (?,?,?,?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(klient);
            pstmt.setInt(1, id);
            pstmt.setInt(2, id_klienta);
            pstmt.setInt(3, id_egzemplarza);
            pstmt.setString(4, String.valueOf(LocalDate.now()));
            pstmt.setString(5, String.valueOf(LocalDate.now().plusDays(14)));
            pstmt.setInt(6, pracownik());
            pstmt.executeUpdate();
            pstmt.close();

            stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select EGZEMPLARZ.ilosc from EGZEMPLARZ " +
                    "inner join FILM " +
                    "on FILM.id_filmu = EGZEMPLARZ.id_egzemplarza " +
                    "where EGZEMPLARZ.id_egzemplarza='"+id_egzemplarza+"'");
            int odejmij = result.getInt(1)-1;
            String wypozyczAkt = "update EGZEMPLARZ set ilosc='"+odejmij+"' where EGZEMPLARZ.id_egzemplarza='"+id_egzemplarza+"'";
            stmt.executeUpdate(wypozyczAkt);
        } else
            JOptionPane.showMessageDialog(null, "Masz już wypożyczony ten film");
        stmt.close();
        con.close();
        return wypozyczony;
    }

    public boolean usun() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        this.stmt = con.createStatement();

        boolean wypozyczone = true;
        ResultSet result = stmt.executeQuery("select id_klienta from WYPOZYCZENIA where id_klienta='"+getAktualnyUzytkownik()+"'");
        while (result.next()) {
            if (result.getInt(1) == getAktualnyUzytkownik())
                wypozyczone = false;
        }

        if(wypozyczone) {
            stmt.executeUpdate("delete from KLIENT where id_klienta='" + getAktualnyUzytkownik() + "'");
            stmt.executeUpdate("delete from UZYTKOWNIK where id_uzytkownika='" + getAktualnyUzytkownik() + "'");
        } else
            JOptionPane.showMessageDialog(null, "Masz na koncie nie zwrócone filmy");

        con.close();
        stmt.close();
        return wypozyczone;
    }

    public void zwrot(String tytul) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        this.stmt = con.createStatement();
        int id_egzemplarza;
        ResultSet result = stmt.executeQuery("select id_egzemplarza from EGZEMPLARZ " +
                "inner join FILM " +
                "on FILM.id_filmu=EGZEMPLARZ.id_filmu " +
                "where FILM.tytul='"+tytul+"'");
        id_egzemplarza = result.getInt(1);
        stmt.executeUpdate("delete from WYPOZYCZENIA where id_klienta='"+getAktualnyUzytkownik()+"' and id_egzemplarza='"+id_egzemplarza+"'");

        String wypozycz = "select EGZEMPLARZ.ilosc from FILM " +
                "inner join EGZEMPLARZ " +
                "on FILM.id_filmu = EGZEMPLARZ.id_egzemplarza " +
                "where EGZEMPLARZ.id_egzemplarza = '"+id_egzemplarza+"'";
        result = stmt.executeQuery(wypozycz);
        int dodaj = result.getInt(1)+1;
        String wypozyczAkt = "update EGZEMPLARZ set ilosc='"+dodaj+"' where EGZEMPLARZ.id_egzemplarza='"+id_egzemplarza+"'";
        stmt.executeUpdate(wypozyczAkt);
        stmt.close();

        con.close();
        stmt.close();
    }

    public int pracownik() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        this.stmt = con.createStatement();
        ResultSet result = stmt.executeQuery("select id_prac from PRACOWNIK");
        int iloscPracownikow = 0;
        while (result.next())
            iloscPracownikow++;
        Random id_pracownika = new Random();
        int pracownik = id_pracownika.nextInt(iloscPracownikow);
        con.close();
        return pracownik;
    }

    public void dodajFilm (String[] dane) throws ClassNotFoundException, SQLException {
        String tytul = dane[0];
        String rok_produkcji = dane[1];
        String cena = dane[2];
        String opis = dane[3];
        String imie = dane[4];
        String nazwisko = dane[5];
        int ilosc = Integer.parseInt(dane[6]);
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        this.stmt = con.createStatement();
        ResultSet result = stmt.executeQuery("select imie, nazwisko from REZYSER");
        int idRezysera = 0;
        boolean istniejeRezyser = false;
        while (result.next()) {
            String aktualny = result.getString("imie") + " " + result.getString("nazwisko");
            if (aktualny.equalsIgnoreCase(imie + " " + nazwisko)) { istniejeRezyser = true; break; }
            else idRezysera++;
        }

        result = stmt.executeQuery("select tytul, rok_produkcji from FILM");
        int idFilmu = 0;
        boolean istniejeFilm = false;
        while (result.next()) {
            String aktualny = result.getString("tytul") + " " + result.getString("rok_produkcji");
            if (aktualny.equalsIgnoreCase(tytul + " " + rok_produkcji)) { istniejeFilm = true; break; }
            idFilmu++;
        }
        if (!istniejeRezyser && !istniejeFilm) {
            String query = "INSERT INTO REZYSER values(?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, idRezysera);
            ps.setString(2, imie);
            ps.setString(3, nazwisko);
            ps.executeUpdate();
            ps.close();

            query = "INSERT INTO FILM values(?,?,?,?,?,?)";
            ps = con.prepareStatement(query);
            ps.setInt(1, idFilmu);
            ps.setString(2, tytul);
            ps.setString(3, rok_produkcji);
            ps.setString(4, cena);
            ps.setString(5, opis);
            ps.setInt(6, idRezysera);
            ps.executeUpdate();
            ps.close();

            query = "INSERT INTO EGZEMPLARZ values(?,?,?)";
            ps = con.prepareStatement(query);
            ps.setInt(1, idFilmu);
            ps.setInt(2, idFilmu);
            ps.setInt(3, ilosc);
            ps.executeUpdate();
            ps.close();

            JOptionPane.showMessageDialog(null, "Dodoano nowego reżysera i nowy film");
        }
        else if (istniejeRezyser && !istniejeFilm) {
            String query = "INSERT INTO FILM values(?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, idFilmu);
            ps.setString(2, tytul);
            ps.setString(3, rok_produkcji);
            ps.setString(4, cena);
            ps.setString(5, opis);
            ps.setInt(6, idRezysera);
            ps.executeUpdate();
            ps.close();

            query = "INSERT INTO EGZEMPLARZ values(?,?,?)";
            ps = con.prepareStatement(query);
            ps.setInt(1, idFilmu);
            ps.setInt(2, idFilmu);
            ps.setInt(3, ilosc);
            ps.executeUpdate();
            ps.close();

            JOptionPane.showMessageDialog(null, "Dodano nowy film");
        }
        else {
            JOptionPane.showMessageDialog(null, "Nie udało się dodać filmu tutaj");
        }

        con.close();
    }

    public String[][] getPracownicy() throws ClassNotFoundException, SQLException {
        System.out.println();
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        stmt = con.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM PRACOWNIK");
        meta = result.getMetaData();
        String[][] data = new String[100][meta.getColumnCount()];
        int j = 0;
        while (result.next()) {
            data[j][0] = result.getString("id_prac");
            data[j][1] = result.getString("imie");
            data[j][2] = result.getString("nazwisko");
            data[j][3] = result.getString("zarobki");
            data[j][4] = result.getString("data_zatr");
            data[j][5] = result.getString("adres");
            j++;
        }
        con.close();
        stmt.close();
        return data;
    }

    public void aktualizacjaDanychPracownika(String[] dane, int idPracownika) throws ClassNotFoundException, SQLException {
        String imie = dane[0];
        String nazwisko = dane[1];
        String zarobki = dane[2];
        String data = dane[3];
        String adres = dane[4];

        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        stmt = con.createStatement();
        String klient = "update PRACOWNIK set imie=?, nazwisko=?, zarobki=?, data_zatr=?, adres=? where id_prac='"+idPracownika+"'";
        PreparedStatement pstmt = con.prepareStatement(klient);
        pstmt.setString(1, imie);
        pstmt.setString(2, nazwisko);
        pstmt.setString(3, zarobki);
        pstmt.setString(4, data);
        pstmt.setString(5, adres);
        pstmt.executeUpdate();
        pstmt.close();
        con.close();
    }

    public String[] aktualizacjaDanychPracownika(int idPracownika) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        stmt = con.createStatement();
        String[] dane = new String[6];

        String who = "select imie, nazwisko, zarobki, data_zatr, adres from PRACOWNIK where id_prac='"+idPracownika+"'";
        result = stmt.executeQuery(who);

        meta = result.getMetaData();

        while (result.next()) {
            for (int i = 0; i < meta.getColumnCount(); i++) {
                dane[i] = result.getString(i+1);
            }
        }
        con.close();
        stmt.close();
        return dane;
    }

    public void zwolnijPracownika(int idPracownika) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        stmt = con.createStatement();
        String query = "DELETE FROM PRACOWNIK where id_prac='"+idPracownika+"'";
        stmt.executeUpdate(query);
        con.close();
        new ListaPracownikow().listaPracownikow();
    }

    public boolean usunFilm(String tytul) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);

        stmt = con.createStatement();
        String query = "select id_filmu FROM FILM where tytul='"+tytul+"'";
        ResultSet id_filmu = stmt.executeQuery(query);
        int idFilmu = id_filmu.getInt(1);
        stmt.close();

        stmt = con.createStatement();
        query = "select id_egzemplarza FROM EGZEMPLARZ where id_egzemplarza='"+idFilmu+"'";
        ResultSet id_egzemplarza = stmt.executeQuery(query);
        int idEgzemplarza = id_egzemplarza.getInt(1);
        stmt.close();

        stmt = con.createStatement();
        query = "select id_egzemplarza FROM WYPOZYCZENIA where id_egzemplarza='"+idEgzemplarza+"'";
        ResultSet id_wypozyczenia = stmt.executeQuery(query);
        stmt.close();
        int idWypozyczenia;
        if(id_wypozyczenia.next())
            idWypozyczenia = id_wypozyczenia.getInt(1);
        else {
            idWypozyczenia = idEgzemplarza-1;
            stmt = con.createStatement();
            query = "DELETE FROM FILM where id_filmu='"+idFilmu+"'";
            stmt.executeUpdate(query);
            stmt.close();
        }

        boolean wypozyczony = true;

        if(!(idWypozyczenia == idEgzemplarza)) {
            wypozyczony = false;
        }
        con.close();
        return wypozyczony;
    }
}
