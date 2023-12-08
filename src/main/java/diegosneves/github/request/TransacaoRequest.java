package diegosneves.github.request;

import lombok.*;

import java.math.BigDecimal;

/**
 * Representa uma solicitação de {@link diegosneves.github.model.Transacao transação}.
 *
 * <p>Exemplo de uso:</p>
 * <pre>{@code
 * TransacaoRequest request = TransacaoRequest.builder()
 *             .idPagador(1L)
 *             .idRecebedor(2L)
 *             .valor(BigDecimal.TEN)
 *             .build();
 * }</pre>
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TransacaoRequest {

    private BigDecimal valor;
    private Long idPagador;
    private Long idRecebedor;

}
