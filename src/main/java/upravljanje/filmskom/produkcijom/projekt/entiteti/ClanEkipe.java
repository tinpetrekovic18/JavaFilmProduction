package upravljanje.filmskom.produkcijom.projekt.entiteti;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.io.*;

public class ClanEkipe extends Osoba implements ZapisPromjena{

    private static final Logger logger = LoggerFactory.getLogger(ClanEkipe.class);
    String opisPosla;

    Projekt trenutniProjekt;


    public ClanEkipe(Long idOsobe, String ime, String prezime, String OIB, Integer dob, String opisPosla, Projekt trenutniProjekt) {
        super(idOsobe, ime, prezime, OIB, dob);
        this.opisPosla = opisPosla;
        this.trenutniProjekt=trenutniProjekt;
    }

    public String getOpisPosla() {
        return opisPosla;
    }

    public void setOpisPosla(String opisPosla) {
        this.opisPosla = opisPosla;
    }

    public Projekt getTrenutniProjekt() {
        return trenutniProjekt;
    }

    public void setTrenutniProjekt(Projekt trenutniProjekt) {
        this.trenutniProjekt = trenutniProjekt;
    }

    @Override
    public void Serijalizacija(String filepath) {
        try (ObjectOutputStream zapisivanje = new ObjectOutputStream(new FileOutputStream(filepath))) {
            ClanEkipe clanEkipe = new ClanEkipe(getIdOsobe(), getIme(), getPrezime(), getOIB(), getDob(), getOpisPosla(), getTrenutniProjekt());
            zapisivanje.writeObject(clanEkipe);
        } catch (IOException e) {
            logger.error("Dogodila se pogreška kod serijalizacije promjene!", e);
        }
    }

    @Override
    public void ZapisTXT(String filepath) {
        File adreseFile=new File(filepath);
        try(PrintWriter pw=new PrintWriter(adreseFile)){
            pw.println(getIdOsobe());
            pw.println(getIme());
            pw.println(getPrezime());
            pw.println(getOIB());
            pw.println(getDob());
            pw.println(getOpisPosla());

        }
        catch(IOException e){
            e.printStackTrace();
            String poruka="Dogodila se pogreška kod zapisivanja promjene!";
            System.out.println(poruka);
            Main.logger.info(poruka);
        }
    }
    //idOsobe + ", " + ime + ", " + prezime + ", " + OIB + ", " + dob  + ", " + opisPosla +
    @Override
    public String toString() {
        return "{" +
                "idOsobe=" + idOsobe +
                ", imeOsobe=" + ime +
                ", prezimeOsobe='" + prezime + '\'' +
                ", OIBOsobe=" + OIB +
                ", dobOsobe=" + dob +
                ", opisPosla=" + opisPosla +
                '}';
    }

    public static ClanEkipe Deserijalizacija(String filepath) {
        ClanEkipe clanEkipe = null;

        try (ObjectInputStream citanje = new ObjectInputStream(new FileInputStream(filepath))) {
            clanEkipe = (ClanEkipe) citanje.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Dogodila se pogreška kod deserijalizacije promjene!", e);
        }

        return clanEkipe;
    }
}
