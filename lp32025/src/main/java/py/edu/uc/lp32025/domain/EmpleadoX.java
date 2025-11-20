package py.edu.uc.lp32025.domain;

import py.edu.uc.lp32025.interfaces.Mapeable;

import java.math.BigDecimal;

public class EmpleadoX extends Persona implements Mapeable {

    @Override
    public BigDecimal calcularSalario() {
        // MOCK: Siempre retorna un monto fijo
        return new BigDecimal("1500000");
    }

    @Override
    public BigDecimal calcularDeducciones() {
        // MOCK: Retorna valor fijo
        return new BigDecimal("200000");
    }

    @Override
    public boolean validarDatosEspecificos() {
        // MOCK: Siempre válido
        return true;
    }

    @Override
    public PosicionGps ubicarElemento() {
        // MOCK: Ubicación inventada
        return new PosicionGps(-25.3000, -57.6400);
    }

    @Override
    public Avatar obtenerImagen() {
        // MOCK: Avatar dummy
        return new Avatar(
                "https://cdn-icons-png.flaticon.com/512/149/149071.png",
                "EmpleadoX"
        );
    }
}
