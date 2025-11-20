package py.edu.uc.lp32025.exception;

public class EmpleadoNoEncontradoException extends RuntimeException {

    public EmpleadoNoEncontradoException(Long id) {
        super("Empleado no encontrado con ID: " + id);
    }

    public EmpleadoNoEncontradoException(String message) {
        super(message);
    }
}
