package diegosneves.github.adapter;

import diegosneves.github.exception.ServicoAutorizadorException;
import diegosneves.github.response.ServicoAutorizadorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

@Component
@Slf4j
public class ServicoAutorizadorAdapter extends HttpAdapter{

    private final String url;

    @Autowired
    public ServicoAutorizadorAdapter(@Value("${spring.api.url.servico-autorizador}") String url) {
        this.url = url;
    }

    public ServicoAutorizadorResponse getAutorizacaoParaTransferencia(){
        ServicoAutorizadorResponse response;
        try {
            response = this.getRestTemplateSimpleWebClient().getRestTemplate().getForEntity(this.url, ServicoAutorizadorResponse.class).getBody();
        } catch (RestClientException e) {
            log.error(ServicoAutorizadorException.ERRO.mensagemDeErro(this.url), e);
            throw new ServicoAutorizadorException(this.url, e);
        }
        return response;
    }

}
