package py.edu.uc.lp32025.util;

import py.edu.uc.lp32025.domain.EmpleadoX;
import py.edu.uc.lp32025.domain.Vehiculo;
import py.edu.uc.lp32025.domain.Edificio;
import py.edu.uc.lp32025.interfaces.Mapeable;

import java.util.ArrayList;
import java.util.List;

public class MapeableDataFactory {

    public static List<Mapeable> crearDatosDemo() {
        List<Mapeable> elementos = new ArrayList<>();

        // Mock de Empleado
        elementos.add(new EmpleadoX());

        // Mock de Vehículos
        elementos.add(new Vehiculo("Toyota", "Corolla"));
        elementos.add(new Vehiculo("Honda", "Civic"));

        // Mock de Edificios
        elementos.add(new Edificio("Torre Central"));
        elementos.add(new Edificio("Edificio Azul"));

        return elementos;
    }
}
