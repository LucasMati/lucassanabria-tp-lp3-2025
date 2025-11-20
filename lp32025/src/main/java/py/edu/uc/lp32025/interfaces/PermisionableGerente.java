package py.edu.uc.lp32025.interfaces;

import py.edu.uc.lp32025.exception.PermisoDenegadoException;

import java.time.LocalDate;

public interface PermisionableGerente {

    /**
     * Vacaciones para gerentes, con máximo 30 días consecutivos.
     */
    void solicitarVacaciones(LocalDate inicio, LocalDate fin) throws PermisoDenegadoException;

    /**
     * Permisos especiales para gerentes, máximo 10 días.
     */
    void solicitarPermiso(String motivo, LocalDate inicio, LocalDate fin) throws PermisoDenegadoException;

    /**
     * Cálculo de bono anual exclusivo de gerentes.
     */
    java.math.BigDecimal calcularBonoAnual();
}
