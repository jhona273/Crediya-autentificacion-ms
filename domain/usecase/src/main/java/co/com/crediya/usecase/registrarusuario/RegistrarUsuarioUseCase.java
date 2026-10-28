package co.com.crediya.usecase.registrarusuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.exceptions.UsuarioYaExisteException;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class RegistrarUsuarioUseCase {

    private static final BigDecimal SALARIO_MINIMO = BigDecimal.ZERO;
    private static final BigDecimal SALARIO_MAXIMO = new BigDecimal("15000000");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final UsuarioRepository usuarioRepository;

    public Usuario execute(Usuario usuario) {
        validar(usuario);
        String correoElectronico = usuario.getCorreoElectronico().trim().toLowerCase(Locale.ROOT);

        if (usuarioRepository.existsByCorreoElectronico(correoElectronico)) {
            throw new UsuarioYaExisteException();
        }

        Usuario usuarioNormalizado = usuario.toBuilder()
                .nombres(usuario.getNombres().trim())
                .apellidos(usuario.getApellidos().trim())
                .correoElectronico(correoElectronico)
                .build();
        return usuarioRepository.save(usuarioNormalizado);
    }

    private void validar(Usuario usuario) {
        if (usuario == null
                || esVacio(usuario.getNombres())
                || esVacio(usuario.getApellidos())
                || esVacio(usuario.getCorreoElectronico())
                || usuario.getSalarioBase() == null) {
            throw new UsuarioInvalidoException("Nombres, apellidos, correo_electronico y salario_base son obligatorios");
        }

        String correoElectronico = usuario.getCorreoElectronico().trim();
        if (!EMAIL_PATTERN.matcher(correoElectronico).matches()) {
            throw new UsuarioInvalidoException("El correo_electronico no tiene un formato válido");
        }

        if (usuario.getSalarioBase().compareTo(SALARIO_MINIMO) < 0
                || usuario.getSalarioBase().compareTo(SALARIO_MAXIMO) > 0) {
            throw new UsuarioInvalidoException("El salario_base debe estar entre 0 y 15000000");
        }
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
