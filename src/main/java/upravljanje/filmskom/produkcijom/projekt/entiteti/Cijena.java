package upravljanje.filmskom.produkcijom.projekt.entiteti;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public record Cijena(BigDecimal cijena) implements Serializable {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cijena cijena1)) return false;
        return Objects.equals(cijena, cijena1.cijena);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cijena);
    }

    @Override
    public String toString() {
        return String.valueOf(cijena);
    }
}
