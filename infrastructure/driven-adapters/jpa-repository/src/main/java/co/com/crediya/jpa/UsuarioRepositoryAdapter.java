package co.com.crediya.jpa;

import co.com.crediya.model.usuario.CorreoDuplicadoException;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final UsuarioJpaRepository jpa;

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        try {
            UsuarioData guardado = jpa.save(UsuarioMapper.toData(usuario));
            log.info("Usuario guardado con id {}", guardado.getId());
            return UsuarioMapper.toDomain(guardado);
        } catch (DataIntegrityViolationException e) {
            if (esViolacionUnica(e)) {
                log.warn("Correo duplicado al guardar: {}", usuario.getCorreoElectronico());
                throw new CorreoDuplicadoException(usuario.getCorreoElectronico());
            }
            throw e;
        }
    }

    @Override
    public boolean existePorCorreo(String correoElectronico) {
        return jpa.existsByCorreoElectronico(correoElectronico);
    }

    private boolean esViolacionUnica(DataIntegrityViolationException exception) {
        Throwable causa = exception;
        while (causa != null) {
            if (causa instanceof SQLException sqlException && "23505".equals(sqlException.getSQLState())) {
                return true;
            }
            causa = causa.getCause();
        }
        return false;
    }
}