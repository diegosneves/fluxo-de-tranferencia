package diegosneves.github.dto;

import diegosneves.github.model.Usuario;
import lombok.*;

import java.math.BigDecimal;
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
