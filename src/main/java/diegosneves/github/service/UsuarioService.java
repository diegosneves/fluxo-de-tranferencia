package diegosneves.github.service;

import diegosneves.github.enums.TipoDeUsuario;
import diegosneves.github.exception.CpfJaCadastradoException;
import diegosneves.github.exception.EmailJaCadastradoException;
import diegosneves.github.exception.UsuarioNaoEncontradoException;
import diegosneves.github.mapper.MapearConstrutor;
import diegosneves.github.model.Usuario;
import diegosneves.github.repository.UsuarioRepository;
import diegosneves.github.request.UsuarioRequest;
import diegosneves.github.response.UsuarioResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


    public List<UsuarioResponse> obterTodosUsuarios() {
        List<Usuario> usuarios = this.repository.findAll();
        return usuarios.stream()
                .map(usuario -> MapearConstrutor.construirNovoDe(UsuarioResponse.class, usuario)).collect(Collectors.toList());
    }

    public UsuarioResponse cadastrarUsuario(UsuarioRequest request) throws CpfJaCadastradoException, EmailJaCadastradoException {
        this.validarCpfAndEmail(request.getCpf(), request.getEmail());
        Usuario novoUsuario = MapearConstrutor.construirNovoDe(Usuario.class, request);

        novoUsuario = this.repository.save(novoUsuario);

        return MapearConstrutor.construirNovoDe(UsuarioResponse.class, novoUsuario);
    }

    private void validarCpfAndEmail(String cpf, String email) throws CpfJaCadastradoException, EmailJaCadastradoException {
        Optional<Usuario> usuarioOptional = this.repository.findUsuarioByCpfOrEmail(cpf, email);
        if (usuarioOptional.isPresent()) {
            if (cpf.equals(usuarioOptional.get().getCpf())) {
                throw new CpfJaCadastradoException(cpf);
            } else {
                throw new EmailJaCadastradoException(email);
            }
        }
    }
}
