package diegosneves.github.repository;

import diegosneves.github.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findUsuarioByCpf(String cpf);

    Optional<Usuario> findUsuarioByCpfOrEmail(String cpf, String email);

}
