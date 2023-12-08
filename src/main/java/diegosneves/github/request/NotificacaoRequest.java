package diegosneves.github.request;

import lombok.*;

/**
 * Esta classe representa uma solicitação de notificação.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NotificacaoRequest {

    private String email;
    private String autorizacao;

}
