package diegosneves.github.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * O ServicoAutorizadorResponse representa a resposta recebida do {@link diegosneves.github.adapter.ServicoAutorizadorAdapter Serviço Externo}.
 * Contém a mensagem da resposta e a data de aprovação.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ServicoAutorizadorResponse {

    private String message;
    private LocalDateTime dataDaAprovacao;

}
