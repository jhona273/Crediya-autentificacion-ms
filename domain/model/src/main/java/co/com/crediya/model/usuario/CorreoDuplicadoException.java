package co.com.crediya.model.usuario;

public class CorreoDuplicadoException extends RuntimeException {
    public CorreoDuplicadoException(String correo) {
        super("El correo electrónico ya está registrado: " + correo);
    }
}
