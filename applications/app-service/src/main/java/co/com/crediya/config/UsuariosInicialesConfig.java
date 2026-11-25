package co.com.crediya.config;

import co.com.crediya.model.usuario.Rol;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.usecase.registrarusuario.RegistrarUsuarioUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Slf4j
@Configuration
public class UsuariosInicialesConfig {

    @Bean
    public CommandLineRunner crearUsuariosIniciales(UsuarioRepository usuarioRepository,
                                                    RegistrarUsuarioUseCase registrarUsuarioUseCase,
                                                    @Value("${seed.password:Crediya2026}") String password) {
        return args -> {
            crearSiNoExiste(usuarioRepository, registrarUsuarioUseCase,
                    "admin@crediya.com", "Admin", "1000000001", Rol.ADMINISTRADOR, password);
            crearSiNoExiste(usuarioRepository, registrarUsuarioUseCase,
                    "asesor@crediya.com", "Asesor", "1000000002", Rol.ASESOR, password);
        };
    }

    private void crearSiNoExiste(UsuarioRepository usuarioRepository,
                                 RegistrarUsuarioUseCase registrarUsuarioUseCase,
                                 String correo, String nombres, String documento, Rol rol, String password) {
        if (usuarioRepository.existePorCorreo(correo)) {
            return;
        }
        registrarUsuarioUseCase.ejecutar(Usuario.builder()
                .nombres(nombres)
                .apellidos("Sistema")
                .correoElectronico(correo)
                .salarioBase(BigDecimal.ZERO)
                .documentoIdentidad(documento)
                .password(password)
                .rol(rol)
                .build());
        log.info("Usuario inicial creado: {}", correo);
    }
}
