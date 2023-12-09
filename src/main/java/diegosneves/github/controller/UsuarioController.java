package diegosneves.github.controller;

import diegosneves.github.request.UsuarioRequest;
import diegosneves.github.response.TransacaoPagadorResponse;
import diegosneves.github.response.TransacaoRecebedorResponse;
import diegosneves.github.response.UsuarioResponse;
import diegosneves.github.service.TransacaoService;
import diegosneves.github.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService service;
    private final TransacaoService transacaoService;

    @Autowired
    public UsuarioController(UsuarioService service, TransacaoService transacaoService) {
        this.service = service;
        this.transacaoService = transacaoService;
    }

    /**
     * Método para obter todos os {@link diegosneves.github.model.Usuario usuários}.
     * <p>
     * Este método é responsável por retornar todos os {@link diegosneves.github.model.Usuario usuários} cadastrados.<br>
     * @return {@link ResponseEntity} com uma lista de {@link UsuarioResponse} se existirem {@link diegosneves.github.model.Usuario usuários} cadastrados, caso contrário, retorna {@link HttpStatus#NO_CONTENT status 204 - No Content}
     */
    @GetMapping("/todos")
    @Operation(summary = "Retornar todos os usuarios", tags = "Usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista com todos os usuários cadastrados", content = @Content),
            @ApiResponse(responseCode = "204", description = "No caso de não haver nenhum usuário cadastrado, nada é retornado.", content = @Content),
    })
    public ResponseEntity<List<UsuarioResponse>> obterTodosUsuarios() {
        List<UsuarioResponse> usuarioResponses = this.service.obterTodosUsuarios();

        if (usuarioResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuarioResponses);
    }

    /**
     * Obter as movimentações debitadas de um {@link diegosneves.github.model.Usuario usuário} pelo CPF.
     *
     * @param cpf O CPF do {@link diegosneves.github.model.Usuario usuário}.
     * @return {@link ResponseEntity} contendo a lista de {@link TransacaoPagadorResponse} com todas as transações debitadas.
     */
    @GetMapping("/transacoes/enviadas/{cpf}")
    @Operation(summary = "Retorna todas as transações enviadas pelo CPF especificado", tags = "Usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de todas as transações enviadas pelo CPF informado", content = @Content),
            @ApiResponse(responseCode = "204", description = "No caso de nenhuma transação ter sido enviada pelo CPF especificado, nada é retornado.", content = @Content),
    })
    public ResponseEntity<List<TransacaoPagadorResponse>> obterMovimentacoesDebitadas(@PathVariable(name = "cpf") String cpf) {
        List<TransacaoPagadorResponse> responses = this.transacaoService.obterTransacoesDebitadas(cpf);

        if (responses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responses);
    }

    /**
     * Obter as movimentações creditadas de um {@link diegosneves.github.model.Usuario usuário} pelo CPF.
     *
     * @param cpf O CPF do {@link diegosneves.github.model.Usuario usuário}.
     * @return {@link ResponseEntity} contendo a lista de {@link TransacaoRecebedorResponse} com todas as transações creditadas.
     */
    @GetMapping("/transacoes/recebidas/{cpf}")
    @Operation(summary = "Retorna todas as transações recebidas pelo CPF especificado", tags = "Usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de todas as transações recebidas pelo CPF informado", content = @Content),
            @ApiResponse(responseCode = "204", description = "No caso de nenhuma transação ter sido recebida pelo CPF especificado, nada é retornado.", content = @Content),
    })
    public ResponseEntity<List<TransacaoRecebedorResponse>> obterMovimentacoesCreditadas(@PathVariable(name = "cpf") String cpf) {
        List<TransacaoRecebedorResponse> responses = this.transacaoService.obterTransacoesCreditadas(cpf);

        if (responses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responses);
    }

    /**
     * CadastroDeUsuario é um método usado para registrar um novo {@link diegosneves.github.model.Usuario usuário} no sistema.
     *
     * @param request O objeto UsuarioRequest contendo os dados do usuário.
     * @return {@link ResponseEntity<>}<{@link UsuarioResponse}> O ResponseEntity contendo o objeto {@link UsuarioResponse} criado.
     */
    @PostMapping("/cadastro")
    @Operation(tags = "Usuários", summary = "Cadastrar um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário Cadastrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "CPF ou E-mail já cadastrado na base de dados", content = @Content),
            @ApiResponse(responseCode = "500", description = "Falha ao cadastrar o novo usuário", content = @Content)
    })
    public ResponseEntity<UsuarioResponse> cadastroDeUsuario(@RequestBody UsuarioRequest request) {
        UsuarioResponse usuarioResponse = this.service.cadastrarUsuario(request);
        return new ResponseEntity<>(usuarioResponse, HttpStatus.CREATED);
    }

}
