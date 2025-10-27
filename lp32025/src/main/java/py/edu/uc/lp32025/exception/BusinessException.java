package py.edu.uc.lp32025.exception;

/**
 * Excepción personalizada para errores de negocio.
 * Permite definir un código de error y un mensaje específico.
 */
public class BusinessException extends RuntimeException {

    private final String code;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
