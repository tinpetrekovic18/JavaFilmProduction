package upravljanje.filmskom.produkcijom.projekt.entiteti;

import java.io.Serializable;

public interface ZapisPromjena extends Serializable {
    public void Serijalizacija(String filepath);

    public void ZapisTXT(String filepath);

}
