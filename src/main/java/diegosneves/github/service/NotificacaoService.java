package diegosneves.github.service;

import diegosneves.github.adapter.EnviarNotificacaoAdapter;
import diegosneves.github.response.EnviarNotificacaoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {

    private final EnviarNotificacaoAdapter notificacaoAdapter;


    @Autowired
    public NotificacaoService(EnviarNotificacaoAdapter notificacaoAdapter) {
        this.notificacaoAdapter = notificacaoAdapter;
    }

    public Boolean enviarNotificacao(String email, String autorizacao) {
        EnviarNotificacaoResponse autorizacaoDeEnvio = this.notificacaoAdapter.postAutorizacaoDeEnvio(email, autorizacao);
        return autorizacaoDeEnvio.getMessage();
    }

}
