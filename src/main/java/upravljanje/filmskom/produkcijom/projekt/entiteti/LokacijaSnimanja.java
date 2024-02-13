package upravljanje.filmskom.produkcijom.projekt.entiteti;

import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.io.*;
import java.time.LocalDateTime;

public class LokacijaSnimanja implements ZapisPromjena{

    Long idLokacije;

    Adresa lokacija;

    Projekt projekt;

    LocalDateTime rezerviranoOd;

    LocalDateTime rezerviranoDo;


    public LokacijaSnimanja(Long idLokacije, Adresa lokacija, Projekt projekt, LocalDateTime rezerviranoOd, LocalDateTime rezerviranoDo) {
        this.idLokacije = idLokacije;
        this.lokacija = lokacija;
        this.projekt = projekt;
        this.rezerviranoOd = rezerviranoOd;
        this.rezerviranoDo = rezerviranoDo;
    }

    public Long getIdLokacije() {
        return idLokacije;
    }

    public void setIdLokacije(Long idLokacije) {
        this.idLokacije = idLokacije;
    }

    public Adresa getLokacija() {
        return lokacija;
    }

    public void setLokacija(Adresa lokacija) {
        this.lokacija = lokacija;
    }

    public Projekt getProjekt() {
        return projekt;
    }

    public void setProjekt(Projekt projekt) {
        this.projekt = projekt;
    }

    public LocalDateTime getRezerviranoOd() {
        return rezerviranoOd;
    }

    public void setRezerviranoOd(LocalDateTime rezerviranoOd) {
        this.rezerviranoOd = rezerviranoOd;
    }

    public LocalDateTime getRezerviranoDo() {
        return rezerviranoDo;
    }

    public void setRezerviranoDo(LocalDateTime rezerviranoDo) {
        this.rezerviranoDo = rezerviranoDo;
    }
    @Override
    public void Serijalizacija(String filepath) {
        try {
            ObjectOutputStream zapisivanje = new ObjectOutputStream(new FileOutputStream(filepath));
            LokacijaSnimanja lokacijaSnimanja = new LokacijaSnimanja(getIdLokacije(), getLokacija(), getProjekt(), getRezerviranoOd(), getRezerviranoDo());
            zapisivanje.writeObject(lokacijaSnimanja);
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
    public String toString() {
        return "{" +
                "idLokacije=" + idLokacije +
                ", lokacija=" + lokacija +
                ", projekt='" + projekt + '\'' +
                ", rezerviranoOd=" + rezerviranoOd +
                ", rezerviranoDo=" + rezerviranoDo +
                '}';
    }

    @Override
    public void ZapisTXT(String filepath) {
        File adreseFile=new File(filepath);
        try(PrintWriter pw=new PrintWriter(adreseFile)){
            pw.println(getLokacija());
            pw.println(getLokacija());
            pw.println(getProjekt());
            pw.println(getRezerviranoOd());
            pw.println(getRezerviranoDo());

        }
        catch(IOException e){
            e.printStackTrace();
            String poruka="Dogodila se pogreška kod zapisivanja promjene!";
            System.out.println(poruka);
            Main.logger.info(poruka);
        }
    }
    public LokacijaSnimanja Deserijalizacija(String filepath) {
        try (ObjectInputStream citanje = new ObjectInputStream(new FileInputStream(filepath))) {
            Object obj = citanje.readObject();
            if (obj instanceof LokacijaSnimanja) {
                return (LokacijaSnimanja) obj;
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
