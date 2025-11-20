package py.edu.uc.lp32025.demo;

import lombok.extern.slf4j.Slf4j;
import py.edu.uc.lp32025.domain.EmpleadoPermisionable;
import py.edu.uc.lp32025.domain.Gerente;
import py.edu.uc.lp32025.exception.PermisoDenegadoException;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
public class PermisionableApp {
    public static void main(String[] args) {
        log.info("=== DEMOSTRACIÓN DE INTERFACE PERMISIONABLE ===");

        // 🧍 Empleado común
        EmpleadoPermisionable empleado = new EmpleadoPermisionable();
        empleado.setNombre("Lucas");
        empleado.setApellido("Sanabria");

        try {
            log.info("--- CASOS DE EMPLEADO ---");
            empleado.solicitarVacaciones(LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 10));
            empleado.solicitarVacaciones(LocalDate.of(2025, 12, 15), LocalDate.of(2026, 1, 10)); // excede
            empleado.solicitarPermiso("enfermedad", LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 5));
            empleado.solicitarPermiso("viaje", LocalDate.of(2025, 11, 10), LocalDate.of(2025, 11, 15));

        } catch (PermisoDenegadoException e) {
            log.error("❌ EMPLEADO — {} | Motivo: {} | Rango: {} → {}",
                    e.getMessage(), e.getMotivo(), e.getFechaInicio(), e.getFechaFin());
        }

        // 👔 Gerente con interfaz extendida
        Gerente gerente = new Gerente();
        gerente.setNombre("María López");

        try {
            log.info("\n=== DEMOSTRACIÓN DE INTERFACE PERMISIONABLEGERENTE ===");
            log.info("--- CASOS DE GERENTE ---");

            // ✅ Vacaciones válidas
            gerente.solicitarVacaciones(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 25));

            // ❌ Vacaciones excesivas
            gerente.solicitarVacaciones(LocalDate.of(2025, 12, 1), LocalDate.of(2026, 1, 15));

            // ✅ Permiso válido
            gerente.solicitarPermiso("Conferencia internacional", LocalDate.of(2025, 10, 5), LocalDate.of(2025, 10, 10));

            // ❌ Permiso excesivo
            gerente.solicitarPermiso("Descanso extendido", LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 20));

            // 💰 Cálculo de bono
            BigDecimal bono = gerente.calcularBonoAnual();
            log.info("💵 Bono anual asignado a gerente {}: {}", gerente.getNombre(), bono);

        } catch (PermisoDenegadoException e) {
            log.error("❌ GERENTE — {} | Motivo: {} | Rango: {} → {}",
                    e.getMessage(), e.getMotivo(), e.getFechaInicio(), e.getFechaFin());
        }

        log.info("\n=== FIN DEMO ===");
    }
}
