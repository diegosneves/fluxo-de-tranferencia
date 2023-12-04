package diegosneves.github.adapter;

import diegosneves.github.exception.EnvioNotificacaoException;
import diegosneves.github.request.NotificacaoRequest;
import diegosneves.github.response.EnviarNotificacaoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EnviarNotificacaoAdapter extends HttpAdapter {

    private final String url;

    @Autowired
    public EnviarNotificacaoAdapter(@Value("${spring.api.url.enviar-notificacao}") String url) {
        this.url = url;
    }

    public EnviarNotificacaoResponse postAutorizacaoDeEnvio(String email, String autorizacao) throws EnvioNotificacaoException {
        EnviarNotificacaoResponse response;
        NotificacaoRequest request = NotificacaoRequest.builder()
                .email(email)
                .autorizacao(autorizacao)
                .build();
        try {
            response = this.getRestTemplateSimpleWebClient().getRestTemplate().postForEntity(this.url, request, EnviarNotificacaoResponse.class).getBody();
        } catch (Exception e) {
            log.error(EnvioNotificacaoException.ERRO.mensagemDeErro(this.url), e);
            throw new EnvioNotificacaoException(this.url, e);
        }
        return response;
    }

}
