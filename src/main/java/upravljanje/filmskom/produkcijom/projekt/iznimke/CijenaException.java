package upravljanje.filmskom.produkcijom.projekt.iznimke;

public class CijenaException extends Exception{
    public CijenaException() {
    }

    public CijenaException(String message) {
        super(message);
    }

    public CijenaException(String message, Throwable cause) {
        super(message, cause);
    }

    public CijenaException(Throwable cause) {
        super(cause);
    }

    public CijenaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
