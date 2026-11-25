package co.com.crediya.model.usuario.gateways;

import co.com.crediya.model.usuario.TokenAcceso;
import co.com.crediya.model.usuario.Usuario;

public interface TokenGateway {
    TokenAcceso generar(Usuario usuario);
}
