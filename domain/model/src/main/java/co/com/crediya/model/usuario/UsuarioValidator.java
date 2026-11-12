package co.com.crediya.model.usuario;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public final class UsuarioValidator {

    private static final BigDecimal SALARIO_MINIMO = BigDecimal.ZERO;
    private static final BigDecimal SALARIO_MAXIMO = new BigDecimal("15000000");
    private static final int PASSWORD_LONGITUD_MINIMA = 8;
    private static final Pattern FORMATO_CORREO = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private UsuarioValidator() {
    }

    public static void validar(Usuario usuario) {
        exigirTexto(usuario.getNombres(), "Los nombres son obligatorios");
        exigirTexto(usuario.getApellidos(), "Los apellidos son obligatorios");
        validarCorreo(usuario.getCorreoElectronico());
        validarSalario(usuario.getSalarioBase());
        validarPassword(usuario.getPassword());
    }

    private static void exigirTexto(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new UsuarioInvalidoException(mensaje);
        }
    }

    private static void validarCorreo(String correo) {
        if (correo == null || !FORMATO_CORREO.matcher(correo).matches()) {
            throw new UsuarioInvalidoException("El correo no tiene un formato válido");
        }
    }

    private static void validarSalario(BigDecimal salario) {
        if (salario == null
                || salario.compareTo(SALARIO_MINIMO) < 0
                || salario.compareTo(SALARIO_MAXIMO) > 0) {
            throw new UsuarioInvalidoException("El salario base debe estar entre 0 y 15.000.000");
        }
    }

    private static void validarPassword(String password) {
        if (password == null || password.length() < PASSWORD_LONGITUD_MINIMA) {
            throw new UsuarioInvalidoException("La contraseña debe tener mínimo 8 caracteres");
        }
    }
}