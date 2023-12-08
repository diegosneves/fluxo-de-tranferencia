package diegosneves.github.service;

import diegosneves.github.enums.TipoDeTransacao;
import diegosneves.github.enums.TipoDeUsuario;
import diegosneves.github.exception.AutorizacaoTransacaoException;
import diegosneves.github.exception.IdInvalidoException;
import diegosneves.github.exception.LojistaPagadorException;
import diegosneves.github.exception.SaldoInsuficienteException;
import diegosneves.github.mapper.MapearConstrutor;
import diegosneves.github.mapper.TransacaoPagadorResponseMapper;
import diegosneves.github.mapper.TransacaoRecebedorResponseMapper;
import diegosneves.github.model.Transacao;
import diegosneves.github.model.Usuario;
import diegosneves.github.repository.TransacaoRepository;
import diegosneves.github.request.TransacaoRequest;
import diegosneves.github.response.TransacaoPagadorResponse;
import diegosneves.github.response.TransacaoRecebedorResponse;
import diegosneves.github.response.TransacaoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Esta classe representa um serviço para tratamento de transações financeiras.
 * @author diegosneves
 */
@Service
public class TransacaoService {

    private static final String AUTORIZADO = "Autorizado";
    private final TransacaoRepository repository;
    private final NotificacaoService notificacaoService;
    private final AutorizadorService autorizadorService;

    private final UsuarioService usuarioService;

    @Autowired
    public TransacaoService(TransacaoRepository repository, NotificacaoService notificacaoService, AutorizadorService autorizadorService, UsuarioService usuarioService) {
        this.repository = repository;
        this.notificacaoService = notificacaoService;
        this.autorizadorService = autorizadorService;
        this.usuarioService = usuarioService;
    }


    /**
     * Realiza uma transferência financeira entre {@link Usuario usuários}.
     *
     * @param request O objeto contendo as informações da transferência.
     *                Deve conter o valor da transferência, o ID do {@link Usuario pagador} e o ID do {@link Usuario recebedor}.
     * @return Um objeto {@link TransacaoResponse}, que contém informações sobre a {@link Transacao transação} realizada.
     * @throws LojistaPagadorException     Caso o {@link Usuario usuário} pagador seja do tipo {@link TipoDeUsuario#LOJISTA Lojista}.
     * @throws SaldoInsuficienteException  Caso o {@link Usuario usuário} pagador não possua saldo suficiente.
     * @throws IdInvalidoException         Caso o ID do {@link Usuario usuário} pagador ou do {@link Usuario usuário} recebedor seja inválido.
     * @throws AutorizacaoTransacaoException  Caso ocorra uma falha na autorização da {@link Transacao transação}.
     */
    public TransacaoResponse transferenciaFinanceira(TransacaoRequest request) {
        Usuario recebedor = this.usuarioService.encontrarUsuarioPorId(request.getIdRecebedor());
        Usuario pagador = this.validarUsuarioPagador(this.usuarioService.encontrarUsuarioPorId(request.getIdPagador()), request.getValor());

        boolean notificacaoPagadorEnviada = this.enviarNotificacaoDeTransferencia(pagador, recebedor, TipoDeTransacao.ENVIADA);
        boolean notificacaoRecebedorEnviada = this.enviarNotificacaoDeTransferencia(recebedor, pagador, TipoDeTransacao.RECEBIDA);

        return this.construirRespostaTransacao(pagador, recebedor, request.getValor(), notificacaoPagadorEnviada && notificacaoRecebedorEnviada);
    }

    /**
     * Enviara uma notificação de transferência para o {@link Usuario destinatário}.
     *
     * @param destinatario O {@link Usuario destinatário} da notificação.
     * @param participanteTransferencia O {@link Usuario participante} da transferência.
     * @param tipo O tipo({@link TipoDeTransacao#ENVIADA Enviada}, {@link TipoDeTransacao#RECEBIDA Recebida}) da transação.
     * @return O resultado da operação.
     */
    private boolean enviarNotificacaoDeTransferencia(Usuario destinatario, Usuario participanteTransferencia, TipoDeTransacao tipo) {
        return this.notificacaoService.enviarNotificacao(destinatario.getEmail(), tipo.enviar(participanteTransferencia.getCpf()));
    }

    /**
     * Constrói a {@link TransacaoResponse resposta} de uma transação com base nos parâmetros fornecidos.
     *
     * @param pagador      O {@link Usuario} que realiza o pagamento.
     * @param recebedor    O {@link Usuario} que recebe o pagamento.
     * @param valor        O valor da transação.
     * @param notificacoesEnviadas Se as notificações foram enviadas.
     * @return A {@link TransacaoResponse resposta} da transação, contendo informações sobre a transação realizada.
     */
    private TransacaoResponse construirRespostaTransacao(Usuario pagador, Usuario recebedor, BigDecimal valor, Boolean notificacoesEnviadas) {
        Transacao transacao = this.realizarTranferenciaFinanceira(pagador, recebedor, valor);
        TransacaoResponse response = MapearConstrutor.construirNovoDe(TransacaoResponse.class, transacao);
        response.setStatusDaTransacao(AUTORIZADO);
        response.setNotificacoesEnviadas(notificacoesEnviadas);
        return response;
    }

    /**
     * Realiza uma transferência financeira entre {@link Usuario usuários}.
     *
     * @param pagador O {@link Usuario usuário} que realiza o pagamento.
     * @param recebedor O {@link Usuario usuário} que recebe o pagamento.
     * @param valor O valor da transferência.
     * @return A {@link Transacao transação} realizada.
     */
    private Transacao realizarTranferenciaFinanceira(Usuario pagador, Usuario recebedor, BigDecimal valor) {
        Transacao transacao = this.autorizarTransacao(pagador, recebedor, valor);

        pagador.setSaldo(pagador.getSaldo().subtract(valor));
        recebedor.setSaldo(recebedor.getSaldo().add(valor));

        this.usuarioService.atualizarUsuarioNaBaseDeDados(pagador);
        this.usuarioService.atualizarUsuarioNaBaseDeDados(recebedor);

        return this.repository.save(transacao);
    }

    /**
     * Autoriza uma {@link Transacao transação} entre um pagador e um recebedor através de um serviço externo.
     *
     * @param pagador   O {@link Usuario usuário} que realizará o pagamento.
     * @param recebedor O {@link Usuario usuário} que receberá o pagamento.
     * @param valor     O valor da transação.
     * @return A {@link Transacao transação} autorizada.
     * @throws AutorizacaoTransacaoException Caso ocorra uma falha na autorização da {@link Transacao transação}.
     */
    private Transacao autorizarTransacao(Usuario pagador, Usuario recebedor, BigDecimal valor) throws AutorizacaoTransacaoException {
        Transacao transacao = Transacao.builder()
                .valorTransacao(valor)
                .recebedor(recebedor)
                .pagador(pagador)
                .build();

        return this.autorizadorService.autorizarTransacao(transacao);
    }

    /**
     * Valida o usuário pagador para uma transação.
     *
     * @param pagador O pagador {@link Usuario usuário}.
     * @param valor   O valor da transação.
     * @return O {@link Usuario pagador} validado.
     * @throws LojistaPagadorException    Se o {@link Usuario pagador} for um {@link TipoDeUsuario#LOJISTA LOJISTA}.
     * @throws SaldoInsuficienteException Se o {@link Usuario pagador} não tiver saldo suficiente.
     */
    private Usuario validarUsuarioPagador(Usuario pagador, BigDecimal valor) throws LojistaPagadorException, SaldoInsuficienteException {
        if (pagador.getTipoDeUsuario().equals(TipoDeUsuario.LOJISTA)) {
            throw new LojistaPagadorException(pagador.getCpf());
        } else if (pagador.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsuficienteException(pagador.getCpf());
        }
        return pagador;
    }

    /**
     * Recupera as {@link Transacao transações} debitadas para um determinado CPF.
     *
     * @param cpf O CPF do {@link Usuario usuário}.
     * @return Uma lista de {@link TransacaoPagadorResponse} contendo as informações da {@link Transacao transação} debitada.
     */
    public List<TransacaoPagadorResponse> obterTransacoesDebitadas(String cpf) {
        TransacaoPagadorResponseMapper mapper = new TransacaoPagadorResponseMapper();
        return this.repository.findTransacaoByPagador_Cpf(cpf).stream()
                .map(transacao -> MapearConstrutor.construirNovoDe(TransacaoPagadorResponse.class, transacao, mapper))
                .toList();
    }

    /**
     * Recupera as {@link Transacao transações} creditadas para um determinado CPF.
     *
     * @param cpf O CPF do {@link Usuario usuário}.
     * @return Uma lista de {@link TransacaoRecebedorResponse} contendo informações sobre as transações creditadas.
     */
    public List<TransacaoRecebedorResponse> obterTransacoesCreditadas(String cpf) {
        TransacaoRecebedorResponseMapper mapper = new TransacaoRecebedorResponseMapper();
        return this.repository.findTransacaoByRecebedor_Cpf(cpf).stream()
                .map(transacao -> MapearConstrutor.construirNovoDe(TransacaoRecebedorResponse.class, transacao, mapper)).toList();
    }
}
