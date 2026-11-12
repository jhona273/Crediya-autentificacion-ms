package co.com.crediya.api;

import co.com.crediya.model.usuario.Rol;

public record UsuarioResponse(Long id, String nombres, String apellidos,
                              String correoElectronico, Rol rol) {
}