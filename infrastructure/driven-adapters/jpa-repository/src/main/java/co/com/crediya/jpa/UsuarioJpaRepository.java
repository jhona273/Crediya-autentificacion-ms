package co.com.crediya.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioData, Long> {
    boolean existsByCorreoElectronico(String correoElectronico);
    Optional<UsuarioData> findByCorreoElectronico(String correoElectronico);
}