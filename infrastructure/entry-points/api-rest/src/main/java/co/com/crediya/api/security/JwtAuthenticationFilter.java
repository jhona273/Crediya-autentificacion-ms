package co.com.crediya.api.security;

import co.com.crediya.model.usuario.Rol;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String RUTA_USUARIOS = "/api/v1/usuarios";
    private static final String PREFIJO_BEARER = "Bearer ";
    private static final Set<String> ROLES_PERMITIDOS = Set.of(Rol.ADMINISTRADOR.name(), Rol.ASESOR.name());

    private final JwtTokenAdapter jwtTokenAdapter;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        boolean esRegistroDeUsuarios = "POST".equalsIgnoreCase(request.getMethod())
                && RUTA_USUARIOS.equals(request.getRequestURI());
        return !esRegistroDeUsuarios;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String encabezado = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (encabezado == null || !encabezado.startsWith(PREFIJO_BEARER)) {
            responder(response, HttpStatus.UNAUTHORIZED, "Se requiere un token de autenticación");
            return;
        }
        Claims claims;
        try {
            claims = jwtTokenAdapter.leer(encabezado.substring(PREFIJO_BEARER.length()).strip());
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Token rechazado: {}", e.getClass().getSimpleName());
            responder(response, HttpStatus.UNAUTHORIZED, "Token inválido o expirado");
            return;
        }
        if (!ROLES_PERMITIDOS.contains(claims.get("rol", String.class))) {
            responder(response, HttpStatus.FORBIDDEN, "No tienes permisos para realizar esta acción");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void responder(HttpServletResponse response, HttpStatus estado, String mensaje) throws IOException {
        response.setStatus(estado.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write("{\"mensaje\":\"" + mensaje + "\"}");
    }
}
