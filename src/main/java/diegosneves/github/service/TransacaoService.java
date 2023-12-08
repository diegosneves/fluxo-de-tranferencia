package diegosneves.github.service;

import diegosneves.github.enums.TipoDeTransacao;
import diegosneves.github.enums.TipoDeUsuario;
import diegosneves.github.exception.AutorizacaoTransacaoException;
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


    public TransacaoResponse transferenciaFinanceira(TransacaoRequest request) {
        Usuario recebedor = this.usuarioService.encontrarUsuarioPorId(request.getIdRecebedor());
        Usuario pagador = this.validarUsuarioPagador(this.usuarioService.encontrarUsuarioPorId(request.getIdPagador()), request.getValor());

        boolean notificacaoPagadorEnviada = this.enviarNotificacaoDeTransferencia(pagador, recebedor, TipoDeTransacao.ENVIADA);
        boolean notificacaoRecebedorEnviada = this.enviarNotificacaoDeTransferencia(recebedor, pagador, TipoDeTransacao.RECEBIDA);

        return this.construirRespostaTransacao(pagador, recebedor, request.getValor(), notificacaoPagadorEnviada && notificacaoRecebedorEnviada);
    }

    private boolean enviarNotificacaoDeTransferencia(Usuario destinatario, Usuario participanteTransferencia, TipoDeTransacao tipo) {
        return this.notificacaoService.enviarNotificacao(destinatario.getEmail(), tipo.enviar(participanteTransferencia.getCpf()));
    }

    private TransacaoResponse construirRespostaTransacao(Usuario pagador, Usuario recebedor, BigDecimal valor, Boolean notificacoesEnviadas) {
        Transacao transacao = this.realizarTranferenciaFinanceira(pagador, recebedor, valor);
        TransacaoResponse response = MapearConstrutor.construirNovoDe(TransacaoResponse.class, transacao);
        response.setStatusDaTransacao(AUTORIZADO);
        response.setNotificacoesEnviadas(notificacoesEnviadas);
        return response;
    }

    private Transacao realizarTranferenciaFinanceira(Usuario pagador, Usuario recebdor, BigDecimal valor) {
        Transacao transacao = this.autorizarTransacao(pagador, recebdor, valor);

        pagador.setSaldo(pagador.getSaldo().subtract(valor));
        recebdor.setSaldo(recebdor.getSaldo().add(valor));

        this.usuarioService.atualizarUsuarioNaBaseDeDados(pagador);
        this.usuarioService.atualizarUsuarioNaBaseDeDados(recebdor);

        return this.repository.save(transacao);
    }

    private Transacao autorizarTransacao(Usuario pagador, Usuario recebdor, BigDecimal valor) throws AutorizacaoTransacaoException {
        Transacao transacao = Transacao.builder()
                .valorTransacao(valor)
                .recebedor(recebdor)
                .pagador(pagador)
                .build();

        return this.autorizadorService.autorizarTransacao(transacao);
    }

    private Usuario validarUsuarioPagador(Usuario pagador, BigDecimal valor) throws LojistaPagadorException, SaldoInsuficienteException {
        if (pagador.getTipoDeUsuario().equals(TipoDeUsuario.LOJISTA)) {
            throw new LojistaPagadorException(pagador.getCpf());
        } else if (pagador.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsuficienteException(pagador.getCpf());
        }
        return pagador;
    }

    public List<TransacaoPagadorResponse> obterTransacoesDebitadas(String cpf) {
        TransacaoPagadorResponseMapper mapper = new TransacaoPagadorResponseMapper();
        return this.repository.findTransacaoByPagador_Cpf(cpf).stream()
                .map(transacao -> MapearConstrutor.construirNovoDe(TransacaoPagadorResponse.class, transacao, mapper))
                .toList();
    }

    public List<TransacaoRecebedorResponse> obterTransacoesCreditadas(String cpf) {
        TransacaoRecebedorResponseMapper mapper = new TransacaoRecebedorResponseMapper();
        return this.repository.findTransacaoByRecebedor_Cpf(cpf).stream()
                .map(transacao -> MapearConstrutor.construirNovoDe(TransacaoRecebedorResponse.class, transacao, mapper)).toList();
    }
}
