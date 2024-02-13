package upravljanje.filmskom.produkcijom.projekt.iznimke;

public class LozinkaException extends RuntimeException{
    public LozinkaException() {
    }

    public LozinkaException(String message) {
        super(message);
    }

    public LozinkaException(String message, Throwable cause) {
        super(message, cause);
    }

    public LozinkaException(Throwable cause) {
        super(cause);
    }

    public LozinkaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
