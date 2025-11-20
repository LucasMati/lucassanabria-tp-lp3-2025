package py.edu.uc.lp32025.exception;

public class MapeoInvalidoException extends RuntimeException {

    public MapeoInvalidoException(String message) {
        super(message);
    }

    public MapeoInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
