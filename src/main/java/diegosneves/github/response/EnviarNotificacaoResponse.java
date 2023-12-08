package diegosneves.github.response;

import lombok.*;

/**
 * Esta classe representa a resposta do envio de uma notificação.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EnviarNotificacaoResponse {

    private Boolean message;

}
