package upravljanje.filmskom.produkcijom.projekt.entiteti;

import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.io.*;

public class Adresa implements ZapisPromjena {

    private Long idAdrese;
    private Integer broj;
    private String ulica, grad, postanskiBroj, drzava;

    public Adresa(Long idAdrese, Integer broj, String ulica, String grad, String postanskiBroj, String drzava) {
        this.idAdrese = idAdrese;
        this.broj = broj;
        this.ulica = ulica;
        this.grad = grad;
        this.postanskiBroj = postanskiBroj;
        this.drzava = drzava;
    }

    public Long getId() {
        return idAdrese;
    }

    public void setId(Long id) {
        this.idAdrese = id;
    }

    public Integer getBroj() {
        return broj;
    }

    public void setBroj(Integer broj) {
        this.broj = broj;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getPostanskiBroj() {
        return postanskiBroj;
    }

    public void setPostanskiBroj(String postanskiBroj) {
        this.postanskiBroj = postanskiBroj;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    @Override
    public void Serijalizacija(String filepath) {
        try {
            ObjectOutputStream zapisivanje = new ObjectOutputStream(new FileOutputStream(filepath));
            Adresa adresa = new Adresa(getId(), getBroj(), getUlica(), getGrad(), getPostanskiBroj(), getDrzava());
            zapisivanje.writeObject(adresa);
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
                pw.println(getId());
                pw.println(getBroj());
                pw.println(getUlica());
                pw.println(getGrad());
                pw.println(getPostanskiBroj());
                pw.println(getDrzava());

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
        return idAdrese + " " + broj + " " +ulica + " " + grad + " " + postanskiBroj + " " + drzava;
    }
    public Adresa Deserijalizacija(String filepath) {
        try (ObjectInputStream citanje = new ObjectInputStream(new FileInputStream(filepath))) {
            Object obj = citanje.readObject();
            if (obj instanceof Adresa) {
                return (Adresa) obj;
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

    public static class BuilderAdresa {

        private Long idAdrese;

        private Integer broj;

        private String ulica;

        private String grad;

        private String postanskiBroj;

        private String drzava;

        public Adresa build() {
            return new Adresa(idAdrese, broj, ulica,grad,postanskiBroj,drzava);
        }
        public BuilderAdresa withId(Long idAdrese){
            this.idAdrese = idAdrese;
            return this;
        }
        public BuilderAdresa withBroj(Integer broj){
            this.broj = broj;
            return this;
        }
        public BuilderAdresa withUlica(String ulica){
            this.ulica = ulica;
            return this;
        }
        public BuilderAdresa withGrad(String grad){
            this.grad = grad;
            return this;
        }
        public BuilderAdresa withPbr(String postanskiBroj){
            this.postanskiBroj = postanskiBroj;
            return this;
        }
        public BuilderAdresa withDrzava(String drzava){
            this.drzava = drzava;
            return this;
        }
    }


}
