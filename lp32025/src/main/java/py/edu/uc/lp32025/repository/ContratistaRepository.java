package py.edu.uc.lp32025.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import py.edu.uc.lp32025.domain.Contratista;
import java.time.LocalDate;
import java.util.List;

public interface ContratistaRepository extends JpaRepository<Contratista, Long> {
    List<Contratista> findByFechaFinContratoAfter(LocalDate fecha);
}
