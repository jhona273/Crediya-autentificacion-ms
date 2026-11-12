package co.com.crediya.usecase.registrarusuario;

import co.com.crediya.model.usuario.CorreoDuplicadoException;
import co.com.crediya.model.usuario.Rol;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.UsuarioValidator;
import co.com.crediya.model.usuario.gateways.PasswordEncoderGateway;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import java.util.Locale;
import java.util.Objects;

@RequiredArgsConstructor
public class RegistrarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoderGateway passwordEncoder;

    public Usuario ejecutar(Usuario usuario) {
        Objects.requireNonNull(usuario, "El usuario es obligatorio");
        Usuario datos = normalizar(usuario);
        UsuarioValidator.validar(datos);
        verificarCorreoDisponible(datos.getCorreoElectronico());
        return usuarioRepository.guardar(prepararParaGuardar(datos));
    }

    private Usuario normalizar(Usuario usuario) {
        return usuario.toBuilder()
                .nombres(limpiar(usuario.getNombres()))
                .apellidos(limpiar(usuario.getApellidos()))
                .correoElectronico(limpiarCorreo(usuario.getCorreoElectronico()))
                .build();
    }

    private void verificarCorreoDisponible(String correo) {
        if (usuarioRepository.existePorCorreo(correo)) {
            throw new CorreoDuplicadoException(correo);
        }
    }

    private Usuario prepararParaGuardar(Usuario usuario) {
        return usuario.toBuilder()
                .password(passwordEncoder.encriptar(usuario.getPassword()))
                .rol(usuario.getRol() == null ? Rol.CLIENTE : usuario.getRol())
                .build();
    }

    private static String limpiar(String valor) {
        return valor == null ? null : valor.strip();
    }

    private static String limpiarCorreo(String correo) {
        return correo == null ? null : correo.strip().toLowerCase(Locale.ROOT);
    }
}