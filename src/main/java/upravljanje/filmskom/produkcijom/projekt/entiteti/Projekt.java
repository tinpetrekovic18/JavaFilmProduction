package upravljanje.filmskom.produkcijom.projekt.entiteti;

import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class Projekt implements Popularnost, ZapisPromjena{

    Long idProjekta;

    ProdukcijskaKuca produkcijskaKuca;


    Integer brojNominacija;

    Integer brojNagrada;

    Financije financije;

    Distribucija distribucija;

    LocalDate pocetakRada;

    public Projekt(Long idProjekta, ProdukcijskaKuca produkcijskaKuca, Integer brojNominacija, Integer brojNagrada, Financije financije, Distribucija distribucija, LocalDate pocetakRada) {
        this.idProjekta = idProjekta;
        this.produkcijskaKuca = produkcijskaKuca;
        this.brojNominacija = brojNominacija;
        this.brojNagrada = brojNagrada;
        this.financije = financije;
        this.distribucija = distribucija;
        this.pocetakRada = pocetakRada;
    }

    public Long getIdProjekta() {
        return idProjekta;
    }

    public void setIdProjekta(Long idProjekta) {
        this.idProjekta = idProjekta;
    }

    public ProdukcijskaKuca getProdukcijskaKuca() {
        return produkcijskaKuca;
    }

    public void setProdukcijskaKuca(ProdukcijskaKuca produkcijskaKuca) {
        this.produkcijskaKuca = produkcijskaKuca;
    }

    public Financije getFinancije() {
        return financije;
    }

    public void setFinancije(Financije financije) {
        this.financije = financije;
    }

    public Distribucija getDistribucija() {
        return distribucija;
    }

    public void setDistribucija(Distribucija distribucija) {
        this.distribucija = distribucija;
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


    @Override
    public String toString() {
        return idProjekta + " " + produkcijskaKuca.getIdProdukcijskeKuce() + " " +brojNominacija + " " + brojNagrada
                + " " + financije.getIdFinancije() + " " + distribucija.getIdDistribucije() + " " + pocetakRada;
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
            Projekt projekt = new Projekt(getIdProjekta(), getProdukcijskaKuca(), getBrojNominacija(), getBrojNagrada(), getFinancije(), getDistribucija(), getPocetakRada());
            zapisivanje.writeObject(projekt);
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
            pw.println(getIdProjekta());
            pw.println(getProdukcijskaKuca());
            pw.println(getBrojNominacija());
            pw.println(getBrojNagrada());
            pw.println(getFinancije());
            pw.println(getDistribucija());
            pw.println(getPocetakRada());
        }
        catch(IOException e){
            e.printStackTrace();
            String poruka="Dogodila se pogreška kod zapisivanja promjene!";
            System.out.println(poruka);
            Main.logger.info(poruka);
        }
    }
    public Projekt Deserijalizacija(String filepath) {
        try (ObjectInputStream citanje = new ObjectInputStream(new FileInputStream(filepath))) {
            Object obj = citanje.readObject();
            if (obj instanceof Projekt) {
                return (Projekt) obj;
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
