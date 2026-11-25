package co.com.crediya.api;

import co.com.crediya.model.usuario.TokenAcceso;
import co.com.crediya.usecase.login.LoginUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/login", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {

    private static final String TIPO_TOKEN = "Bearer";

    private final LoginUseCase loginUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Solicitud de inicio de sesión recibida");
        TokenAcceso acceso = loginUseCase.ejecutar(request.correoElectronico(), request.password());
        return ResponseEntity.ok(new LoginResponse(acceso.token(), TIPO_TOKEN, acceso.expiraEnSegundos()));
    }
}
