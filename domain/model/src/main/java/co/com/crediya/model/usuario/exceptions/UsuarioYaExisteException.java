package co.com.crediya.model.usuario.exceptions;

public class UsuarioYaExisteException extends RuntimeException {

    public UsuarioYaExisteException() {
        super("Ya existe un usuario registrado con ese correo_electronico");
    }
}
