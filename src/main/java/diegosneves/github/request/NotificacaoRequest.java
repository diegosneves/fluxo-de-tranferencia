package diegosneves.github.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NotificacaoRequest {

    private String email;
    private String autorizacao;

}
