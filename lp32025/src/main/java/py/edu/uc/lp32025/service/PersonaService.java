package py.edu.uc.lp32025.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.edu.uc.lp32025.domain.Persona;
import py.edu.uc.lp32025.exception.EmpleadoNoEncontradoException;
import py.edu.uc.lp32025.repository.PersonaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    // ================================================================
    // 1️⃣ Obtener todas las personas
    // ================================================================
    public List<Persona> findAllPersonas() {
        return personaRepository.findAll();
    }

    // ================================================================
    // 2️⃣ Buscar persona por ID
    // ================================================================
    public Optional<Persona> findPersonaById(Long id) {
        return personaRepository.findById(id);
    }

    // ================================================================
    // 3️⃣ Crear persona (con validación)
    // ================================================================
    public Persona savePersona(Persona persona) {

        if (persona.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser en el futuro.");
        }

        return personaRepository.save(persona);
    }

    // ================================================================
    // 4️⃣ Actualizar persona existente
    // ================================================================
    public Persona updatePersona(Long id, Persona personaDetails) {

        Persona existing = personaRepository.findById(id)
                .orElseThrow(() -> new EmpleadoNoEncontradoException(id));

        existing.setNombre(personaDetails.getNombre());
        existing.setApellido(personaDetails.getApellido());
        existing.setFechaNacimiento(personaDetails.getFechaNacimiento());
        existing.setNumeroDocumento(personaDetails.getNumeroDocumento());

        if (existing.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser en el futuro.");
        }

        return personaRepository.save(existing);
    }

    // ================================================================
    // 5️⃣ Eliminar persona
    // ================================================================
    public void deletePersonaById(Long id) {

        if (!personaRepository.existsById(id)) {
            throw new EmpleadoNoEncontradoException(id);
        }

        personaRepository.deleteById(id);
    }

    // ================================================================
    // 6️⃣ Buscar personas por nombre
    // ================================================================
    public List<Persona> findByNombreContainingIgnoreCase(String nombre) {
        return personaRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
