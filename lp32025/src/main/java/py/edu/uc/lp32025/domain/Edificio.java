package py.edu.uc.lp32025.domain;

import py.edu.uc.lp32025.interfaces.Mapeable;

public class Edificio implements Mapeable {

    private String nombre;

    public Edificio(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public PosicionGps ubicarElemento() {
        // MOCK: Ubicación de edificio ficticia
        return new PosicionGps(-25.2800, -57.6300);
    }

    @Override
    public Avatar obtenerImagen() {
        return new Avatar(
                "https://cdn-icons-png.flaticon.com/512/684/684908.png",
                nombre
        );
    }
}
