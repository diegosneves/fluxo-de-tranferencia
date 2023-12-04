package diegosneves.github.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ServicoAutorizadorResponse {

    private String message;
    private LocalDateTime dataDaAprovacao;

}
