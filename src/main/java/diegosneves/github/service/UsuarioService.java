package diegosneves.github.service;

import diegosneves.github.exception.UsuarioNaoEncontradoException;
import diegosneves.github.model.Usuario;
import diegosneves.github.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    @Autowired
    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario findUsuarioByCpf(String cpf) {
        Optional<Usuario> usuarioOptional = this.repository.findUsuarioByCpf(cpf);
        if (usuarioOptional.isEmpty()) {
            throw new UsuarioNaoEncontradoException(cpf);
        }
        return usuarioOptional.get();
    }


    public List<Usuario> obterTodosUsuarios() {
        return this.repository.findAll();
    }
}
