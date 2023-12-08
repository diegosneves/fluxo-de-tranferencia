package diegosneves.github.mapper;

import diegosneves.github.dto.UsuarioDTO;
import diegosneves.github.model.Transacao;
import diegosneves.github.response.TransacaoRecebedorResponse;

/**
 * A classe {@link TransacaoRecebedorResponseMapper} implementa a interface {@link EstrategiaDeMapeamento} para mapear objetos {@link Transacao} para objetos {@link TransacaoRecebedorResponse}.
 */
public class TransacaoRecebedorResponseMapper implements EstrategiaDeMapeamento<TransacaoRecebedorResponse, Transacao> {
    @Override
    public TransacaoRecebedorResponse mapear(Transacao origem) {
        TransacaoRecebedorResponse response = new TransacaoRecebedorResponse();
        response.setDataTransacao(origem.getDataTransacao());
        response.setValorTransacao(origem.getValorTransacao());
        response.setCreditadoDe(MapearConstrutor.construirNovoDe(UsuarioDTO.class, origem.getPagador()));
        response.setHashTransacao(origem.getHashTransacao());
        return response;
    }
}
