package co.com.crediya.model.usuario.gateways;

import co.com.crediya.model.usuario.Usuario;

public interface UsuarioRepository {

    boolean existsByCorreoElectronico(String correoElectronico);

    Usuario save(Usuario usuario);
}
