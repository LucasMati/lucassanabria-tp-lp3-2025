package py.edu.uc.lp32025.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import py.edu.uc.lp32025.domain.Persona;
import py.edu.uc.lp32025.service.PersonaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @GetMapping
    public List<Persona> getAllPersonas() {
        return personaService.findAllPersonas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> getPersonaById(@PathVariable Long id) {
        Optional<Persona> persona = personaService.findPersonaById(id);
        return persona.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createPersona(@RequestBody Persona persona) {
        try {
            Persona createdPersona = personaService.savePersona(persona);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPersona);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Solicitud incorrecta");
            errorResponse.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePersona(@PathVariable Long id, @RequestBody Persona personaDetails) {
        Optional<Persona> optionalPersona = personaService.findPersonaById(id);

        if (optionalPersona.isPresent()) {
            Persona existingPersona = optionalPersona.get();
            existingPersona.setNombre(personaDetails.getNombre());
            existingPersona.setApellido(personaDetails.getApellido());
            existingPersona.setFechaNacimiento(personaDetails.getFechaNacimiento());
            existingPersona.setNumeroDocumento(personaDetails.getNumeroDocumento());

            try {
                Persona updatedPersona = personaService.savePersona(existingPersona);
                return ResponseEntity.ok(updatedPersona);
            } catch (IllegalArgumentException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Solicitud incorrecta");
                errorResponse.put("mensaje", e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersona(@PathVariable Long id) {
        if (personaService.findPersonaById(id).isPresent()) {
            personaService.deletePersonaById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}