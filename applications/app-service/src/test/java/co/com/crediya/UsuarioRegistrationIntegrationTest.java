package co.com.crediya;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.exceptions.UsuarioYaExisteException;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.usecase.registrarusuario.RegistrarUsuarioUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UsuarioRegistrationIntegrationTest {

    @Autowired
    private RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void savesUsuarioAndRejectsDuplicateEmail() {
        String correoElectronico = UUID.randomUUID() + "@example.com";
        Usuario usuario = Usuario.builder()
                .nombres("Ana")
                .apellidos("Pérez")
                .correoElectronico(correoElectronico)
                .salarioBase(new BigDecimal("2500000"))
                .build();

        Usuario registrado = registrarUsuarioUseCase.execute(usuario);

        assertEquals("Ana", registrado.getNombres());
        assertTrue(usuarioRepository.existsByCorreoElectronico(correoElectronico));
        assertThrows(UsuarioYaExisteException.class, () -> registrarUsuarioUseCase.execute(usuario));
    }
}
