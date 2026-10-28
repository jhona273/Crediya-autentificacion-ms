package co.com.crediya.api.dto;

import co.com.crediya.model.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class RegistrarUsuarioRequest {

    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidos;

    @JsonProperty("fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private String direccion;

    private String telefono;

    @JsonProperty("correo_electronico")
    @NotBlank
    @Email
    private String correoElectronico;

    @JsonProperty("salario_base")
    @NotNull
    @DecimalMin("0")
    @DecimalMax("15000000")
    private BigDecimal salarioBase;

    public Usuario toDomain() {
        return Usuario.builder()
                .nombres(nombres)
                .apellidos(apellidos)
                .fechaNacimiento(fechaNacimiento)
                .direccion(direccion)
                .telefono(telefono)
                .correoElectronico(correoElectronico)
                .salarioBase(salarioBase)
                .build();
    }
}
