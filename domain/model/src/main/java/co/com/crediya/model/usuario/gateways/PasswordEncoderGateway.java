package co.com.crediya.model.usuario.gateways;

public interface PasswordEncoderGateway {
    String encriptar(String passwordPlano);
    boolean coincide(String passwordPlano, String passwordEncriptado);
}