package co.com.crediya.jpa;

import co.com.crediya.model.usuario.Usuario;

public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static UsuarioData toData(Usuario u) {
        return UsuarioData.builder()
                .id(u.getId())
                .nombres(u.getNombres())
                .apellidos(u.getApellidos())
                .fechaNacimiento(u.getFechaNacimiento())
                .direccion(u.getDireccion())
                .telefono(u.getTelefono())
                .correoElectronico(u.getCorreoElectronico())
                .salarioBase(u.getSalarioBase())
                .documentoIdentidad(u.getDocumentoIdentidad())
                .password(u.getPassword())
                .rol(u.getRol())
                .build();
    }

    public static Usuario toDomain(UsuarioData d) {
        return Usuario.builder()
                .id(d.getId())
                .nombres(d.getNombres())
                .apellidos(d.getApellidos())
                .fechaNacimiento(d.getFechaNacimiento())
                .direccion(d.getDireccion())
                .telefono(d.getTelefono())
                .correoElectronico(d.getCorreoElectronico())
                .salarioBase(d.getSalarioBase())
                .documentoIdentidad(d.getDocumentoIdentidad())
                .password(d.getPassword())
                .rol(d.getRol())
                .build();
    }
}