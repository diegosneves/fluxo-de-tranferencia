package diegosneves.github.response;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa um objeto de resposta para uma {@link diegosneves.github.model.Transacao transação}.
 * Ele contém informações como valor da transação, status da notificação, status da transação, data da transação e hash da transação.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TransacaoResponse {

    private BigDecimal valorTransacao;
    private Boolean notificacoesEnviadas;
    private String statusDaTransacao;
    private LocalDateTime dataTransacao;
    private String hashTransacao;

}
