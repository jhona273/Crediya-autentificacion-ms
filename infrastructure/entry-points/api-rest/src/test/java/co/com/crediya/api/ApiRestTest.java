package co.com.crediya.api;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.exceptions.UsuarioYaExisteException;
import co.com.crediya.usecase.registrarusuario.RegistrarUsuarioUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TestConfig.class)
class ApiRestTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RegistrarUsuarioUseCase registrarUsuarioUseCase;

    private RestTestClient client;

    @BeforeEach
    void setup() {
        reset(registrarUsuarioUseCase);
        client = RestTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void shouldReturnValueFromUseCaseVersion1() {
        client.get()
                .uri("/api/usecase/path")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class)
                .isEqualTo("Value from use case v1");
    }


    @Test
    void shouldReturnNotFoundForInvalidPath() {
        client.get()
                .uri("/api/usecase/invalid")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldRegisterUsuarioAndReturnCreated() {
        when(registrarUsuarioUseCase.execute(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        client.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "nombres": "Ana",
                          "apellidos": "Pérez",
                          "fecha_nacimiento": "1990-05-10",
                          "direccion": "Calle 10",
                          "telefono": "3001234567",
                          "correo_electronico": "ana@example.com",
                          "salario_base": 2500000
                        }
                        """)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.nombres").isEqualTo("Ana")
                .jsonPath("$.correo_electronico").isEqualTo("ana@example.com")
                .jsonPath("$.salario_base").isEqualTo(2500000);

        verify(registrarUsuarioUseCase).execute(any(Usuario.class));
    }

    @Test
    void shouldRejectInvalidSalary() {
        client.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "nombres": "Ana",
                          "apellidos": "Pérez",
                          "correo_electronico": "ana@example.com",
                          "salario_base": 15000000.01
                        }
                        """)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnConflictWhenEmailIsAlreadyRegistered() {
        when(registrarUsuarioUseCase.execute(any(Usuario.class))).thenThrow(new UsuarioYaExisteException());

        client.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "nombres": "Ana",
                          "apellidos": "Pérez",
                          "correo_electronico": "ana@example.com",
                          "salario_base": 2500000
                        }
                        """)
                .exchange()
                .expectStatus().isEqualTo(409);
    }
}
