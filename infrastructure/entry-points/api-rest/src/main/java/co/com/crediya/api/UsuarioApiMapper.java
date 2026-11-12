package co.com.crediya.api;

import co.com.crediya.model.usuario.Usuario;

public final class UsuarioApiMapper {

    private UsuarioApiMapper() {
    }

    public static Usuario toDomain(RegistrarUsuarioRequest r) {
        return Usuario.builder()
                .nombres(r.nombres())
                .apellidos(r.apellidos())
                .fechaNacimiento(r.fechaNacimiento())
                .direccion(r.direccion())
                .telefono(r.telefono())
                .correoElectronico(r.correoElectronico())
                .salarioBase(r.salarioBase())
                .documentoIdentidad(r.documentoIdentidad())
                .password(r.password())
                .build();
    }

    public static UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNombres(), u.getApellidos(),
                u.getCorreoElectronico(), u.getRol());
    }
}