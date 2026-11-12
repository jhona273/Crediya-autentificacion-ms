package co.com.crediya.api;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegistrarUsuarioRequest(
        @NotBlank(message = "Los nombres son obligatorios") String nombres,
        @NotBlank(message = "Los apellidos son obligatorios") String apellidos,
        LocalDate fechaNacimiento,
        String direccion,
        String telefono,
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato válido") String correoElectronico,
        @NotNull(message = "El salario base es obligatorio")
        @DecimalMin(value = "0", message = "El salario base no puede ser negativo")
        @DecimalMax(value = "15000000", message = "El salario base no puede superar 15.000.000") BigDecimal salarioBase,
        String documentoIdentidad,
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres") String password) {
}