package py.edu.uc.lp32025.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import py.edu.uc.lp32025.dto.BaseResponseDto;
import py.edu.uc.lp32025.exception.BusinessException;
import py.edu.uc.lp32025.exception.NotFoundException;

import java.time.LocalDateTime;

/**
 * Manejo global de excepciones para toda la aplicación.
 * Convierte excepciones en respuestas JSON con BaseResponseDto.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponseDto> handleBusinessException(BusinessException ex) {
        BaseResponseDto response = new BaseResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                ex.getCode(),
                ex.getMessage()
        );
        response.setTimestamp(LocalDateTime.now());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BaseResponseDto> handleNotFoundException(NotFoundException ex) {
        BaseResponseDto response = new BaseResponseDto(
                HttpStatus.NOT_FOUND.value(),
                ex.getCode(),
                ex.getMessage()
        );
        response.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponseDto> handleIllegalArgument(IllegalArgumentException ex) {
        BaseResponseDto response = new BaseResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_ARGUMENT",
                ex.getMessage()
        );
        response.setTimestamp(LocalDateTime.now());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponseDto> handleGeneralException(Exception ex) {
        BaseResponseDto response = new BaseResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_ERROR",
                "Ocurrió un error inesperado: " + ex.getMessage()
        );
        response.setTimestamp(LocalDateTime.now());
        return ResponseEntity.internalServerError().body(response);
    }
}
