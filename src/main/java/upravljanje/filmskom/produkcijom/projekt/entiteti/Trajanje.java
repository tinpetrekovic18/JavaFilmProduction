package upravljanje.filmskom.produkcijom.projekt.entiteti;

public sealed interface Trajanje permits Scena, Distribucija{

    public Long satiSnimanja();
}
