package co.com.crediya.usecase.registrarusuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.exceptions.UsuarioYaExisteException;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegistrarUsuarioUseCaseTest {

    private final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    private final RegistrarUsuarioUseCase useCase = new RegistrarUsuarioUseCase(usuarioRepository);

    @Test
    void registraUsuarioNormalizandoNombresYCorreo() {
        Usuario usuario = usuarioValido(" Ana ", " Pérez ", " ANA@EXAMPLE.COM ", "15000000");
        when(usuarioRepository.existsByCorreoElectronico("ana@example.com")).thenReturn(false);
        when(usuarioRepository.save(org.mockito.ArgumentMatchers.any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario registrado = useCase.execute(usuario);

        assertEquals("Ana", registrado.getNombres());
        assertEquals("Pérez", registrado.getApellidos());
        assertEquals("ana@example.com", registrado.getCorreoElectronico());
        verify(usuarioRepository).save(registrado);
    }

    @Test
    void rechazaCorreoDuplicado() {
        when(usuarioRepository.existsByCorreoElectronico("ana@example.com")).thenReturn(true);

        assertThrows(UsuarioYaExisteException.class,
                () -> useCase.execute(usuarioValido("Ana", "Pérez", "ana@example.com", "1")));

        verify(usuarioRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void rechazaCorreoInvalido() {
        assertThrows(UsuarioInvalidoException.class,
                () -> useCase.execute(usuarioValido("Ana", "Pérez", "correo-invalido", "1")));

        verify(usuarioRepository, never()).existsByCorreoElectronico(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    void rechazaSalarioFueraDelRango() {
        assertThrows(UsuarioInvalidoException.class,
                () -> useCase.execute(usuarioValido("Ana", "Pérez", "ana@example.com", "15000000.01")));

        verify(usuarioRepository, never()).existsByCorreoElectronico(org.mockito.ArgumentMatchers.anyString());
    }

    private Usuario usuarioValido(String nombres, String apellidos, String correo, String salario) {
        return Usuario.builder()
                .nombres(nombres)
                .apellidos(apellidos)
                .correoElectronico(correo)
                .salarioBase(new BigDecimal(salario))
                .build();
    }
}
