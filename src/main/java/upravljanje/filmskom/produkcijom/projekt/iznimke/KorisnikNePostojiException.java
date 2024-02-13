package upravljanje.filmskom.produkcijom.projekt.iznimke;

public class KorisnikNePostojiException extends Exception{
    public KorisnikNePostojiException() {
    }

    public KorisnikNePostojiException(String message) {
        super(message);
    }

    public KorisnikNePostojiException(String message, Throwable cause) {
        super(message, cause);
    }

    public KorisnikNePostojiException(Throwable cause) {
        super(cause);
    }

    public KorisnikNePostojiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
