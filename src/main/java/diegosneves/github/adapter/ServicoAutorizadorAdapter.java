package diegosneves.github.adapter;

import diegosneves.github.dto.TransacaoDTO;
import diegosneves.github.exception.ServicoAutorizadorException;
import diegosneves.github.mapper.MapearConstrutor;
import diegosneves.github.model.Transacao;
import diegosneves.github.response.ServicoAutorizadorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Slf4j
public class ServicoAutorizadorAdapter extends HttpAdapter{

    private final String url;

    @Autowired
    public ServicoAutorizadorAdapter(@Value("${spring.api.url.servico-autorizador}") String url) {
        this.url = url;
    }

    public ServicoAutorizadorResponse postAutorizacaoParaTransferencia(Transacao transacao){
        ServicoAutorizadorResponse response;
        TransacaoDTO request = MapearConstrutor.construirNovoDe(TransacaoDTO.class, transacao);
        try {
            response = this.getRestTemplateSimpleWebClient().getRestTemplate().getForEntity(this.url, ServicoAutorizadorResponse.class).getBody();
            if (response != null && request.getValorTransacao().compareTo(BigDecimal.ONE) >= 0) {
                response.setDataDaAprovacao(LocalDateTime.now());
            }
        } catch (Exception e) {
            log.error(ServicoAutorizadorException.ERRO.mensagemDeErro(this.url), e);
            throw new ServicoAutorizadorException(this.url, e);
        }
        return response;
    }

}
