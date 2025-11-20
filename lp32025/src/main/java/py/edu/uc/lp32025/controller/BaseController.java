package py.edu.uc.lp32025.controller;

import org.springframework.http.ResponseEntity;
import py.edu.uc.lp32025.domain.Persona;
import py.edu.uc.lp32025.exception.EmpleadoNoEncontradoException;
import py.edu.uc.lp32025.repository.PersonaRepository;

public abstract class BaseController {

    protected final PersonaRepository personaRepository;

    protected BaseController(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    protected Persona getPersonaOrThrow(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new EmpleadoNoEncontradoException(id));
    }

    protected ResponseEntity<?> ok(Object body) {
        return ResponseEntity.ok(body);
    }
}
