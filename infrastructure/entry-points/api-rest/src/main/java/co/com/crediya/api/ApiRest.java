package co.com.crediya.api;

import co.com.crediya.api.dto.RegistrarUsuarioRequest;
import co.com.crediya.api.dto.UsuarioResponse;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.usecase.registrarusuario.RegistrarUsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ApiRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiRest.class);
    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @GetMapping(path = "/usecase/path")
    public String commandName() {
        return "Value from use case v1";
    }

    @PostMapping(path = "/v1/usuarios", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioResponse> registrarUsuario(@Valid @RequestBody RegistrarUsuarioRequest request) {
        LOGGER.info("Solicitud recibida para registrar usuario");
        Usuario usuarioRegistrado = registrarUsuarioUseCase.execute(request.toDomain());
        LOGGER.info("Registro de usuario completado");
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.from(usuarioRegistrado));
    }

}
