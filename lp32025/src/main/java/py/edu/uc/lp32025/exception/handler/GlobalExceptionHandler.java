package py.edu.uc.lp32025.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import py.edu.uc.lp32025.dto.ErrorDto;
import py.edu.uc.lp32025.exception.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // -------------------------------------------------------------------------
    //  VALIDACIÓN DE @Valid  (SE MANTIENE COMO ESTÁ)
    // -------------------------------------------------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            fieldErrors.put(fieldName, message);
        });

        body.put("status", 400);
        body.put("error", "BAD_REQUEST");
        body.put("timestamp", LocalDateTime.now());
        body.put("validationErrors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // -------------------------------------------------------------------------
    //  EXCEPCIONES PERSONALIZADAS DEL TRABAJO  (OBLIGATORIAS)
    // -------------------------------------------------------------------------

    @ExceptionHandler(EmpleadoNoEncontradoException.class)
    public ResponseEntity<ErrorDto> handleEmpleadoNoEncontrado(EmpleadoNoEncontradoException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DiasInsuficientesException.class)
    public ResponseEntity<ErrorDto> handleDiasInsuficientes(DiasInsuficientesException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(PermisoDenegadoException.class)
    public ResponseEntity<ErrorDto> handlePermisoDenegado(PermisoDenegadoException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MapeoInvalidoException.class)
    public ResponseEntity<ErrorDto> handleMapeoInvalido(MapeoInvalidoException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // -------------------------------------------------------------------------
    //  IllegalArgument (tu lógica actual)
    // -------------------------------------------------------------------------
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 400);
        body.put("error", "BAD_REQUEST");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // -------------------------------------------------------------------------
    //  Excepción genérica (tu lógica actual)
    // -------------------------------------------------------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 500);
        body.put("error", "INTERNAL_SERVER_ERROR");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    // -------------------------------------------------------------------------
    //  Método privado para construir ErrorDto  (REQUERIDO POR LA CONSIGNA)
    // -------------------------------------------------------------------------
    private ResponseEntity<ErrorDto> build(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ErrorDto(message, status.value()));
    }
}
