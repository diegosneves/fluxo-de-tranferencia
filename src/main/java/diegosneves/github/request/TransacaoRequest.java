package diegosneves.github.request;

import lombok.*;

import java.math.BigDecimal;

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
