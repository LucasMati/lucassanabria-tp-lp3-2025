package py.edu.uc.lp32025.demo;

import py.edu.uc.lp32025.interfaces.Mapeable;
import py.edu.uc.lp32025.util.MapeableDataFactory;
import py.edu.uc.lp32025.util.MapaPrinter;

import java.util.List;

public class MapaDemo {

    public static void main(String[] args) {

        System.out.println("=== DEMO INTERFACE MAPEABLE ===");

        // ✅ Obtener mapeables desde la DataFactory
        List<Mapeable> elementos = MapeableDataFactory.crearDatosDemo();

        // ✅ Imprimir usando util printer
        MapaPrinter.imprimirMapa(elementos);

        System.out.println("\n=== FIN DEMO ===");
    }
}
