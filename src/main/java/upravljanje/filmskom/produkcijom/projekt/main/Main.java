package upravljanje.filmskom.produkcijom.projekt.main;

import javafx.scene.chart.PieChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upravljanje.filmskom.produkcijom.projekt.Aplikacija;
import upravljanje.filmskom.produkcijom.projekt.controllers.PocetniLoginController;
import upravljanje.filmskom.produkcijom.projekt.database.Database;
import upravljanje.filmskom.produkcijom.projekt.entiteti.*;
import upravljanje.filmskom.produkcijom.projekt.threads.DeserijalizacijaThread;
import upravljanje.filmskom.produkcijom.projekt.threads.SerijalizacijaThread;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class Main {

    private static final String KORISNICI_DAT="C:\\Java-faks\\#Projekt\\Projekt\\dat\\korisnici.txt";

    private static final String PROMJENE_DAT="C:\\Java-faks\\#Projekt\\Projekt\\dat\\promjenePodataka.dat";

    private static final String PROMJENE_TXT="C:\\Java-faks\\#Projekt\\Projekt\\dat\\promjenePodataka.txt";

    private static final String PROMJENE_CLAN_EKIPE="C:\\Java-faks\\#Projekt\\Projekt\\dat\\promjeneClanEkipe.dat";


    private static final String s="C:\\Java-faks\\#Projekt\\Projekt\\dat\\promjeneClanEkipe.dat";


    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        Scanner inputScanner = new Scanner(System.in);


        String a="111";


        Projekt projekt=new Projekt(Long.valueOf(0), null, null, 0, null, null, null);

        ClanEkipe clanEkipe1 = new ClanEkipe(Long.valueOf(3), "Ivan", "Horvat", "012345678910", 22, "Kamerman", projekt );
        ClanEkipe clanEkipe2 = new ClanEkipe(Long.valueOf(3), "Ivan", "Horvat", "012345678910", 23, "Kamerman", projekt);

        String p1=clanEkipe1.toString();
        String p2=clanEkipe2.toString();

        Promjena<String> promjenaStr = new Promjena<>(p1,p2 , "Admin", LocalDateTime.now());
        Promjena<String> promjenaStr1 = new Promjena<>(p2, p1, "Admin", LocalDateTime.now());

        Promjena<String> en = new Promjena<>("ah","jah" , "Admin", LocalDateTime.now());
        Promjena<String> dva = new Promjena<>("pah", "mah", "Admin", LocalDateTime.now());



       /* List<Promjena<String>> listaPromjenaStr = Deserijalizacija();
        listaPromjenaStr.add(promjenaStr);
        listaPromjenaStr.add(promjenaStr1);

        Serijalizacija(listaPromjenaStr);
        Deserijalizacija();

        List<Promjena<ClanEkipe>> listica = Deserijalizacija();
        for (Promjena<ClanEkipe> clan : listica) {
            System.out.println(clan.toString());
        }*/
        try(Connection conn=Database.spajanjeNaBazu()){
            System.out.println("YIPPIEEEEEEEEEEEEE!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
       // List<Glumac> listicka=Database.dohvatiGlumce();

        List<Producent> adrese=Database.dohvatiProducente();

        for(Producent korisnik:adrese){
            System.out.println(korisnik.getIdOsobe());
            System.out.println(korisnik.getIme());
            System.out.println(korisnik.getPrezime());
            System.out.println(korisnik.getOIB());
            System.out.println(korisnik.getDob());
            System.out.println(korisnik.getUmjetnickoIme());
            System.out.println(korisnik.getBrojNominacija());
            System.out.println(korisnik.getBrojNagrada());
            System.out.println(korisnik.getTrenutniProjekt().getIdProjekta());
            System.out.println(korisnik.getPocetakRada());



            System.out.println("\n");
        }


    }

    public static String getDateTimeFormatted(String start){
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        SimpleDateFormat desiredFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String formattedDate = null;
        try{
            Date date = originalFormat.parse(start);
            formattedDate = desiredFormat.format(date);
        }catch (ParseException e){
            Aplikacija.logger.error(e.getMessage(), e);
        }
        return formattedDate;
    }


    public static void zapisPromjene(Object staraVrijednost, Object novaVrijednost) {
        Promjena<Object> promjena = new Promjena<>(staraVrijednost, novaVrijednost, PocetniLoginController.getRolica(), LocalDateTime.now());


        List<Promjena<Object>> listaPromjena = new ArrayList<>();


        DeserijalizacijaThread<Object> deserijalizacijaThread = new DeserijalizacijaThread<>(listaPromjena);
        Thread thread = new Thread(deserijalizacijaThread);
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        listaPromjena = deserijalizacijaThread.getListaPromjena();


        listaPromjena.add(promjena);


        SerijalizacijaThread<Object> serijalizacijaThread = new SerijalizacijaThread<>(listaPromjena);
        Thread sthread = new Thread(serijalizacijaThread);
        sthread.start();


        try {
            sthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static <T> List<Promjena<T>> Deserijalizacija() {
        List<Promjena<T>> listaPromjena = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("C:\\Java-faks\\#Projekt\\Projekt\\dat\\promjeneClanEkipe.dat"))) {

            listaPromjena = (List<Promjena<T>>) ois.readObject();
        } catch (FileNotFoundException e) {

            System.out.println("File not found. Initializing an empty list.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPromjena;
    }

    public static <T> void Serijalizacija(List<Promjena<T>> listaPromjena) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PROMJENE_CLAN_EKIPE))) {

            oos.writeObject(listaPromjena);
            System.out.println("Objects serialized and saved to " + PROMJENE_CLAN_EKIPE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loginTest(String idL, Integer hashlozinkaL){
        Boolean neke=false;

        while(!neke) {

            try (BufferedReader citanje = new BufferedReader(new FileReader(KORISNICI_DAT))) {
                String line;
                while ((line = citanje.readLine()) != null) {
                    String id = line;
                    String lozinka = citanje.readLine();
                    String rola= citanje.readLine();
                    Integer hashlozinka=lozinka.hashCode();
                    if (idL.equals(id) && hashlozinkaL.equals(hashlozinka)) {
                        if(rola.equals("admin")) System.out.println("Uspješna prijava administratora!");
                        else System.out.println("Uspješna prijava korisnika!");
                        neke = true;
                        break;
                    }
                }
                if (!neke) {
                    System.out.println("Korisnički račun ne postoji!");
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.info("Greška kod prijave");
            }

        }
    }

    public static void loggerTest(){
        Integer broj;
        try{
            broj=2/0;
        }
        catch(ArithmeticException e){
            String poruka="Greška kod računanja";
            logger.info(poruka);
        }
    }




}
