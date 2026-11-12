package co.com.crediya;

import co.com.crediya.model.usuario.Rol;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.CorreoDuplicadoException;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.usecase.registrarusuario.RegistrarUsuarioUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:crediya-test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
})
class UsuarioRegistrationIntegrationTest {

    @Autowired
    private RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void savesUsuarioAndRejectsDuplicateEmail() {
        String correoElectronico = UUID.randomUUID() + "@example.com";
        Usuario usuario = Usuario.builder()
                .rol(Rol.CLIENTE)
                .nombres("Ana")
                .apellidos("Pérez")
                .correoElectronico(correoElectronico)
                .salarioBase(new BigDecimal("2500000"))
                .password("Password123")
                .build();

        Usuario registrado = registrarUsuarioUseCase.ejecutar(usuario);

        assertEquals("Ana", registrado.getNombres());
        assertTrue(usuarioRepository.existePorCorreo(correoElectronico));
        assertThrows(CorreoDuplicadoException.class, () -> registrarUsuarioUseCase.ejecutar(usuario));
    }

    @Test
    void onlyMapsEmailConstraintViolationsAsDuplicateEmail() {
        String correoElectronico = UUID.randomUUID() + "@example.com";
        Usuario usuario = Usuario.builder()
                .rol(Rol.CLIENTE)
                .nombres("Ana")
                .apellidos("Pérez")
                .correoElectronico(correoElectronico)
                .salarioBase(new BigDecimal("2500000"))
                .password("Password123")
                .build();

        usuarioRepository.guardar(usuario);
        assertThrows(CorreoDuplicadoException.class, () -> usuarioRepository.guardar(usuario));

        Usuario usuarioSinNombre = usuario.toBuilder()
                .nombres(null)
                .correoElectronico(UUID.randomUUID() + "@example.com")
                .build();
        assertThrows(DataIntegrityViolationException.class, () -> usuarioRepository.guardar(usuarioSinNombre));
    }
}
