package co.com.crediya.api.security;

import co.com.crediya.model.usuario.TokenAcceso;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.TokenGateway;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenAdapter implements TokenGateway {

    private final SecretKey key;
    private final long expiracionSegundos;

    public JwtTokenAdapter(@Value("${jwt.secret}") String secret,
                           @Value("${jwt.expiracion-segundos}") long expiracionSegundos) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiracionSegundos = expiracionSegundos;
    }

    @Override
    public TokenAcceso generar(Usuario usuario) {
        Instant ahora = Instant.now();
        String token = Jwts.builder()
                .subject(usuario.getCorreoElectronico())
                .claim("uid", usuario.getId())
                .claim("rol", usuario.getRol().name())
                .claim("documento_identidad", usuario.getDocumentoIdentidad())
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plusSeconds(expiracionSegundos)))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
        return new TokenAcceso(token, expiracionSegundos);
    }

    public Claims leer(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
