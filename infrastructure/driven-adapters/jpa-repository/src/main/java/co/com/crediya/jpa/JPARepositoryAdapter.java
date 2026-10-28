package co.com.crediya.jpa;

import co.com.crediya.jpa.entity.UsuarioEntity;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.exceptions.UsuarioYaExisteException;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JPARepositoryAdapter implements UsuarioRepository {

    private final JPARepository repository;

    @Override
    public boolean existsByCorreoElectronico(String correoElectronico) {
        return repository.existsByCorreoElectronicoIgnoreCase(correoElectronico);
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = UsuarioEntity.fromDomain(usuario);
        try {
            return repository.saveAndFlush(entity).toDomain();
        } catch (DataIntegrityViolationException exception) {
            throw new UsuarioYaExisteException();
        }
    }
}
