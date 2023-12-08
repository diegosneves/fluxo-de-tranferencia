package diegosneves.github.mapper;

import diegosneves.github.dto.UsuarioDTO;
import diegosneves.github.model.Transacao;
import diegosneves.github.response.TransacaoPagadorResponse;

/**
 * A classe {@link TransacaoPagadorResponseMapper} implementa a interface {@link EstrategiaDeMapeamento}
 * para mapear objetos do tipo {@link Transacao Transacao} para {@link TransacaoPagadorResponse}.
 */
public class TransacaoPagadorResponseMapper implements EstrategiaDeMapeamento<TransacaoPagadorResponse, Transacao> {

    @Override
    public TransacaoPagadorResponse mapear(Transacao origem) {
        TransacaoPagadorResponse response = new TransacaoPagadorResponse();
        response.setDataTransacao(origem.getDataTransacao());
        response.setHashTransacao(origem.getHashTransacao());
        response.setValorTransacao(origem.getValorTransacao());
        response.setDebitadoPara(MapearConstrutor.construirNovoDe(UsuarioDTO.class, origem.getRecebedor()));

        return response;
    }
}
