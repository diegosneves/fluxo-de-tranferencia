package diegosneves.github.controller;

import diegosneves.github.request.UsuarioRequest;
import diegosneves.github.response.TransacaoPagadorResponse;
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

    @GetMapping("/debitos/{cpf}")
    @Operation(summary = "Retornar todas as transações debitadas", tags = "Usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de todas as transações debitadas pelo CPF informado", content = @Content),
            @ApiResponse(responseCode = "204", description = "No caso de nenhuma transação ter sido realizada, nada é retornado.", content = @Content),
    })
    public ResponseEntity<List<TransacaoPagadorResponse>> obterMovimentacoesDebitadas(@RequestParam(name = "cpf") String cpf) {
        List<TransacaoPagadorResponse> responses = this.transacaoService.obterTransacoesDebitadas(cpf);

        if (responses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responses);
    }

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
