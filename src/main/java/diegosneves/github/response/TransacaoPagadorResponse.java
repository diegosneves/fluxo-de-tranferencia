package diegosneves.github.response;


import diegosneves.github.dto.UsuarioDTO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
