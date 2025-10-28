package py.edu.uc.lp32025.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import py.edu.uc.lp32025.domain.Persona;
import py.edu.uc.lp32025.dto.BatchEmpleadosRequest;
import py.edu.uc.lp32025.exception.NotFoundException;
import py.edu.uc.lp32025.service.PersonaService;
import py.edu.uc.lp32025.service.EmpleadoTiempoCompletoService;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private EmpleadoTiempoCompletoService empleadoService;

    // =================================================================
    // 1️⃣ Obtener todas las personas
    // =================================================================
    @GetMapping
    public ResponseEntity<List<Persona>> getAllPersonas() {
        List<Persona> personas = personaService.findAllPersonas();
        return ResponseEntity.ok(personas);
    }

    // =================================================================
    // 2️⃣ Buscar persona por ID
    // =================================================================
    @GetMapping("/{id}")
    public ResponseEntity<Persona> getPersonaById(@PathVariable Long id) {
        Persona persona = personaService.findPersonaById(id)
                .orElseThrow(() -> new NotFoundException("Persona no encontrada con ID: " + id));
        return ResponseEntity.ok(persona);
    }

    // =================================================================
    // 3️⃣ Buscar personas por nombre (punto 6 del TP)
    // =================================================================
    @GetMapping(params = "nombre")
    public ResponseEntity<List<Persona>> buscarPorNombre(@RequestParam String nombre) {
        List<Persona> personasFiltradas = personaService.findByNombreContainingIgnoreCase(nombre);
        if (personasFiltradas.isEmpty()) {
            throw new NotFoundException("No se encontraron personas con el nombre: " + nombre);
        }
        return ResponseEntity.ok(personasFiltradas);
    }

    // =================================================================
    // 4️⃣ Crear nueva persona (validada)
    // =================================================================
    @PostMapping
    public ResponseEntity<Persona> createPersona(@Valid @RequestBody Persona persona) {
        Persona createdPersona = personaService.savePersona(persona);
        return ResponseEntity.status(201).body(createdPersona);
    }

    // =================================================================
    // 5️⃣ Actualizar persona existente (validada)
    // =================================================================
    @PutMapping("/{id}")
    public ResponseEntity<Persona> updatePersona(@PathVariable Long id, @Valid @RequestBody Persona personaDetails) {
        Persona updatedPersona = personaService.updatePersona(id, personaDetails);
        return ResponseEntity.ok(updatedPersona);
    }

    // =================================================================
    // 6️⃣ Eliminar persona
    // =================================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersona(@PathVariable Long id) {
        if (personaService.findPersonaById(id).isEmpty()) {
            throw new NotFoundException("No existe una persona con el ID: " + id);
        }
        personaService.deletePersonaById(id);
        return ResponseEntity.noContent().build();
    }

    // =================================================================
    // 7️⃣ Nuevo endpoint: Persistencia polimórfica en batch (validada)
    // =================================================================
    @PostMapping("/batch")
    public ResponseEntity<?> guardarEmpleadosEnBatch(@Valid @RequestBody BatchEmpleadosRequest request) {
        var empleadosGuardados = empleadoService.guardarEmpleadosEnBatch(request.getEmpleados());
        return ResponseEntity.status(201).body(empleadosGuardados);
    }
}
