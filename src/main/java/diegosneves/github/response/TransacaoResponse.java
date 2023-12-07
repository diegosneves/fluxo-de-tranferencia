package diegosneves.github.response;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
