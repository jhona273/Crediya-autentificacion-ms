package co.com.crediya.jpa;

import co.com.crediya.jpa.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JPARepository extends JpaRepository<UsuarioEntity, UUID> {

    boolean existsByCorreoElectronicoIgnoreCase(String correoElectronico);
}
