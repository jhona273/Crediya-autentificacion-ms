package co.com.crediya.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioData, Long> {
    boolean existsByCorreoElectronico(String correoElectronico);
}