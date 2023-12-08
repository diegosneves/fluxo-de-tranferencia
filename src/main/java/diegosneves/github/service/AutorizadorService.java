package diegosneves.github.service;

import diegosneves.github.adapter.ServicoAutorizadorAdapter;
import diegosneves.github.enums.HashEncoder;
import diegosneves.github.exception.AutorizacaoTransacaoException;
import diegosneves.github.exception.ServicoAutorizadorException;
import diegosneves.github.model.Transacao;
import diegosneves.github.response.ServicoAutorizadorResponse;
import org.springframework.stereotype.Service;

/**
 * A classe AutorizadorService é responsável por autorizar uma {@link Transacao transação} e gerar um {@link HashEncoder#SHA_256 hash} de transação.
 */
@Service
public class AutorizadorService {

    private static final String AUTORIZADO = "Autorizado";

    private final ServicoAutorizadorAdapter servicoAutorizadorAdapter;


    public AutorizadorService(ServicoAutorizadorAdapter servicoAutorizadorAdapter) {
        this.servicoAutorizadorAdapter = servicoAutorizadorAdapter;
    }

    /**
     * Autoriza uma transação chamando um serviço externo para autorizar a {@link Transacao transação} e gerar um {@link HashEncoder#SHA_256 hash} de transação.
     *
     * @param transacao A {@link Transacao transação} a ser autorizada.
     * @return A transação autorizada com um {@link HashEncoder#SHA_256 hash} gerado.
     * @throws AutorizacaoTransacaoException    Se a {@link Transacao transação} não for autorizada.
     * @throws ServicoAutorizadorException     Se houver um erro com o serviço de autorização.
     */
    public Transacao autorizarTransacao(Transacao transacao) throws AutorizacaoTransacaoException, ServicoAutorizadorException {
        ServicoAutorizadorResponse autorizacaoParaTransferencia = this.servicoAutorizadorAdapter.postAutorizacaoParaTransferencia(transacao);

        if (!AUTORIZADO.equals(autorizacaoParaTransferencia.getMessage()) || autorizacaoParaTransferencia.getDataDaAprovacao() == null) {
            throw new AutorizacaoTransacaoException(transacao.getValorTransacao().toString());
        }
        transacao.setDataTransacao(autorizacaoParaTransferencia.getDataDaAprovacao());
        return gerarHashTransacao(transacao);
    }

    /**
     * Gera um {@link HashEncoder#SHA_256 hash} para uma determinada {@link Transacao transação}.
     *
     * @param transacao A {@link Transacao transação} para a qual gerar o {@link HashEncoder#SHA_256 hash}.
     * @return A {@link Transacao transação} com o {@link HashEncoder#SHA_256 hash} gerado.
     */
    private Transacao gerarHashTransacao(Transacao transacao) {
        transacao.setHashTransacao(HashEncoder.SHA_256.encode(transacao.getPagador().getCpf() + transacao.getRecebedor().getCpf() + transacao.getDataTransacao()));
        return transacao;
    }

}
