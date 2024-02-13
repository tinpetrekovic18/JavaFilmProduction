package upravljanje.filmskom.produkcijom.projekt.entiteti;

import java.util.List;

public class PopisOsoba<T extends Osoba> {

    private List<T> popis;

    public PopisOsoba(List<T> popis) {
        this.popis = popis;
    }

    public List<T> getPopis() {
        return popis;
    }

    public void setPopis(List<T> popis) {
        this.popis = popis;
    }
}
