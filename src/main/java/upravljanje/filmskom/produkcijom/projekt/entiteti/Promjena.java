package upravljanje.filmskom.produkcijom.projekt.entiteti;

import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.io.*;
import java.time.LocalDateTime;

public class Promjena<T> implements ZapisPromjena, Serializable{

    private T staraVrijednost;

    private T novaVrijednost;

    private String rola;

    private LocalDateTime vrijemePromjene;

    public Promjena(T staraVrijednost, T novaVrijednost, String rola, LocalDateTime vrijemePromjene) {
        this.staraVrijednost = staraVrijednost;
        this.novaVrijednost = novaVrijednost;
        this.rola = rola;
        this.vrijemePromjene = vrijemePromjene;
    }

    public T getStaraVrijednost() {
        return staraVrijednost;
    }

    public void setStaraVrijednost(T staraVrijednost) {
        this.staraVrijednost = staraVrijednost;
    }

    public T getNovaVrijednost() {
        return novaVrijednost;
    }

    public void setNovaVrijednost(T novaVrijednost) {
        this.novaVrijednost = novaVrijednost;
    }

    public String getRola() {
        return rola;
    }

    public void setRola(String rola) {
        this.rola = rola;
    }

    public LocalDateTime getVrijemePromjene() {
        return vrijemePromjene;
    }

    public void setVrijemePromjene(LocalDateTime vrijemePromjene) {
        this.vrijemePromjene = vrijemePromjene;
    }

    @Override
    public void Serijalizacija(String filepath) {
        try (ObjectOutputStream zapisivanje = new ObjectOutputStream(new FileOutputStream(filepath))) {
            Promjena<T> promjena = new Promjena<>(getStaraVrijednost(), getNovaVrijednost(), getRola(), getVrijemePromjene());
            zapisivanje.writeObject(promjena);
            System.out.println("Uspješna serijalizacija promjene!");
        } catch (IOException e) {
            e.printStackTrace();
            String poruka = "Dogodila se pogreška kod serijalizacije promjene!";
            System.out.println(poruka);
            Main.logger.info(poruka);
        }
    }

    @Override
    public void ZapisTXT(String filepath) {
        File adreseFile=new File(filepath);
        try(PrintWriter pw=new PrintWriter(adreseFile)){
            pw.println(getStaraVrijednost());
            pw.println(getNovaVrijednost());
            pw.println(getRola());
            pw.println(getVrijemePromjene());
        }
        catch(IOException e){
            e.printStackTrace();
            String poruka="Dogodila se pogreška kod zapisivanja promjene!";
            System.out.println(poruka);
            Main.logger.info(poruka);
        }
    }
    @Override
    public String toString() {
        return "Promjena{" +
                "staraVrijednost=" + staraVrijednost +
                ", novaVrijednost=" + novaVrijednost +
                ", rola='" + rola + '\'' +
                ", vrijemePromjene=" + vrijemePromjene +
                '}';
    }

    public static Promjena Deserijalizacija(String filepath) {
        try (ObjectInputStream citanje = new ObjectInputStream(new FileInputStream(filepath))) {
            Promjena obj = (Promjena) citanje.readObject();
            if (obj instanceof Promjena) {
                return (Promjena) obj;
            } else {
                System.out.println("Objekt nije tipa Promjena.");
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
