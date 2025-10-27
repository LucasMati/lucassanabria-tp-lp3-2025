package py.edu.uc.lp32025.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import py.edu.uc.lp32025.domain.Persona;
import java.util.List;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    /**
     * Busca personas cuyo nombre contenga la cadena indicada, sin distinguir mayúsculas/minúsculas.
     * Ejemplo: findByNombreContainingIgnoreCase("ale")
     * Genera la consulta SQL equivalente a:
     * SELECT * FROM persona WHERE LOWER(nombre) LIKE LOWER('%ale%');
     */
    List<Persona> findByNombreContainingIgnoreCase(String nombre);
}
