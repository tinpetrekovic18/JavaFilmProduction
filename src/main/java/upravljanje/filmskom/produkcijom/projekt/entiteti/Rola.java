package upravljanje.filmskom.produkcijom.projekt.entiteti;

import java.io.Serializable;

public class Rola implements Serializable {
    private Long idRole;
    private String nazivRole;
    private String opisRole;

    public Rola(Long idRole, String nazivRole, String opisRole) {
        this.idRole = idRole;
        this.nazivRole = nazivRole;
        this.opisRole = opisRole;
    }

    public Long getIdRole() {
        return idRole;
    }

    public void setIdRole(Long idRole) {
        this.idRole = idRole;
    }

    public String getOpisRole() {
        return opisRole;
    }

    public void setOpisRole(String opisRole) {
        this.opisRole = opisRole;
    }

    public String getNazivRole() {
        return nazivRole;
    }

    public void setNazivRole(String nazivRole) {
        this.nazivRole = nazivRole;
    }

    @Override
    public String toString() {
        return "Rola{" +
                "idRole=" + idRole +
                ", nazivRole='" + nazivRole + '\'' +
                ", opisRole='" + opisRole + '\'' +
                '}';
    }
}
