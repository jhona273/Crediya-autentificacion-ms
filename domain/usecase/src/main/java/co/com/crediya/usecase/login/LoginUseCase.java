package co.com.crediya.usecase.login;

import co.com.crediya.model.usuario.CredencialesInvalidasException;
import co.com.crediya.model.usuario.TokenAcceso;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.PasswordEncoderGateway;
import co.com.crediya.model.usuario.gateways.TokenGateway;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

@RequiredArgsConstructor
public class LoginUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoderGateway passwordEncoder;
    private final TokenGateway tokenGateway;

    public TokenAcceso ejecutar(String correoElectronico, String password) {
        if (correoElectronico == null || correoElectronico.isBlank() || password == null || password.isBlank()) {
            throw new CredencialesInvalidasException();
        }
        String correo = correoElectronico.strip().toLowerCase(Locale.ROOT);
        Usuario usuario = usuarioRepository.buscarPorCorreo(correo)
                .filter(u -> passwordEncoder.coincide(password, u.getPassword()))
                .orElseThrow(CredencialesInvalidasException::new);
        return tokenGateway.generar(usuario);
    }
}
