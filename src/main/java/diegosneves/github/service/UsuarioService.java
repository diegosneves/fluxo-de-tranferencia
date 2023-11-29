package diegosneves.github.service;

import diegosneves.github.enums.TipoDeUsuario;
import diegosneves.github.exception.UsuarioNaoEncontradoException;
import diegosneves.github.mapper.MapearConstrutor;
import diegosneves.github.model.Usuario;
import diegosneves.github.repository.UsuarioRepository;
import diegosneves.github.request.UsuarioRequest;
import diegosneves.github.response.UsuarioResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public UsuarioResponse cadastrarUsuario(UsuarioRequest request) {
        Usuario novoUsuario = MapearConstrutor.construirNovoDe(Usuario.class, request);

        novoUsuario = this.repository.save(novoUsuario);

        return MapearConstrutor.construirNovoDe(UsuarioResponse.class, novoUsuario);
    }
}

class Teste {
    public static void main(String[] args) {
        UsuarioRequest request = UsuarioRequest.builder()
                .senha("Teste")
                .saldo(BigDecimal.TEN)
                .nomeCompleto("Solito")
                .email("email")
                .cpf("005")
                .tipoDeUsuario(TipoDeUsuario.COMUM)
                .build();

        Usuario novoUsuario = MapearConstrutor.construirNovoDe(Usuario.class, request);

        System.out.println(novoUsuario);
    }
}
