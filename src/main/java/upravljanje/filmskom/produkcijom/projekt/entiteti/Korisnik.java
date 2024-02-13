package upravljanje.filmskom.produkcijom.projekt.entiteti;

import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.io.*;

public class Korisnik implements ZapisPromjena, Serializable{

    private String idKorisnika;
    private String lozinka;
    private String ime;
    private String prezime;
    private Rola rola;

    public Korisnik(String idKorisnika, String lozinka, String ime, String prezime, Rola rola) {
        this.idKorisnika = idKorisnika;
        this.lozinka = lozinka;
        this.ime = ime;
        this.prezime = prezime;
        this.rola = rola;
    }

    public String getIdKorisnika() {
        return idKorisnika;
    }

    public void setIdKorisnika(String idKorisnika) {
        this.idKorisnika = idKorisnika;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public Rola getRola() {
        return rola;
    }

    public void setRola(Rola rola) {
        this.rola = rola;
    }

    @Override
    public void Serijalizacija(String filepath) {
        try {
            ObjectOutputStream zapisivanje = new ObjectOutputStream(new FileOutputStream(filepath));
            Korisnik korisnik = new Korisnik(getIdKorisnika(), getLozinka(), getIme(), getPrezime(), getRola());
            zapisivanje.writeObject(korisnik);
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
                "idKorisnika=" + idKorisnika +
                ", lozinka=" + lozinka +
                ", ime='" + ime + '\'' +
                ", prezime=" + prezime +
                ", rola=" + rola +
                '}';
    }

    @Override
    public void ZapisTXT(String filepath) {
        File adreseFile=new File(filepath);
        try(PrintWriter pw=new PrintWriter(adreseFile)){
            pw.println(getIdKorisnika());
            pw.println(getLozinka());
            pw.println(getIme());
            pw.println(getPrezime());
            pw.println(getRola());
        }
        catch(IOException e){
            e.printStackTrace();
            String poruka="Dogodila se pogreška kod zapisivanja promjene!";
            System.out.println(poruka);
            Main.logger.info(poruka);
        }
    }
    public Korisnik Deserijalizacija(String filepath) {
        try (ObjectInputStream citanje = new ObjectInputStream(new FileInputStream(filepath))) {
            Object obj = citanje.readObject();
            if (obj instanceof Korisnik) {
                return (Korisnik) obj;
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
