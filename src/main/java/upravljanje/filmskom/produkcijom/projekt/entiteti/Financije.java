package upravljanje.filmskom.produkcijom.projekt.entiteti;

import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.io.*;

public class Financije implements ZapisPromjena{

    Long idFinancije;

    Cijena proracun;

    Cijena troskovi;

    Cijena prihodi;

    public Financije(Long idFinancije, Cijena proracun, Cijena troskovi, Cijena prihodi) {
        this.idFinancije = idFinancije;
        this.proracun = proracun;
        this.troskovi = troskovi;
        this.prihodi = prihodi;
    }

    public Long getIdFinancije() {
        return idFinancije;
    }

    public void setIdFinancije(Long idFinancije) {
        this.idFinancije = idFinancije;
    }

    public Cijena getProracun() {
        return proracun;
    }

    public void setProracun(Cijena proracun) {
        this.proracun = proracun;
    }

    public Cijena getTroskovi() {
        return troskovi;
    }

    public void setTroskovi(Cijena troskovi) {
        this.troskovi = troskovi;
    }

    public Cijena getPrihodi() {
        return prihodi;
    }

    public void setPrihodi(Cijena prihodi) {
        this.prihodi = prihodi;
    }

    @Override
    public String toString() {
        return "{" +
                "idFinancije=" + idFinancije +
                ", proracun=" + proracun +
                ", troskovi='" + troskovi + '\'' +
                ", prihodi=" + prihodi +
                '}';
    }

    @Override
    public void Serijalizacija(String filepath) {
        try {
            ObjectOutputStream zapisivanje = new ObjectOutputStream(new FileOutputStream(filepath));
            Financije financije = new Financije(getIdFinancije(), getProracun(), getTroskovi(), getPrihodi());
            zapisivanje.writeObject(financije);
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
            pw.println(getIdFinancije());
            pw.println(getProracun());
            pw.println(getTroskovi());
            pw.println(getPrihodi());

        }
        catch(IOException e){
            e.printStackTrace();
            String poruka="Dogodila se pogreška kod zapisivanja promjene!";
            System.out.println(poruka);
            Main.logger.info(poruka);
        }
    }
    public Financije Deserijalizacija(String filepath) {
        try (ObjectInputStream citanje = new ObjectInputStream(new FileInputStream(filepath))) {
            Object obj = citanje.readObject();
            if (obj instanceof Financije) {
                return (Financije) obj;
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
