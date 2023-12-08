package diegosneves.github.service;

import diegosneves.github.exception.CpfJaCadastradoException;
import diegosneves.github.exception.EmailJaCadastradoException;
import diegosneves.github.exception.CpfNaoEncontradoException;
import diegosneves.github.exception.IdInvalidoException;
import diegosneves.github.mapper.MapearConstrutor;
import diegosneves.github.model.Usuario;
import diegosneves.github.repository.UsuarioRepository;
import diegosneves.github.request.UsuarioRequest;
import diegosneves.github.response.UsuarioResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Esta classe representa um serviço para gerenciamento de usuários.
 *
 * @author diegosneves
 */
@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    @Autowired
    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    /**
     * Encontra um Usuário por CPF.
     *
     * @param cpf o CPF do Usuário para encontrar
     * @return o usuário encontrado
     * @throws CpfNaoEncontradoException se o Usuário com o CPF informado não for encontrado.
     */
    public Usuario findUsuarioByCpf(String cpf) {
        Optional<Usuario> usuarioOptional = this.repository.findUsuarioByCpf(cpf);
        if (usuarioOptional.isEmpty()) {
            throw new CpfNaoEncontradoException(cpf);
        }
        return usuarioOptional.get();
    }


    /**
     * Obtém todos os usuários.
     * <p>
     * Este método é responsável por buscar todos os usuários cadastrados.
     *
     * @return A {@link List} of {@link UsuarioResponse} contendo todos os usuários, ou uma lista vazia se nenhum usuário estiver registrado.
     */
    public List<UsuarioResponse> obterTodosUsuarios() {
        List<Usuario> usuarios = this.repository.findAll();
        return usuarios.stream()
                .map(usuario -> MapearConstrutor.construirNovoDe(UsuarioResponse.class, usuario)).toList();
    }

    /**
     * Cadastra um novo {@link Usuario usuário}.
     *
     * @param request O objeto {@link UsuarioRequest} contendo as informações do usuário a ser cadastrado.
     * @return O objeto {@link UsuarioResponse} representando o usuário cadastrado.
     * @throws CpfJaCadastradoException    se o CPF já estiver cadastrado para outro usuário.
     * @throws EmailJaCadastradoException  se o email já estiver cadastrado para outro usuário.
     */
    public UsuarioResponse cadastrarUsuario(UsuarioRequest request) throws CpfJaCadastradoException, EmailJaCadastradoException {
        this.validarCpfAndEmail(request.getCpf(), request.getEmail());
        Usuario novoUsuario = MapearConstrutor.construirNovoDe(Usuario.class, request);

        novoUsuario = this.repository.save(novoUsuario);

        return MapearConstrutor.construirNovoDe(UsuarioResponse.class, novoUsuario);
    }

    /**
     * Valida o CPF e email informados de um {@link Usuario usuário}.
     *
     * @param cpf   o CPF do {@link Usuario usuário} para validar
     * @param email o e-mail do {@link Usuario usuário} para validar
     * @throws CpfJaCadastradoException   se o CPF já estiver cadastrado para outro {@link Usuario usuário}
     * @throws EmailJaCadastradoException se o e-mail já estiver cadastrado para outro {@link Usuario usuário}
     */
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

    /**
     * Encontra um {@link Usuario usuário} por ID.
     *
     * @param idUsuario o ID do {@link Usuario usuário} para encontrar
     * @return o {@link Usuario usuário} encontrado
     * @throws IdInvalidoException se o ID do {@link Usuario usuário} informado for inválido ou não for encontrado
     */
    public Usuario encontrarUsuarioPorId(Long idUsuario) throws IdInvalidoException {
        Optional<Usuario> usuarioOptional = this.repository.findUsuarioById(idUsuario);
        if (usuarioOptional.isEmpty()) {
            throw new IdInvalidoException(idUsuario.toString());
        }
        return usuarioOptional.get();
    }

    /**
     * Atualiza um {@link Usuario usuário} na base de dados.
     *
     * @param usuario o objeto {@link Usuario Usuário} a ser atualizado
     */
    public void atualizarUsuarioNaBaseDeDados(Usuario usuario) {
        this.repository.save(usuario);
    }

}
