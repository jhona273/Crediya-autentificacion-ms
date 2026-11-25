package co.com.crediya.model.usuario.gateways;

import co.com.crediya.model.usuario.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
    Usuario guardar(Usuario usuario);
    boolean existePorCorreo(String correoElectronico);
    Optional<Usuario> buscarPorCorreo(String correoElectronico);
}