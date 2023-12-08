package diegosneves.github.service;

import diegosneves.github.adapter.EnviarNotificacaoAdapter;
import diegosneves.github.response.EnviarNotificacaoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A classe NotificacaoService é responsável pelo envio de notificações.
 */
@Service
public class NotificacaoService {

    private final EnviarNotificacaoAdapter notificacaoAdapter;


    @Autowired
    public NotificacaoService(EnviarNotificacaoAdapter notificacaoAdapter) {
        this.notificacaoAdapter = notificacaoAdapter;
    }

    /**
     * Envia uma notificação por e-mail.
     *
     * @param email      O endereço de e-mail do {@link diegosneves.github.model.Usuario destinatário}.
     * @param mensagem   A mensagem de notificação.
     * @return {@link Boolean#TRUE verdadeiro} se a notificação foi enviada com sucesso; caso contrário, {@link Boolean#FALSE falso}.
     */
    public Boolean enviarNotificacao(String email, String mensagem) {
        EnviarNotificacaoResponse autorizacaoDeEnvio = this.notificacaoAdapter.postAutorizacaoDeEnvio(email, mensagem);
        return autorizacaoDeEnvio.getMessage();
    }

}
