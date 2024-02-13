package upravljanje.filmskom.produkcijom.projekt.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upravljanje.filmskom.produkcijom.projekt.entiteti.*;
import upravljanje.filmskom.produkcijom.projekt.iznimke.CijenaException;
import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public interface Database {

    String DATABASE_FILE = "C:\\Java-faks\\#Projekt\\Projekt\\conf\\database.properties";

    Logger logger = LoggerFactory.getLogger(Database.class);


    static Connection spajanjeNaBazu() throws IOException, SQLException {
        Connection veza;
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(DATABASE_FILE));
            String url = properties.getProperty("databaseUrl");
            String user = properties.getProperty("username");
            String pass = properties.getProperty("lozinka");
            veza = DriverManager.getConnection(url, user, pass);
        } catch (IOException e) {
            logger.error("Problem kod spajanja s bazom podataka");
            throw new IOException("Error while reading properties file for DB.", e);
        } catch (SQLException e) {
            logger.error("Problem kod spajanja s bazom podataka");
            throw new SQLException("Error while connecting to database.", e);
        }
        return veza;
    }


    public static List<Distribucija> dohvatiDistribucije() {
        List<Distribucija> distribucije = new ArrayList<>();

        try (Connection veza = spajanjeNaBazu()) {
            String sqlQuery = "SELECT * FROM DISTRIBUCIJA";
            Statement stmt = veza.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                Long id = rs.getLong("ID");
                LocalDate pocetakProjekta = rs.getDate("POCETAK_PROJEKTA").toLocalDate();
                LocalDate krajProjekta = rs.getDate("KRAJ_PROJEKTA").toLocalDate();
                LocalDate izlazakFilma = rs.getDate("IZLAZAK_FILMA").toLocalDate();


                Distribucija distribucija = new Distribucija(id, pocetakProjekta, krajProjekta, izlazakFilma);

                distribucije.add(distribucija);
            }


        } catch (SQLException | IOException ex) {
            String message = "Dogodila se greška kod dohvaćanja Distribucija iz baze podataka";
            System.out.println(message);
            ex.printStackTrace();
            logger.error(message);
        }

        return distribucije;
    }

    public static List<Financije> dohvatiFinancije() {
        List<Financije> financije = new ArrayList<>();

        try (Connection veza = spajanjeNaBazu()) {
            String sqlQuery = "SELECT * FROM FINANCIJE";
            Statement stmt = veza.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                Long id = rs.getLong("ID");
                BigDecimal proracunB = rs.getBigDecimal("PRORACUN");
                BigDecimal troskoviB = rs.getBigDecimal("TROSKOVI");
                BigDecimal prihodiB = rs.getBigDecimal("PRIHODI");

                Cijena proracun = new Cijena(proracunB);
                Cijena troskovi = new Cijena(troskoviB);
                Cijena prihodi = new Cijena(prihodiB);

                Financije financija = new Financije(id, proracun, troskovi, prihodi);

                financije.add(financija);
            }


        } catch (SQLException | IOException ex) {
            String message = "Dogodila se greška kod dohvaćanja Financija iz baze podataka";
            System.out.println(message);
            ex.printStackTrace();
            logger.error(message);
        }

        return financije;
    }

    public static List<Rola> dohvatiRole() {
        List<Rola> role = new ArrayList<>();

        try (Connection veza = spajanjeNaBazu()) {
            String sqlQuery = "SELECT * FROM ROLA";
            Statement stmt = veza.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                Long id = rs.getLong("ID");
                String nazivRole = rs.getString("NAZIV_ROLE");
                String opis = rs.getString("OPIS");

                Rola rola = new Rola(id, nazivRole, opis);

                role.add(rola);
            }


        } catch (SQLException | IOException ex) {
            String message = "Dogodila se greška kod dohvaćanja Rola iz baze podataka";
            System.out.println(message);
            ex.printStackTrace();
            logger.error(message);
        }

        return role;
    }

    public static List<Korisnik> dohvatiKorisnike() {
        List<Korisnik> korisnici = new ArrayList<>();

        try (Connection veza = spajanjeNaBazu()) {
            String sqlQuery = "SELECT * FROM KORISNIK";
            Statement stmt = veza.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                Long id = rs.getLong("ID");
                String idKorisnika = rs.getString("ID_KORISNIKA");
                String lozinka = rs.getString("LOZINKA");
                String ime = rs.getString("IME");
                String prezime = rs.getString("PREZIME");
                Long rolaB = rs.getLong("ROLA");

                List<Rola> role = dohvatiRole();

                Rola rola = null;

                for (Rola rola1 : role) {
                    if (rola1.getIdRole().equals(rolaB)) {
                        rola = rola1;
                    }
                }
                lozinka.hashCode();
                lozinka.hashCode();

                Korisnik korisnik = new Korisnik(idKorisnika, lozinka, ime, prezime, rola);

                korisnici.add(korisnik);
            }


        } catch (SQLException | IOException ex) {
            String message = "Dogodila se greška kod dohvaćanja Korisnika iz baze podataka";
            System.out.println(message);
            ex.printStackTrace();
            logger.error(message);
        }

        return korisnici;
    }

    public static List<ProdukcijskaKuca> dohvatiProdukcijskeKuce() {
        List<ProdukcijskaKuca> produkcijskeKuce = new ArrayList<>();

        try (Connection veza = spajanjeNaBazu()) {
            String sqlQuery = "SELECT * FROM PRODUKCIJSKA_KUCA";
            Statement stmt = veza.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                Long id = rs.getLong("ID");
                String naziv = rs.getString("NAZIV");
                Integer godina_osnutka = rs.getInt("GODINA_OSNUTKA");
                Long sjedisteB = rs.getLong("SJEDISTE");


                List<Adresa> adrese = dohvatiAdrese();

                Adresa sjediste = adrese.get((int) (sjedisteB - 1));

                for (Adresa adresa1 : adrese) {
                    if (adresa1.getId().equals(sjedisteB)) {
                        sjediste = adresa1;
                    }
                }

                ProdukcijskaKuca produkcijskaKuca = new ProdukcijskaKuca(id, naziv, godina_osnutka, sjediste);

                produkcijskeKuce.add(produkcijskaKuca);
            }


        } catch (SQLException | IOException ex) {
            String message = "Dogodila se greška kod dohvaćanja Produkcijskih kuća iz baze podataka";
            System.out.println(message);
            ex.printStackTrace();
            logger.error(message);
        }

        return produkcijskeKuce;
    }


    public static List<Projekt> dohvatiProjekte() {
        List<Projekt> projekti = new ArrayList<>();

        try (Connection veza = spajanjeNaBazu()) {
            String sqlQuery = "SELECT * FROM PROJEKT";
            Statement stmt = veza.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                Long id = rs.getLong("ID");
                Long produkcijska_kucaB = rs.getLong("PRODUKCIJSKA_KUCA");
                Integer brojNominacija = rs.getInt("BROJ_NOMINACIJA");
                Integer brojNagrada = rs.getInt("BROJ_NAGRADA");
                Long financijeB = rs.getLong("FINANCIJE");
                Long distribucijaB = rs.getLong("DISTRIBUCIJA");
                LocalDate pocetakRada = rs.getDate("POCETAK_RADA").toLocalDate();


                List<ProdukcijskaKuca> prodKuceL = dohvatiProdukcijskeKuce();
                List<Financije> financijeL = dohvatiFinancije();
                List<Distribucija> distribucijeL = dohvatiDistribucije();

                ProdukcijskaKuca produkcijskaKuca = prodKuceL.get((int) (produkcijska_kucaB - 1));
                Financije financije = financijeL.get((int) (financijeB - 1));
                Distribucija distribucija = distribucijeL.get((int) (distribucijaB - 1));

                for (ProdukcijskaKuca prod1 : prodKuceL) {
                    if (prod1.getIdProdukcijskeKuce().equals(produkcijska_kucaB)) {
                        produkcijskaKuca = prod1;
                    }
                }
                for (Financije fin1 : financijeL) {
                    if (fin1.getIdFinancije().equals(financijeB)) {
                        financije = fin1;
                    }
                }
                for (Distribucija dis1 : distribucijeL) {
                    if (dis1.getIdDistribucije() == (distribucijaB)) {
                        distribucija = dis1;
                    }
                }

                Projekt projekt = new Projekt(id, produkcijskaKuca, brojNominacija, brojNagrada, financije, distribucija, pocetakRada);

                projekti.add(projekt);
            }


        } catch (SQLException | IOException ex) {
            String message = "Dogodila se greška kod dohvaćanja Korisnika iz baze podataka";
            System.out.println(message);
            ex.printStackTrace();
            logger.error(message);
        }

        return projekti;
    }

    public static List<LokacijaSnimanja> dohvatiLokacijeSnimanja() {
        List<LokacijaSnimanja> lokacije = new ArrayList<>();

        try (Connection veza = spajanjeNaBazu()) {
            String sqlQuery = "SELECT * FROM LOKACIJA_SNIMANJA";
            Statement stmt = veza.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                Long id = rs.getLong("ID");
                Long lokacijaB = rs.getLong("LOKACIJA");
                Long projektB = rs.getLong("PROJEKT");
                LocalDateTime rezervirano_od = rs.getTimestamp("REZERVIRANO_OD").toLocalDateTime();
                LocalDateTime rezervirano_do = rs.getTimestamp("REZERVIRANO_DO").toLocalDateTime();


                List<Adresa> adrese = dohvatiAdrese();
                List<Projekt> projektiL = dohvatiProjekte();


                Adresa adresa = adrese.get(1);
                Projekt projekt = projektiL.get(1);


                for (Adresa lok1 : adrese) {
                    if (lok1.getId().equals(lokacijaB)) {
                        adresa = lok1;
                    }
                }
                for (Projekt pro1 : projektiL) {
                    if (pro1.getIdProjekta().equals(projektB)) {
                        projekt = pro1;
                    }
                }


                LokacijaSnimanja lokacijaSnimanja = new LokacijaSnimanja(id, adresa, projekt, rezervirano_od, rezervirano_do);

                lokacije.add(lokacijaSnimanja);
            }


        } catch (SQLException | IOException ex) {
            String message = "Dogodila se greška kod dohvaćanja Lokacija snimanja iz baze podataka";
            System.out.println(message);
            ex.printStackTrace();
            logger.error(message);
        }

        return lokacije;
    }

    public static List<ClanEkipe> dohvatiClanove() {
        List<ClanEkipe> clanovi = new ArrayList<>();

        try (Connection veza = spajanjeNaBazu()) {
            String sqlQuery = "SELECT * FROM CLAN_EKIPE";
            Statement stmt = veza.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                Long id = rs.getLong("ID");
                String ime = rs.getString("IME");
                String prezime = rs.getString("PREZIME");
                String oib = rs.getString("OIB");
                Integer dob = rs.getInt("DOB");
                String opis_posla = rs.getString("OPIS_POSLA");
                Long trenutniProjekt = rs.getLong("TRENUTNI_PROJEKT");


                List<Projekt> projektiL = dohvatiProjekte();

                Projekt projekt = projektiL.get((int) (trenutniProjekt - 1));

                for (Projekt pro1 : projektiL) {
                    if (pro1.getIdProjekta().equals(trenutniProjekt)) {
                        projekt = pro1;
                    }
                }


                ClanEkipe clan = new ClanEkipe(id, ime, prezime, oib, dob, opis_posla, projekt);

                clanovi.add(clan);
            }


        } catch (SQLException | IOException ex) {
            String message = "Dogodila se greška kod dohvaćanja Članova ekipe iz baze podataka";
            System.out.println(message);
            ex.printStackTrace();
            logger.error(message);
        }

        return clanovi;
    }


    public static List<Glumac> dohvatiGlumce() {
        List<Glumac> glumci = new ArrayList<>();

        try (Connection veza = spajanjeNaBazu()) {
            String sqlQuery = "SELECT * FROM GLUMAC";
            Statement stmt = veza.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                Long id = rs.getLong("ID");
                String ime = rs.getString("IME");
                String prezime = rs.getString("PREZIME");
                String oib = rs.getString("OIB");
                Integer dob = rs.getInt("DOB");
                String umjetnicko_ime = rs.getString("UMJETNICKO_IME");
                Integer brojNominacija = rs.getInt("BROJ_NOMINACIJA");
                Integer brojNagrada = rs.getInt("BROJ_NAGRADA");
                Long trenutniProjekt = rs.getLong("TRENUTNI_PROJEKT");
                LocalDate pocetakRada = rs.getDate("POCETAK_RADA").toLocalDate();

                List<Projekt> projektiL = dohvatiProjekte();

                Projekt projekt = projektiL.get((int) (trenutniProjekt - 1));

                for (Projekt pro1 : projektiL) {
                    if (pro1.getIdProjekta().equals(trenutniProjekt)) {
                        projekt = pro1;
                    }
                }


                Glumac glumac = new Glumac(id, ime, prezime, oib, dob, umjetnicko_ime, brojNominacija, brojNagrada, projekt, pocetakRada);

                glumci.add(glumac);
            }


        } catch (SQLException | IOException ex) {
            String message = "Dogodila se greška kod dohvaćanja Glumaca iz baze podataka";
            System.out.println(message);
            ex.printStackTrace();
            logger.error(message);
        }

        return glumci;
    }


    public static List<Producent> dohvatiProducente() {
        List<Producent> producenti = new ArrayList<>();

        try (Connection veza = spajanjeNaBazu()) {
            String sqlQuery = "SELECT * FROM PRODUCENT";
            Statement stmt = veza.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                Long id = rs.getLong("ID");
                String ime = rs.getString("IME");
                String prezime = rs.getString("PREZIME");
                String oib = rs.getString("OIB");
                Integer dob = rs.getInt("DOB");
                String umjetnicko_ime = rs.getString("UMJETNICKO_IME");
                Integer brojNominacija = rs.getInt("BROJ_NOMINACIJA");
                Integer brojNagrada = rs.getInt("BROJ_NAGRADA");
                Long trenutniProjekt = rs.getLong("TRENUTNI_PROJEKT");
                LocalDate pocetakRada = rs.getDate("POCETAK_RADA").toLocalDate();

                List<Projekt> projektiL = dohvatiProjekte();

                Projekt projekt = projektiL.get((int) (trenutniProjekt - 1));

                for (Projekt pro1 : projektiL) {
                    if (pro1.getIdProjekta().equals(trenutniProjekt)) {
                        projekt = pro1;
                    }
                }


                Producent producent = new Producent(id, ime, prezime, oib, dob, umjetnicko_ime, brojNominacija, brojNagrada, projekt, pocetakRada);

                producenti.add(producent);
            }


        } catch (SQLException | IOException ex) {
            String message = "Dogodila se greška kod dohvaćanja Producenata iz baze podataka";
            System.out.println(message);
            ex.printStackTrace();
            logger.error(message);
        }

        return producenti;
    }


    public static List<Scena> dohvatiScene() {
        List<Scena> scene = new ArrayList<>();

        try (Connection veza = spajanjeNaBazu()) {
            String sqlQuery = "SELECT * FROM SCENA";
            Statement stmt = veza.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();


            while (rs.next()) {
                Long id = rs.getLong("ID");
                LocalDateTime pocetakSnimanja = rs.getTimestamp("POCETAK_SNIMANJA").toLocalDateTime();
                LocalDateTime krajSnimanja = rs.getTimestamp("KRAJ_SNIMANJA").toLocalDateTime();
                Long lokacijaSnimanjaB = rs.getLong("LOKACIJA_SNIMANJA");
                Long projektB = rs.getLong("PROJEKT");
                Long producentB = rs.getLong("PRODUCENT");

                List<LokacijaSnimanja> lokacijaL = dohvatiLokacijeSnimanja();
                List<Projekt> projektiL = dohvatiProjekte();
                List<Producent> producentL = dohvatiProducente();

                LokacijaSnimanja lokacijaSnimanja = lokacijaL.get(1);
                Projekt projekt = projektiL.get(1);
                Producent producent = producentL.get(1);

                for (LokacijaSnimanja lok1 : lokacijaL) {
                    if (lok1.getIdLokacije().equals(lokacijaSnimanjaB)) {
                        lokacijaSnimanja = lok1;
                    }
                }
                for (Projekt pro1 : projektiL) {
                    if (pro1.getIdProjekta().equals(projektB)) {
                        projekt = pro1;
                    }
                }
                for (Producent prod1 : producentL) {
                    if (prod1.getIdOsobe().equals(producentB)) {
                        producent = prod1;
                    }
                }


                Scena scena = new Scena(id, pocetakSnimanja, krajSnimanja, lokacijaSnimanja, projekt, producent);

                scene.add(scena);
            }


        } catch (SQLException | IOException ex) {
            String message = "Dogodila se greška kod dohvaćanja Scena iz baze podataka";
            System.out.println(message);
            ex.printStackTrace();
            logger.error(message);
        }

        return scene;
    }

    public static List<Adresa> dohvatiAdrese() {
        List<Adresa> adrese = new ArrayList<>();

        try (Connection veza = spajanjeNaBazu()) {
            String sqlQuery = "SELECT * FROM ADRESA";
            Statement stmt = veza.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                Long id = rs.getLong("ID");
                Integer broj = rs.getInt("BROJ");
                String ulica = rs.getString("ULICA");
                String grad = rs.getString("GRAD");
                String postanskiBroj = rs.getString("POSTANSKI_BROJ");
                String drzava = rs.getString("DRZAVA");


                Adresa adresa = new Adresa(id, broj, ulica, grad, postanskiBroj, drzava);

                adrese.add(adresa);
            }


        } catch (SQLException | IOException ex) {
            String message = "Dogodila se greška kod dohvaćanja Adresa iz baze podataka";
            System.out.println(message);
            ex.printStackTrace();
            logger.error(message);
        }

        return adrese;
    }

    static void updateAdresu(Adresa adresa) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement("UPDATE ADRESA " +
                    "SET BROJ = ?, ULICA = ?, GRAD = ?, POSTANSKI_BROJ = ?, DRZAVA = ?" +
                    "WHERE ID = ?");
            statement.setInt(1, adresa.getBroj());
            statement.setString(2, adresa.getUlica());
            statement.setString(3, adresa.getGrad());
            statement.setString(4, adresa.getPostanskiBroj());
            statement.setString(5, adresa.getDrzava());
            statement.setLong(6, adresa.getId());
            statement.executeUpdate();

        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
        }
    }

    static void obrisiAdresu(Adresa adresa) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement(
                    "DELETE FROM ADRESA WHERE ID = ?");
            statement.setLong(1, adresa.getId());
            statement.executeUpdate();
        } catch (org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }

    public static void dodajAdresu(Adresa adresa) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement stmt = veza.prepareStatement(
                    "INSERT INTO ADRESA (BROJ, ULICA, GRAD, POSTANSKI_BROJ, DRZAVA) " +
                            "VALUES(?, ?, ?, ?, ?)");
            stmt.setInt(1, adresa.getBroj());
            stmt.setString(2, adresa.getUlica());
            stmt.setString(2, adresa.getUlica());
            stmt.setString(3, adresa.getGrad());
            stmt.setString(4, adresa.getPostanskiBroj());
            stmt.setString(5, adresa.getDrzava());
            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }


    static void updateLokaciju(LokacijaSnimanja lokacija) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement("UPDATE LOKACIJA_SNIMANJA " +
                    "SET LOKACIJA = ?, PROJEKT = ?, REZERVIRANO_OD = ?, REZERVIRANO_DO = ?" +
                    "WHERE ID = ?");
            statement.setLong(1, lokacija.getLokacija().getId());
            statement.setLong(2, lokacija.getProjekt().getIdProjekta());
            statement.setTimestamp(3, Timestamp.valueOf(lokacija.getRezerviranoOd()));
            statement.setTimestamp(4, Timestamp.valueOf(lokacija.getRezerviranoDo()));
            statement.setLong(5, lokacija.getIdLokacije());
            statement.executeUpdate();

        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }

    static void obrisiLokaciju(LokacijaSnimanja lokacija) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement(
                    "DELETE FROM LOKACIJA_SNIMANJA WHERE ID = ?");
            statement.setLong(1, lokacija.getIdLokacije());
            statement.executeUpdate();
        } catch (org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException e) {
        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }

    public static void dodajLokaciju(LokacijaSnimanja loakcija) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement stmt = veza.prepareStatement(
                    "INSERT INTO LOKACIJA_SNIMANJA (LOKACIJA, PROJEKT, REZERVIRANO_OD, REZERVIRANO_DO) " +
                            "VALUES(?, ?, ?, ?)");
            stmt.setLong(1, loakcija.getLokacija().getId());
            stmt.setLong(2, loakcija.getProjekt().getIdProjekta());
            stmt.setTimestamp(3, Timestamp.valueOf(loakcija.getRezerviranoOd()));
            stmt.setTimestamp(4, Timestamp.valueOf(loakcija.getRezerviranoDo()));
            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    static void updateGlumca(Glumac glumac) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement("UPDATE GLUMAC " +
                    "SET IME = ?, PREZIME = ?, OIB = ?, DOB = ?, UMJETNICKO_IME = ?, BROJ_NOMINACIJA = ?, BROJ_NAGRADA = ?,  TRENUTNI_PROJEKT = ?, POCETAK_RADA = ?  " +
                    "WHERE ID = ?");
            statement.setString(1, glumac.getIme());
            statement.setString(2, glumac.getPrezime());
            statement.setString(3, glumac.getOIB());
            statement.setInt(4, glumac.getDob());
            statement.setString(5, glumac.getUmjetnickoIme());
            statement.setInt(6, glumac.getBrojNominacija());
            statement.setInt(7, glumac.getBrojNagrada());
            statement.setLong(8, glumac.getTrenutniProjekt().getIdProjekta());
            statement.setTimestamp(9, Timestamp.valueOf(glumac.getPocetakRada().atStartOfDay()));
            statement.setLong(10, glumac.getIdOsobe());
            statement.executeUpdate();

        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }

    static void obrisiGlumca(Glumac glumac) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement(
                    "DELETE FROM GLUMAC WHERE ID = ?");
            statement.setLong(1, glumac.getIdOsobe());
            statement.executeUpdate();
        } catch (org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException e) {
        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }

    public static void dodajGlumca(Glumac glumac) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement stmt = veza.prepareStatement(
                    "INSERT INTO GLUMAC (IME, PREZIME, OIB, DOB, UMJETNICKO_IME, BROJ_NOMINACIJA, BROJ_NAGRADA, TRENUTNI_PROJEKT, POCETAK_RADA) " +
                            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, glumac.getIme());
            stmt.setString(2, glumac.getPrezime());
            stmt.setString(3, glumac.getOIB());
            stmt.setInt(4, glumac.getDob());
            stmt.setString(5, glumac.getUmjetnickoIme());
            stmt.setInt(6, glumac.getBrojNominacija());
            stmt.setInt(7, glumac.getBrojNagrada());
            stmt.setLong(8, glumac.getTrenutniProjekt().getIdProjekta());
            stmt.setTimestamp(9, Timestamp.valueOf(glumac.getPocetakRada().atStartOfDay()));

            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    static void updateProducent(Producent producent) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement("UPDATE PRODUCENT " +
                    "SET IME = ?, PREZIME = ?, OIB = ?, DOB = ?, UMJETNICKO_IME = ?, BROJ_NOMINACIJA = ?, BROJ_NAGRADA = ?,  TRENUTNI_PROJEKT = ?, POCETAK_RADA = ?  " +
                    "WHERE ID = ?");
            statement.setString(1, producent.getIme());
            statement.setString(2, producent.getPrezime());
            statement.setString(3, producent.getOIB());
            statement.setInt(4, producent.getDob());
            statement.setString(5, producent.getUmjetnickoIme());
            statement.setInt(6, producent.getBrojNominacija());
            statement.setInt(7, producent.getBrojNagrada());
            statement.setLong(8, producent.getTrenutniProjekt().getIdProjekta());
            statement.setTimestamp(9, Timestamp.valueOf(producent.getPocetakRada().atStartOfDay()));
            statement.setLong(10, producent.getIdOsobe());
            statement.executeUpdate();

        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }

    static void obrisiProducent(Producent producent) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement(
                    "DELETE FROM PRODUCENT WHERE ID = ?");
            statement.setLong(1, producent.getIdOsobe());
            statement.executeUpdate();
        } catch (org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException e) {
        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }

    public static void dodajProducenta(Producent producent) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement stmt = veza.prepareStatement(
                    "INSERT INTO PRODUCENT (IME, PREZIME, OIB, DOB, UMJETNICKO_IME, BROJ_NOMINACIJA, BROJ_NAGRADA, TRENUTNI_PROJEKT, POCETAK_RADA) " +
                            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, producent.getIme());
            stmt.setString(2, producent.getPrezime());
            stmt.setString(3, producent.getOIB());
            stmt.setInt(4, producent.getDob());
            stmt.setString(5, producent.getUmjetnickoIme());
            stmt.setInt(6, producent.getBrojNominacija());
            stmt.setInt(7, producent.getBrojNagrada());
            stmt.setLong(8, producent.getTrenutniProjekt().getIdProjekta());
            stmt.setTimestamp(9, Timestamp.valueOf(producent.getPocetakRada().atStartOfDay()));

            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    static void updateClan(ClanEkipe clanEkipe) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement("UPDATE CLAN_EKIPE " +
                    "SET IME = ?, PREZIME = ?, OIB = ?, DOB = ?, OPIS_POSLA = ?, TRENUTNI_PROJEKT = ? " +
                    "WHERE ID = ?");
            statement.setString(1, clanEkipe.getIme());
            statement.setString(2, clanEkipe.getPrezime());
            statement.setString(3, clanEkipe.getOIB());
            statement.setInt(4, clanEkipe.getDob());
            statement.setString(5, clanEkipe.getOpisPosla());
            statement.setLong(6, clanEkipe.getTrenutniProjekt().getIdProjekta());
            statement.setLong(7, clanEkipe.getIdOsobe());
            statement.executeUpdate();


        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }

    static void obrisiClana(ClanEkipe clanEkipe) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement(
                    "DELETE FROM CLAN_EKIPE WHERE ID = ?");
            statement.setLong(1, clanEkipe.getIdOsobe());
            statement.executeUpdate();
        } catch (org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException e) {
        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }

    public static void dodajClana(ClanEkipe clanEkipe) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement stmt = veza.prepareStatement(
                    "INSERT INTO CLAN_EKIPE (IME, PREZIME, OIB, DOB, OPIS_POSLA, TRENUTNI_PROJEKT) " +
                            "VALUES(?, ?, ?, ?, ?, ?)");
            stmt.setString(1, clanEkipe.getIme());
            stmt.setString(2, clanEkipe.getPrezime());
            stmt.setString(3, clanEkipe.getOIB());
            stmt.setInt(4, clanEkipe.getDob());
            stmt.setString(5, clanEkipe.getOpisPosla());
            stmt.setLong(6, clanEkipe.getTrenutniProjekt().getIdProjekta());


            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    static void updateKuca(ProdukcijskaKuca produkcijskaKuca) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement("UPDATE PRODUKCIJSKA_KUCA " +
                    "SET NAZIV = ?, GODINA_OSNUTKA = ?, SJEDISTE = ? " +
                    "WHERE ID = ?");
            statement.setString(1, produkcijskaKuca.getNaziv());
            statement.setInt(2, produkcijskaKuca.getGodinaOsnutka());
            statement.setLong(3, produkcijskaKuca.getSjediste().getId());
            statement.setLong(4, produkcijskaKuca.getIdProdukcijskeKuce());
            statement.executeUpdate();
        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }


    static void obrisiKucu(ProdukcijskaKuca produkcijskaKuca) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement(
                    "DELETE FROM PRODUKCIJSKA_KUCA WHERE ID = ?");
            statement.setLong(1, produkcijskaKuca.getIdProdukcijskeKuce());
            statement.executeUpdate();
        } catch (org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException e) {
        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }

    public static void dodajKucu(ProdukcijskaKuca produkcijskaKuca) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement stmt = veza.prepareStatement(
                    "INSERT INTO PRODUKCIJSKA_KUCA (NAZIV, GODINA_OSNUTKA, SJEDISTE) " +
                            "VALUES(?, ?, ?)");
            stmt.setString(1, produkcijskaKuca.getNaziv());
            stmt.setInt(2, produkcijskaKuca.getGodinaOsnutka());
            stmt.setLong(3, produkcijskaKuca.getSjediste().getId());

            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }


    static void updateKorisnik(Korisnik korisnik) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement("UPDATE KORISNIK " +
                    "SET  LOZINKA = ?, IME = ?, PREZIME = ?, ROLA = ? " +
                    "WHERE ID_KORISNIKA = ?");
            statement.setString(1, korisnik.getLozinka());
            statement.setString(2, korisnik.getIme());
            statement.setString(3, korisnik.getPrezime());
            statement.setLong(4, korisnik.getRola().getIdRole());
            statement.setString(5, korisnik.getIdKorisnika());
            statement.executeUpdate();
        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }


    static void obrisiKorisnika(Korisnik korisnik) {
        try (Connection veza = spajanjeNaBazu()) {
            if ("admin".equals(korisnik.getIdKorisnika())) {
                throw new IOException("Admin users cannot be deleted.");
            }
            PreparedStatement statement = veza.prepareStatement(
                    "DELETE FROM KORISNIK WHERE ID_KORISNIKA = ?");
            statement.setString(1, korisnik.getIdKorisnika());
            statement.executeUpdate();
        } catch (org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException e) {
        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }

    public static void dodajKorisnika(Korisnik korisnik) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement stmt = veza.prepareStatement(
                    "INSERT INTO KORISNIK (ID_KORISNIKA, LOZINKA, IME, PREZIME, ROLA) " +
                            "VALUES(?, ?, ?, ?, ?)");
            stmt.setString(1, korisnik.getIdKorisnika());
            stmt.setString(2, korisnik.getLozinka());
            stmt.setString(3, korisnik.getIme());
            stmt.setString(4, korisnik.getPrezime());
            stmt.setLong(5, korisnik.getRola().getIdRole());

            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    static void updateRolu(Rola rola) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement("UPDATE ROLA " +
                    "SET NAZIV_ROLE = ?, OPIS = ?" +
                    "WHERE ID = ?");
            statement.setString(1, rola.getNazivRole());
            statement.setString(2, rola.getOpisRole());
            statement.setLong(3, rola.getIdRole());
            statement.executeUpdate();
        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }

    static void obrisiRolu(Rola rola) {
        try (Connection veza = spajanjeNaBazu()) {
            if ("admin".equals(rola.getIdRole())) {
                throw new IOException("Admin users cannot be deleted.");
            }
            PreparedStatement statement = veza.prepareStatement(
                    "DELETE FROM ROLA WHERE ID = ?");
            statement.setLong(1, rola.getIdRole());
            statement.executeUpdate();
        } catch (org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException e) {
        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }

    public static void dodajRolu(Rola rola) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement stmt = veza.prepareStatement(
                    "INSERT INTO ROLA (NAZIV_ROLE, OPIS) " +
                            "VALUES(?, ?)");
            stmt.setString(1, rola.getNazivRole());
            stmt.setString(2, rola.getOpisRole());


            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }


    static void updateProjekt(Projekt projekt) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement("UPDATE PROJEKT " +
                    "SET PRODUKCIJSKA_KUCA = ?, BROJ_NOMINACIJA = ?, BROJ_NAGRADA = ?, FINANCIJE = ?, DISTRIBUCIJA = ?, POCETAK_RADA = ?" +
                    "WHERE ID = ?");
            statement.setLong(1, projekt.getProdukcijskaKuca().getIdProdukcijskeKuce());
            statement.setInt(2, projekt.getBrojNominacija());
            statement.setInt(3, projekt.getBrojNagrada());
            statement.setLong(4, projekt.getFinancije().getIdFinancije());
            statement.setLong(5, projekt.getDistribucija().getIdDistribucije());
            statement.setDate(6, Date.valueOf(projekt.getPocetakRada()));
            statement.setLong(7, projekt.getIdProjekta());
            statement.executeUpdate();
        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }

    static void obrisiProjekt(Projekt projekt) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement(
                    "DELETE FROM PROJEKT WHERE ID = ?");
            statement.setLong(1, projekt.getIdProjekta());
            statement.executeUpdate();
        } catch (org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException e) {
        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }

    public static void dodajProjekt(Projekt projekt) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement stmt = veza.prepareStatement(
                    "INSERT INTO PROJEKT (PRODUKCIJSKA_KUCA, BROJ_NOMINACIJA, BROJ_NAGRADA, FINANCIJE, DISTRIBUCIJA, POCETAK_RADA) " +
                            "VALUES(?, ?, ?, ?, ?, ?)");
            stmt.setLong(1, projekt.getProdukcijskaKuca().getIdProdukcijskeKuce());
            stmt.setInt(2, projekt.getBrojNominacija());
            stmt.setInt(3, projekt.getBrojNagrada());
            stmt.setLong(4, projekt.getFinancije().getIdFinancije());
            stmt.setLong(5, projekt.getDistribucija().getIdDistribucije());
            stmt.setDate(6, Date.valueOf(projekt.getPocetakRada()));

            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }


    static void updateScena(Scena scena) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement("UPDATE SCENA " +
                    "SET POCETAK_SNIMANJA = ?, KRAJ_SNIMANJA = ?, LOKACIJA_SNIMANJA = ?, PROJEKT = ?, PRODUCENT = ?" +
                    "WHERE ID = ?");
            statement.setTimestamp(1, Timestamp.valueOf(scena.getPocetakSnimanja()));
            statement.setTimestamp(2, Timestamp.valueOf(scena.getKrajSnimanja()));
            statement.setLong(3, scena.getLokacija().getIdLokacije());
            statement.setLong(4, scena.getProjekt().getIdProjekta());
            statement.setLong(5, scena.getProducent().getIdOsobe());
            statement.setLong(6, scena.getIdScene());
            statement.executeUpdate();
        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }

    static void obrisiScenu(Scena scena) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement(
                    "DELETE FROM SCENA WHERE ID = ?");
            statement.setLong(1, scena.getIdScene());
            statement.executeUpdate();
        } catch (org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException e) {
        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }

    public static void dodajScenu(Scena scena) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement stmt = veza.prepareStatement(
                    "INSERT INTO SCENA (POCETAK_SNIMANJA, KRAJ_SNIMANJA, LOKACIJA_SNIMANJA, PROJEKT, PRODUCENT) " +
                            "VALUES(?, ?, ?, ?, ?)");
            stmt.setTimestamp(1, Timestamp.valueOf(scena.getPocetakSnimanja()));
            stmt.setTimestamp(2, Timestamp.valueOf(scena.getKrajSnimanja()));
            stmt.setLong(3, scena.getLokacija().getIdLokacije());
            stmt.setLong(4, scena.getProjekt().getIdProjekta());
            stmt.setLong(5, scena.getProducent().getIdOsobe());

            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }


    static void updateDistribucija(Distribucija distribucija) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement("UPDATE DISTRIBUCIJA " +
                    "SET POCETAK_PROJEKTA = ?, KRAJ_PROJEKTA = ?, IZLAZAK_FILMA = ?" +
                    "WHERE ID = ?");

            // Convert LocalDate to java.sql.Date
            Date pocetakProjekta = java.sql.Date.valueOf((LocalDate) distribucija.getPocetakProjekta());
            Date krajProjekta = java.sql.Date.valueOf((LocalDate) distribucija.getKrajProjekta());
            Date izlazakFilma = java.sql.Date.valueOf((LocalDate) distribucija.getIzlazakFilma());

            statement.setDate(1, pocetakProjekta);
            statement.setDate(2, krajProjekta);
            statement.setDate(3, izlazakFilma);
            statement.setLong(4, distribucija.getIdDistribucije());

            statement.executeUpdate();
        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }


    static void obrisiDistribuciju(Distribucija distribucija) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement(
                    "DELETE FROM DISTRIBUCIJA WHERE ID = ?");
            statement.setLong(1, distribucija.getIdDistribucije());
            statement.executeUpdate();
        } catch (org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException e) {
        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }

    public static void dodajDistribuciju(Distribucija distribucija) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement stmt = veza.prepareStatement(
                    "INSERT INTO DISTRIBUCIJA (POCETAK_PROJEKTA, KRAJ_PROJEKTA, IZLAZAK_FILMA) " +
                            "VALUES(?, ?, ?)");
            Date pocetakProjekta = java.sql.Date.valueOf((LocalDate) distribucija.getPocetakProjekta());
            Date krajProjekta = java.sql.Date.valueOf((LocalDate) distribucija.getKrajProjekta());
            Date izlazakFilma = java.sql.Date.valueOf((LocalDate) distribucija.getIzlazakFilma());

            stmt.setDate(1, pocetakProjekta);
            stmt.setDate(2, krajProjekta);
            stmt.setDate(3, izlazakFilma);

            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }


    static void updateFinancija(Financije financije) throws CijenaException {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement("UPDATE FINANCIJE " +
                    "SET PRORACUN = ?, TROSKOVI = ?, PRIHODI = ? " +
                    "WHERE ID = ?");

            Cijena proracun = financije.getProracun();
            Cijena troskovi = financije.getTroskovi();
            Cijena prihodi = financije.getPrihodi();

            statement.setBigDecimal(1, proracun.cijena());
            statement.setBigDecimal(2, troskovi.cijena());
            statement.setBigDecimal(3, prihodi.cijena());
            statement.setLong(4, financije.getIdFinancije());

            statement.executeUpdate();
        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }


    static void obrisiFinanciju(Financije financije) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement statement = veza.prepareStatement(
                    "DELETE FROM FINANCIJE WHERE ID = ?");
            statement.setLong(1, financije.getIdFinancije());
            statement.executeUpdate();
        } catch (org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException e) {
        } catch (SQLException | IOException e) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka);
            e.printStackTrace();
        }
    }

    public static void dodajFinanciju(Financije financije) {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement stmt = veza.prepareStatement(
                    "INSERT INTO FINANCIJE (PRORACUN , TROSKOVI, PRIHODI) " +
                            "VALUES(?, ?, ?)");
            Cijena proracun = financije.getProracun();
            Cijena troskovi = financije.getTroskovi();
            Cijena prihodi = financije.getPrihodi();

            stmt.setBigDecimal(1, proracun.cijena());
            stmt.setBigDecimal(2, troskovi.cijena());
            stmt.setBigDecimal(3, prihodi.cijena());

            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }


}
