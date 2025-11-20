package py.edu.uc.lp32025.domain;

public class Avatar {

    private String imagen;
    private String nick;

    public Avatar(String imagen, String nick) {
        this.imagen = imagen;
        this.nick = nick;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}