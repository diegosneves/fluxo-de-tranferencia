package diegosneves.github.response;


import diegosneves.github.dto.UsuarioDTO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Esta classe representa uma resposta para uma {@link diegosneves.github.model.Transacao transação} feita por um {@link diegosneves.github.model.Usuario pagador}.
 * Ele contém informações como pagador, valor da transação, data da transação e hash da transação.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TransacaoPagadorResponse {

    private UsuarioDTO debitadoPara;
    private BigDecimal valorTransacao;
    private LocalDateTime dataTransacao;
    private String hashTransacao;

}
