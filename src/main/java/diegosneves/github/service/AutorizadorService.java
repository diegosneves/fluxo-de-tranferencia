package diegosneves.github.service;

import diegosneves.github.adapter.ServicoAutorizadorAdapter;
import diegosneves.github.exception.AutorizacaoTransacaoException;
import diegosneves.github.exception.ServicoAutorizadorException;
import diegosneves.github.model.Transacao;
import diegosneves.github.response.ServicoAutorizadorResponse;
import org.springframework.stereotype.Service;

@Service
public class AutorizadorService {

    private static final String AUTORIZADO = "Autorizado";

    private final ServicoAutorizadorAdapter servicoAutorizadorAdapter;


    public AutorizadorService(ServicoAutorizadorAdapter servicoAutorizadorAdapter) {
        this.servicoAutorizadorAdapter = servicoAutorizadorAdapter;
    }

    public Transacao autorizarTransacao(Transacao transacao) throws AutorizacaoTransacaoException, ServicoAutorizadorException {
        ServicoAutorizadorResponse autorizacaoParaTransferencia = this.servicoAutorizadorAdapter.postAutorizacaoParaTransferencia(transacao);

        if (!AUTORIZADO.equals(autorizacaoParaTransferencia.getMessage()) || autorizacaoParaTransferencia.getDataDaAprovacao() == null) {
            throw new AutorizacaoTransacaoException(transacao.getValorTransacao().toString());
        }
        transacao.setDataTransacao(autorizacaoParaTransferencia.getDataDaAprovacao());
        return transacao;
    }

}
