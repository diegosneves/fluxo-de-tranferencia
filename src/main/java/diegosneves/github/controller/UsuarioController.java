package diegosneves.github.controller;

import diegosneves.github.request.UsuarioRequest;
import diegosneves.github.response.UsuarioResponse;
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

    @Autowired
    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping("/todos")
    @Operation(summary = "Retornar todos os usuarios", tags = "Usuários")
    public ResponseEntity<List<UsuarioResponse>> obterTodosUsuarios() {
        List<UsuarioResponse> usuarioResponses = this.service.obterTodosUsuarios();

        if (usuarioResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuarioResponses);
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
