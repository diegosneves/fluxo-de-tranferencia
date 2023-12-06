package diegosneves.github.service;

import diegosneves.github.enums.TipoDeTransacao;
import diegosneves.github.enums.TipoDeUsuario;
import diegosneves.github.exception.AutorizacaoTransacaoException;
import diegosneves.github.exception.LojistaPagadorException;
import diegosneves.github.exception.SaldoInsuficienteException;
import diegosneves.github.mapper.MapearConstrutor;
import diegosneves.github.mapper.TransacaoPagadorResponseMapper;
import diegosneves.github.model.Transacao;
import diegosneves.github.model.Usuario;
import diegosneves.github.repository.TransacaoRepository;
import diegosneves.github.request.TransacaoRequest;
import diegosneves.github.response.ServicoAutorizadorResponse;
import diegosneves.github.response.TransacaoPagadorResponse;
import diegosneves.github.response.TransacaoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        Usuario recebdor = this.usuarioService.encontrarUsuarioPorId(request.getIdRecebedor());
        Usuario pagador = this.validarUsuarioPagador(this.usuarioService.encontrarUsuarioPorId(request.getIdPagador()), request.getValor());

        TransacaoResponse response = MapearConstrutor.construirNovoDe(TransacaoResponse.class, this.realizarTranferenciaFinanceira(pagador, recebdor, request.getValor()));
        response.setStatusDaTransacao(AUTORIZADO);

        response.setNotificacaoEnviadaPagador(this.enviarNotificacao(pagador.getEmail(), TipoDeTransacao.ENVIADA.enviar(recebdor.getCpf())));
        response.setNotificacaoEnviadaRecebedor(this.enviarNotificacao(recebdor.getEmail(), TipoDeTransacao.RECEBIDA.enviar(pagador.getCpf())));

        return response;
    }

    private boolean enviarNotificacao(String email, String mensagem) {
        return this.notificacaoService.enviarNotificacao(email, mensagem);
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
}
