package diegosneves.github.adapter;

import diegosneves.github.exception.EnvioNotificacaoException;
import diegosneves.github.response.EnviarNotificacaoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

@Component
@Slf4j
public class EnviarNotificacaoAdapter extends HttpAdapter {

    private final String url;

    @Autowired
    public EnviarNotificacaoAdapter(@Value("${spring.api.url.enviar-notificacao}") String url) {
        this.url = url;
    }

    public EnviarNotificacaoResponse getAutorizacaoDeEnvio(){
        EnviarNotificacaoResponse response;
        try {
            response = this.getRestTemplateSimpleWebClient().getRestTemplate().getForEntity(this.url, EnviarNotificacaoResponse.class).getBody();
        } catch (RestClientException e) {
            log.error(EnvioNotificacaoException.ERRO.mensagemDeErro(this.url), e);
            throw new EnvioNotificacaoException(this.url, e);
        }
        return response;
    }

}
