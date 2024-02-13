package upravljanje.filmskom.produkcijom.projekt.entiteti;

import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Set;

public class Glumac extends Osoba implements Popularnost, ZapisPromjena{

    String umjetnickoIme;

    Integer brojNominacija;

    Integer brojNagrada;

    Projekt trenutniProjekt;

    LocalDate pocetakRada;

    public Glumac(Long idOsobe, String ime, String prezime, String OIB, Integer dob, String umjetnickoIme, Integer brojNominacija, Integer brojNagrada,Projekt trenutniProjekt, LocalDate pocetakRada) {
        super(idOsobe, ime, prezime, OIB, dob);
        this.umjetnickoIme = umjetnickoIme;
        this.brojNominacija = brojNominacija;
        this.brojNagrada = brojNagrada;
        this.pocetakRada = pocetakRada;
        this.trenutniProjekt=trenutniProjekt;
    }

    public String getUmjetnickoIme() {
        return umjetnickoIme;
    }

    public void setUmjetnickoIme(String umjetnickoIme) {
        this.umjetnickoIme = umjetnickoIme;
    }

    public Integer getBrojNominacija() {
        return brojNominacija;
    }

    public void setBrojNominacija(Integer brojNominacija) {
        this.brojNominacija = brojNominacija;
    }

    public Integer getBrojNagrada() {
        return brojNagrada;
    }

    public void setBrojNagrada(Integer brojNagrada) {
        this.brojNagrada = brojNagrada;
    }

    public LocalDate getPocetakRada() {
        return pocetakRada;
    }

    public void setPocetakRada(LocalDate pocetakRada) {
        this.pocetakRada = pocetakRada;
    }

    public Projekt getTrenutniProjekt() {
        return trenutniProjekt;
    }

    public void setTrenutniProjekt(Projekt trenutniProjekt) {
        this.trenutniProjekt = trenutniProjekt;
    }

    @Override
    public String toString() {
        return "{" +
                "idOsobe=" + idOsobe +
                ", ime=" + ime +
                ", prezime='" + prezime + '\'' +
                ", OIB=" + OIB +
                ", dob=" + dob +
                ", umjetnickoIme=" + umjetnickoIme +
                ", brojNominacija=" + brojNominacija +
                ", brojNagrada=" + brojNagrada +
                ", brojNagrada=" + trenutniProjekt +
                ", pocetakRada=" + pocetakRada +
                '}';
    }

    @Override
    public Integer popularnost() {
        Integer pop=(brojNagrada+brojNominacija)/10;
        return pop;
    }

    @Override
    public Integer godineRada() {
        Period period=Period.between(pocetakRada, LocalDate.now());
        Integer god=period.getYears();
        return god;
    }
    @Override
    public void Serijalizacija(String filepath) {
        try {
            ObjectOutputStream zapisivanje = new ObjectOutputStream(new FileOutputStream(filepath));
            Glumac glumac = new Glumac(getIdOsobe(), getIme(), getPrezime(), getOIB(), getDob(), getUmjetnickoIme(), getBrojNominacija(), getBrojNagrada(),getTrenutniProjekt(), getPocetakRada());
            zapisivanje.writeObject(glumac);
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
            pw.println(getIdOsobe());
            pw.println(getIme());
            pw.println(getPrezime());
            pw.println(getOIB());
            pw.println(getDob());
            pw.println(getUmjetnickoIme());
            pw.println(getBrojNominacija());
            pw.println(getBrojNagrada());
            pw.println(getTrenutniProjekt());
            pw.println(getPocetakRada());

        }
        catch(IOException e){
            e.printStackTrace();
            String poruka="Dogodila se pogreška kod zapisivanja promjene!";
            System.out.println(poruka);
            Main.logger.info(poruka);
        }
    }
    public Glumac Deserijalizacija(String filepath) {
        try (ObjectInputStream citanje = new ObjectInputStream(new FileInputStream(filepath))) {
            Object obj = citanje.readObject();
            if (obj instanceof Glumac) {
                return (Glumac) obj;
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
