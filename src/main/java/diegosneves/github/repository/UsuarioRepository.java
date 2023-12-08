package diegosneves.github.repository;

import diegosneves.github.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *A interface UsuarioRepository fornece métodos para acessar e manipular entidades {@link Usuario}
 *no banco de dados.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findUsuarioByCpf(String cpf);

    Optional<Usuario> findUsuarioByCpfOrEmail(String cpf, String email);

    Optional<Usuario> findUsuarioById(Long id);

}
