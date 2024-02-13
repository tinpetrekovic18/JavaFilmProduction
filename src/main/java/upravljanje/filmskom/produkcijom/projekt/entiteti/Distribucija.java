package upravljanje.filmskom.produkcijom.projekt.entiteti;

import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.Objects;

public non-sealed class Distribucija<T extends Temporal> implements Trajanje, ZapisPromjena, Serializable{

    private Long idDistribucije;
    private T pocetakProjekta;

    private T krajProjekta;

    private T izlazakFilma;

    public Distribucija(Long idDistribucije, T pocetakProjekta, T krajProjekta, T izlazakFilma) {
        this.idDistribucije = idDistribucije;
        this.pocetakProjekta = pocetakProjekta;
        this.krajProjekta = krajProjekta;
        this.izlazakFilma = izlazakFilma;
    }


    public Long getIdDistribucije() {
        return idDistribucije;
    }

    public void setIdDistribucije(Long idDistribucije) {
        this.idDistribucije = idDistribucije;
    }

    public T getPocetakProjekta() {
        return pocetakProjekta;
    }

    public void setPocetakProjekta(T pocetakProjekta) {
        this.pocetakProjekta = pocetakProjekta;
    }

    public T getKrajProjekta() {
        return krajProjekta;
    }

    public void setKrajProjekta(T krajProjekta) {
        this.krajProjekta = krajProjekta;
    }

    public T getIzlazakFilma() {
        return izlazakFilma;
    }

    public void setIzlazakFilma(T izlazakFilma) {
        this.izlazakFilma = izlazakFilma;
    }

    @Override
    public String toString() {
        return "{" +
                "idDistribucije=" + idDistribucije +
                ", pocetakProjekta=" + pocetakProjekta +
                ", krajProjekta='" + krajProjekta + '\'' +
                ", izlazakFilma=" + izlazakFilma +
                '}';
    }

    @Override
    public Long satiSnimanja() {
        Duration duration= Duration.between(pocetakProjekta, krajProjekta);
        Long sati=duration.toHours();
        return sati;
    }
    @Override
    public void Serijalizacija(String filepath) {
        try {
            ObjectOutputStream zapisivanje = new ObjectOutputStream(new FileOutputStream(filepath));
            Distribucija distribucija = new Distribucija(getIdDistribucije(), getPocetakProjekta(), getKrajProjekta(),getIzlazakFilma());
            zapisivanje.writeObject(distribucija);
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
            pw.println(getIdDistribucije());
            pw.println(getPocetakProjekta());
            pw.println(getKrajProjekta());
            pw.println(getIzlazakFilma());

        }
        catch(IOException e){
            e.printStackTrace();
            String poruka="Dogodila se pogreška kod zapisivanja promjene!";
            System.out.println(poruka);
            Main.logger.info(poruka);
        }
    }
    public Distribucija Deserijalizacija(String filepath) {
        try (ObjectInputStream citanje = new ObjectInputStream(new FileInputStream(filepath))) {
            Object obj = citanje.readObject();
            if (obj instanceof Distribucija<?>) {
                return (Distribucija) obj;
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
