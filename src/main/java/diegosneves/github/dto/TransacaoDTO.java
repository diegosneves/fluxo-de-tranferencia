package diegosneves.github.dto;

import diegosneves.github.model.Usuario;
import lombok.*;

import java.math.BigDecimal;
/**
 * A classe TransacaoDTO representa um objeto de transferência de dados para uma {@link diegosneves.github.model.Transacao transação}.<br>
 * Contém informações sobre o pagador, o beneficiário e o valor da {@link diegosneves.github.model.Transacao transação}.
 **/
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TransacaoDTO {

    private Usuario pagador;
    private Usuario recebedor;
    private BigDecimal valorTransacao;

}
