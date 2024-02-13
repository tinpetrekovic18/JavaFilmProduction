package upravljanje.filmskom.produkcijom.projekt.entiteti;

import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public non-sealed class Scena implements Trajanje, ZapisPromjena{

    Long idScene;

    LocalDateTime pocetakSnimanja;

    LocalDateTime krajSnimanja;

    LokacijaSnimanja lokacija;

    Projekt projekt;

    Producent producent;


    public Scena(Long idScene, LocalDateTime pocetakSnimanja, LocalDateTime krajSnimanja, LokacijaSnimanja lokacija, Projekt projekt, Producent producent) {
        this.idScene = idScene;
        this.pocetakSnimanja = pocetakSnimanja;
        this.krajSnimanja = krajSnimanja;
        this.lokacija = lokacija;
        this.projekt = projekt;
        this.producent = producent;
    }

    public Long getIdScene() {
        return idScene;
    }

    public void setIdScene(Long idScene) {
        this.idScene = idScene;
    }

    public LocalDateTime getPocetakSnimanja() {
        return pocetakSnimanja;
    }

    public void setPocetakSnimanja(LocalDateTime pocetakSnimanja) {
        this.pocetakSnimanja = pocetakSnimanja;
    }

    public LocalDateTime getKrajSnimanja() {
        return krajSnimanja;
    }

    public void setKrajSnimanja(LocalDateTime krajSnimanja) {
        this.krajSnimanja = krajSnimanja;
    }

    public LokacijaSnimanja getLokacija() {
        return lokacija;
    }

    public void setLokacija(LokacijaSnimanja lokacija) {
        this.lokacija = lokacija;
    }

    public Projekt getProjekt() {
        return projekt;
    }

    public void setProjekt(Projekt projekt) {
        this.projekt = projekt;
    }

    public Producent getProducent() {
        return producent;
    }

    public void setProducent(Producent producent) {
        this.producent = producent;
    }


    /*
        this.idScene = idScene;
        this.pocetakSnimanja = pocetakSnimanja;
        this.krajSnimanja = krajSnimanja;
        this.lokacija = lokacija;
        this.projekt = projekt;
        this.producenti = producenti;
        this.glumci = glumci;
        this.clanoviEkipe = clanoviEkipe;
     */

    @Override
    public String toString() {
        return "Promjena{" +
                "idScene=" + idScene +
                ", pocetakSnimanja=" + pocetakSnimanja +
                ", krajSnimanja='" + krajSnimanja + '\'' +
                ", lokacija=" + lokacija +
                ", projekt=" + projekt +
                ", producent=" + producent +
                '}';
    }

    @Override
    public Long satiSnimanja() {
        Duration duration= Duration.between(pocetakSnimanja, krajSnimanja);
        Long sati=duration.toHours();
        return sati;
    }
    @Override
    public void Serijalizacija(String filepath) {
        try {
            ObjectOutputStream zapisivanje = new ObjectOutputStream(new FileOutputStream(filepath));
            Scena scena = new Scena(getIdScene(), getPocetakSnimanja(), getKrajSnimanja(), getLokacija(), getProjekt(), getProducent());
            zapisivanje.writeObject(scena);
            zapisivanje.close();
        }
        catch (IOException e){
            e.printStackTrace();
            String poruka="Dogodila se pogreška kod serijalizacije promjene!";
            System.out.println(poruka);
            Main.logger.info(poruka);
        }
    }

    @Override
    public void ZapisTXT(String filepath) {
        File adreseFile=new File(filepath);
        try(PrintWriter pw=new PrintWriter(adreseFile)){
            pw.println(getIdScene());
            pw.println(getPocetakSnimanja());
            pw.println(getKrajSnimanja());
            pw.println(getLokacija());
            pw.println(getProjekt());
            pw.println(getProducent());

        }
        catch(IOException e){
            e.printStackTrace();
            String poruka="Dogodila se pogreška kod zapisivanja promjene!";
            System.out.println(poruka);
            Main.logger.info(poruka);
        }
    }


    public Scena Deserijalizacija(String filepath) {
        try (ObjectInputStream citanje = new ObjectInputStream(new FileInputStream(filepath))) {
            Object obj = citanje.readObject();
            if (obj instanceof Scena) {
                return (Scena) obj;
            } else {
                System.out.println("Objekt nije tipa Scena.");
                return null;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            String poruka = "Dogodila se pogreška kod deserijalizacije!";
            System.out.println(poruka);
            Main.logger.info(poruka);
            return null;
        }
    }
}
