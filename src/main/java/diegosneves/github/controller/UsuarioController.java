package diegosneves.github.controller;

import diegosneves.github.model.Usuario;
import diegosneves.github.request.UsuarioRequest;
import diegosneves.github.response.UsuarioResponse;
import diegosneves.github.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
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
    public ResponseEntity<List<Usuario>> obterTodosUsuarios() {
        List<Usuario> usuarios = this.service.obterTodosUsuarios();

        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/cadastro")
    @Operation(tags = "Usuários", summary = "Cadastrar um novo usuário")
    public ResponseEntity<UsuarioResponse> cadastroDeUsuario(@RequestBody UsuarioRequest request) {
        UsuarioResponse usuarioResponse = this.service.cadastrarUsuario(request);
        return new ResponseEntity<>(usuarioResponse, HttpStatus.CREATED);
    }

}
