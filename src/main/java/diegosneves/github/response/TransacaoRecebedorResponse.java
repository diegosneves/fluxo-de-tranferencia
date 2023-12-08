package diegosneves.github.response;


import diegosneves.github.dto.UsuarioDTO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Esta classe representa a resposta de uma {@link diegosneves.github.model.Transacao transação} recebida pelo {@link diegosneves.github.model.Usuario destinatário}.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TransacaoRecebedorResponse {

    private UsuarioDTO creditadoDe;
    private BigDecimal valorTransacao;
    private LocalDateTime dataTransacao;
    private String hashTransacao;

}
