package co.com.crediya.api;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El correo electrónico es obligatorio") String correoElectronico,
        @NotBlank(message = "La contraseña es obligatoria") String password) {
}
