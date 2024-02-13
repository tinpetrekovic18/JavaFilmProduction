package upravljanje.filmskom.produkcijom.projekt.entiteti;

import java.io.Serializable;

public abstract class Osoba implements Serializable {

    Long idOsobe;

    String ime;

    String prezime;

    String OIB;

    Integer dob;

    public Osoba() {
        // Initialize fields with default values
        this.idOsobe = null;
        this.ime = null;
        this.prezime = null;
        this.OIB = null;
        this.dob = 0;
    }
    public Osoba(Long idOsobe, String ime, String prezime, String OIB, Integer dob) {
        this.idOsobe = idOsobe;
        this.ime = ime;
        this.prezime = prezime;
        this.OIB = OIB;
        this.dob = dob;
    }

    public Long getIdOsobe() {
        return idOsobe;
    }

    public void setIdOsobe(Long idOsobe) {
        this.idOsobe = idOsobe;
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

    public String getOIB() {
        return OIB;
    }

    public void setOIB(String OIB) {
        this.OIB = OIB;
    }

    public Integer getDob() {
        return dob;
    }

    public void setDob(Integer dob) {
        this.dob = dob;
    }
}
