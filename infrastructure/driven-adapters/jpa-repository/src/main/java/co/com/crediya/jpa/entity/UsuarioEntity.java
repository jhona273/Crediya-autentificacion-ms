package co.com.crediya.jpa.entity;

import co.com.crediya.model.usuario.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuarios_correo_electronico", columnNames = "correo_electronico")
})
@Getter
@Setter
@NoArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nombres", nullable = false)
    private String nombres;

    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "correo_electronico", nullable = false)
    private String correoElectronico;

    @Column(name = "salario_base", nullable = false, precision = 17, scale = 2)
    private BigDecimal salarioBase;

    public static UsuarioEntity fromDomain(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setNombres(usuario.getNombres());
        entity.setApellidos(usuario.getApellidos());
        entity.setFechaNacimiento(usuario.getFechaNacimiento());
        entity.setDireccion(usuario.getDireccion());
        entity.setTelefono(usuario.getTelefono());
        entity.setCorreoElectronico(usuario.getCorreoElectronico());
        entity.setSalarioBase(usuario.getSalarioBase());
        return entity;
    }

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
