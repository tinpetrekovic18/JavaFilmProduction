package upravljanje.filmskom.produkcijom.projekt.entiteti;

import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.io.*;

public class ProdukcijskaKuca implements ZapisPromjena{

    Long idProdukcijskeKuce;

    String naziv;

    Integer godinaOsnutka;

    Adresa sjediste;

    public ProdukcijskaKuca(Long idProdukcijskeKuce, String naziv, Integer godinaOsnutka, Adresa sjediste) {
        this.idProdukcijskeKuce = idProdukcijskeKuce;
        this.naziv = naziv;
        this.godinaOsnutka = godinaOsnutka;
        this.sjediste = sjediste;
    }

    public Long getIdProdukcijskeKuce() {
        return idProdukcijskeKuce;
    }

    public void setIdProdukcijskeKuce(Long idProdukcijskeKuce) {
        this.idProdukcijskeKuce = idProdukcijskeKuce;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Integer getGodinaOsnutka() {
        return godinaOsnutka;
    }

    public void setGodinaOsnutka(Integer godinaOsnutka) {
        this.godinaOsnutka = godinaOsnutka;
    }

    public Adresa getSjediste() {
        return sjediste;
    }

    public void setSjediste(Adresa sjediste) {
        this.sjediste = sjediste;
    }

    @Override
    public String toString() {
        return "{" +
                "idProdukcijskeKuce=" + idProdukcijskeKuce +
                ", naziv=" + naziv +
                ", godinaOsnutka='" + godinaOsnutka + '\'' +
                ", sjediste=" + sjediste +
                '}';
    }

    @Override
    public void Serijalizacija(String filepath) {
        try {
            ObjectOutputStream zapisivanje = new ObjectOutputStream(new FileOutputStream(filepath));
            ProdukcijskaKuca produkcijskaKuca = new ProdukcijskaKuca(getIdProdukcijskeKuce(), getNaziv(), getGodinaOsnutka(), getSjediste());
            zapisivanje.writeObject(produkcijskaKuca);
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
            pw.println(getIdProdukcijskeKuce());
            pw.println(getNaziv());
            pw.println(getGodinaOsnutka());
            pw.println(getSjediste());
        }
        catch(IOException e){
            e.printStackTrace();
            String poruka="Dogodila se pogreška kod zapisivanja promjene!";
            System.out.println(poruka);
            Main.logger.info(poruka);
        }
    }

    public ProdukcijskaKuca Deserijalizacija(String filepath) {
        try (ObjectInputStream citanje = new ObjectInputStream(new FileInputStream(filepath))) {
            Object obj = citanje.readObject();
            if (obj instanceof ProdukcijskaKuca) {
                return (ProdukcijskaKuca) obj;
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
