package co.com.crediya.api;

import co.com.crediya.usecase.registrarusuario.RegistrarUsuarioUseCase;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.Mockito.mock;

@Configuration
@EnableAutoConfiguration
@EnableWebMvc
@Import({ApiRest.class, ApiExceptionHandler.class})
public class TestConfig {

    @Bean
    RegistrarUsuarioUseCase registrarUsuarioUseCase() {
        return mock(RegistrarUsuarioUseCase.class);
    }
}