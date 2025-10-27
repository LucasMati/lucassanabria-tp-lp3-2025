package py.edu.uc.lp32025.exception;

/**
 * Excepción específica para recursos no encontrados (404).
 * Hereda de BusinessException.
 */
public class NotFoundException extends BusinessException {

    public NotFoundException(String message) {
        super("NOT_FOUND", message);
    }
}
