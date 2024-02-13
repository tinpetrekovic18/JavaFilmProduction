package upravljanje.filmskom.produkcijom.projekt.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upravljanje.filmskom.produkcijom.projekt.entiteti.ClanEkipe;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Promjena;
import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class PromjeneSerijalizacija {

    private static final String PROMJENE_CLAN_EKIPE="C:\\Java-faks\\#Projekt\\Projekt\\dat\\promjeneClanEkipe.dat";

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static <T> List<Promjena<T>> Deserijalizacija() {
        List<Promjena<T>> listaPromjena = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PROMJENE_CLAN_EKIPE))) {
            // Read the list from the file
            listaPromjena = (List<Promjena<T>>) ois.readObject();
        } catch (FileNotFoundException e) {
            String poruka="Datoteka nije pronađena!";
            System.out.println(poruka);
            logger.error(poruka);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Greška kod deserijalizacije");
        }
        return listaPromjena;
    }

    public static <T> void Serijalizacija(List<Promjena<T>> listaPromjena) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PROMJENE_CLAN_EKIPE))) {
            // Write the list to the file
            oos.writeObject(listaPromjena);
            System.out.println("Objekti klase serijalizirani i spremljeni u" + PROMJENE_CLAN_EKIPE);
        } catch (Exception e) {
            e.printStackTrace();
            String poruka="Greška kod serijalizacije";
            System.out.println(poruka);
            logger.error(poruka);
        }
    }
}
