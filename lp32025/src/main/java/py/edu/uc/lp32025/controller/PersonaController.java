package py.edu.uc.lp32025.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import py.edu.uc.lp32025.domain.Persona;
import py.edu.uc.lp32025.dto.BatchEmpleadosRequest;
import py.edu.uc.lp32025.exception.BusinessException;
import py.edu.uc.lp32025.exception.NotFoundException;
import py.edu.uc.lp32025.service.PersonaService;
import py.edu.uc.lp32025.service.EmpleadoTiempoCompletoService;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    // Servicio con la lógica batch
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
    // 4️⃣ Crear nueva persona
    // =================================================================
    @PostMapping
    public ResponseEntity<Persona> createPersona(@RequestBody Persona persona) {
        Persona createdPersona = personaService.savePersona(persona);
        return ResponseEntity.status(201).body(createdPersona);
    }

    // =================================================================
    // 5️⃣ Actualizar persona existente
    // =================================================================
    @PutMapping("/{id}")
    public ResponseEntity<Persona> updatePersona(@PathVariable Long id, @RequestBody Persona personaDetails) {
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
    // 7️⃣ Nuevo endpoint: Persistencia polimórfica en batch
    // =================================================================
    @PostMapping("/batch")
    public ResponseEntity<?> guardarEmpleadosEnBatch(@RequestBody BatchEmpleadosRequest request) {
        try {
            var empleadosGuardados = empleadoService.guardarEmpleadosEnBatch(request.getEmpleados());
            return ResponseEntity.status(201).body(empleadosGuardados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al guardar empleados en batch: " + e.getMessage());
        }
    }
}
