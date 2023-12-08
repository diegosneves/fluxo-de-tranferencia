package diegosneves.github.controller;

import diegosneves.github.exception.AutorizacaoTransacaoException;
import diegosneves.github.exception.IdInvalidoException;
import diegosneves.github.exception.LojistaPagadorException;
import diegosneves.github.exception.SaldoInsuficienteException;
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

/**
 * A classe do controlador para lidar com transações.
 */
@RestController
@RequestMapping("/transacao")
public class TransacaoController {

    private final TransacaoService service;


    @Autowired
    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    /**
     * Realiza uma transferência financeira entre usuários.
     *
     * @param request O objeto contendo as informações da transferência.
     *                Deve conter o valor da transferência, o ID do {@link diegosneves.github.model.Usuario pagador} e o ID do {@link diegosneves.github.model.Usuario recebedor}.
     * @return Um objeto ResponseEntity contendo a resposta da transferência.
     *         O corpo da resposta é um objeto {@link TransacaoResponse}, que contém informações sobre a {@link diegosneves.github.model.Transacao transação} realizada.
     * @throws LojistaPagadorException     Caso o usuário pagador seja do tipo Lojista.
     * @throws SaldoInsuficienteException  Caso o usuário pagador não possua saldo suficiente.
     * @throws IdInvalidoException         Caso o ID do usuário pagador ou do usuário recebedor seja inválido.
     * @throws AutorizacaoTransacaoException  Caso ocorra uma falha na autorização da transação.
     */
    @PostMapping("/transferencia")
    @Transactional
    @Operation(tags = "Transação", summary = "Realiza uma transferência financeira entre usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso.", content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    ID do usuário informado, não existe. |\s
                    Quando há uma falha ao tentar se conectar a API externa para autorizar a transação. |\s
                    Quando há uma falha ao tentar se conectar a API externa utilizada para notificar os usuários.""", content = @Content),
            @ApiResponse(responseCode = "400", description = """
                    Transferência não autorizada. |\s
                    Quando o usuário pagador informado é do tipo Lojista. |\s
                    Quando o usuário pagador não possui saldo suficiente.""", content = @Content)
    })
    public ResponseEntity<TransacaoResponse> realizarTransferencia(@RequestBody TransacaoRequest request) {
        TransacaoResponse response = this.service.transferenciaFinanceira(request);
        return ResponseEntity.ok(response);
    }

}
