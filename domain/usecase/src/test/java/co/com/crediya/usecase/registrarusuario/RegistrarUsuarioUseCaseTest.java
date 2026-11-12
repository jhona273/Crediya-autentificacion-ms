package co.com.crediya.usecase.registrarusuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.CorreoDuplicadoException;
import co.com.crediya.model.usuario.Rol;
import co.com.crediya.model.usuario.UsuarioInvalidoException;
import co.com.crediya.model.usuario.gateways.PasswordEncoderGateway;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

class RegistrarUsuarioUseCaseTest {

    private final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    private final PasswordEncoderGateway passwordEncoder = mock(PasswordEncoderGateway.class);
    private final RegistrarUsuarioUseCase useCase =
            new RegistrarUsuarioUseCase(usuarioRepository, passwordEncoder);

    @Test
    void normalizaCamposEncriptaPasswordYAsignaRolPorDefecto() {
        Usuario usuario = usuarioValido(" Ana ", " Pérez ", " ANA@EXAMPLE.COM ", "15000000");
        when(usuarioRepository.existePorCorreo("ana@example.com")).thenReturn(false);
        when(passwordEncoder.encriptar("Password123")).thenReturn("password-encriptado");
        when(usuarioRepository.guardar(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario registrado = useCase.ejecutar(usuario);

        assertEquals("Ana", registrado.getNombres());
        assertEquals("Pérez", registrado.getApellidos());
        assertEquals("ana@example.com", registrado.getCorreoElectronico());
        assertEquals("password-encriptado", registrado.getPassword());
        assertEquals(Rol.CLIENTE, registrado.getRol());
        verify(usuarioRepository).guardar(registrado);
    }

    @Test
    void rechazaCorreoInvalidoAntesDeConsultarRepositorio() {
        assertThrows(UsuarioInvalidoException.class,
                () -> useCase.ejecutar(usuarioValido("Ana", "Pérez", "correo-invalido", "1")));

        verify(usuarioRepository, never()).existePorCorreo(any());
    }

    @Test
    void rechazaSalarioFueraDelRangoAntesDeConsultarRepositorio() {
        assertThrows(UsuarioInvalidoException.class,
                () -> useCase.ejecutar(usuarioValido("Ana", "Pérez", "ana@example.com", "15000000.01")));

        verify(usuarioRepository, never()).existePorCorreo(any());
    }

    @Test
    void rechazaCorreoDuplicado() {
        when(usuarioRepository.existePorCorreo("ana@example.com")).thenReturn(true);

        assertThrows(CorreoDuplicadoException.class,
                () -> useCase.ejecutar(usuarioValido("Ana", "Pérez", "ana@example.com", "1")));

        verify(passwordEncoder, never()).encriptar(any());
        verify(usuarioRepository, never()).guardar(any());
    }

    private Usuario usuarioValido(String nombres, String apellidos, String correo, String salario) {
        return Usuario.builder()
                .nombres(nombres)
                .apellidos(apellidos)
                .correoElectronico(correo)
                .salarioBase(new BigDecimal(salario))
                .password("Password123")
                .build();
    }
}
