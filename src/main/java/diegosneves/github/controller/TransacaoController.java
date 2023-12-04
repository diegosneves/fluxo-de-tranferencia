package diegosneves.github.controller;

import diegosneves.github.request.TransacaoRequest;
import diegosneves.github.response.TransacaoResponse;
import diegosneves.github.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {

    private final TransacaoService service;


    @Autowired
    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    @PostMapping("/transferencia")
    @Transactional
    @Operation(tags = "Transação", summary = "Realiza uma transferência financeira entre usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso.", content = @Content)
    })
    public ResponseEntity<TransacaoResponse> realizarTransferencia(@RequestBody TransacaoRequest request) {
        TransacaoResponse response = this.service.transferenciaFinanceira(request);
        return ResponseEntity.ok(response);
    }

}
