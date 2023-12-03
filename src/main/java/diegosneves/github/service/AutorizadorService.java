package diegosneves.github.service;

import diegosneves.github.adapter.ServicoAutorizadorAdapter;
import diegosneves.github.model.Transacao;
import diegosneves.github.response.ServicoAutorizadorResponse;
import org.springframework.stereotype.Service;

@Service
public class AutorizadorService {

    private final ServicoAutorizadorAdapter servicoAutorizadorAdapter;


    public AutorizadorService(ServicoAutorizadorAdapter servicoAutorizadorAdapter) {
        this.servicoAutorizadorAdapter = servicoAutorizadorAdapter;
    }

    public ServicoAutorizadorResponse autorizarTransacao(Transacao transacao) {
        return this.servicoAutorizadorAdapter.postAutorizacaoParaTransferencia(transacao);
    }

}
