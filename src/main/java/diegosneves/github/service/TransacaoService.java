package diegosneves.github.service;

import diegosneves.github.adapter.EnviarNotificacaoAdapter;
import diegosneves.github.adapter.ServicoAutorizadorAdapter;
import diegosneves.github.enums.TipoDeUsuario;
import diegosneves.github.exception.LojistaPagadorException;
import diegosneves.github.exception.SaldoInsuficienteException;
import diegosneves.github.model.Usuario;
import diegosneves.github.repository.TransacaoRepository;
import diegosneves.github.request.TransacaoRequest;
import diegosneves.github.response.TransacaoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransacaoService {

    private final TransacaoRepository repository;
    private final EnviarNotificacaoAdapter notificacaoAdapter;
    private final ServicoAutorizadorAdapter servicoAutorizadorAdapter;

    private final UsuarioService usuarioService;

    @Autowired
    public TransacaoService(TransacaoRepository repository, EnviarNotificacaoAdapter notificacaoAdapter, ServicoAutorizadorAdapter servicoAutorizadorAdapter, UsuarioService usuarioService) {
        this.repository = repository;
        this.notificacaoAdapter = notificacaoAdapter;
        this.servicoAutorizadorAdapter = servicoAutorizadorAdapter;
        this.usuarioService = usuarioService;
    }


    public TransacaoResponse transferenciaFinanceira(TransacaoRequest request) {
        Usuario recebdor = this.usuarioService.encontrarUsuarioPorId(request.getIdRecebedor());
        Usuario pagador = validarUsuarioPagador(this.usuarioService.encontrarUsuarioPorId(request.getIdPagador()), request.getValor());
        return null;
    }

    private Usuario validarUsuarioPagador(Usuario pagador, BigDecimal valor) throws LojistaPagadorException, SaldoInsuficienteException {
        if (pagador.getTipoDeUsuario().equals(TipoDeUsuario.LOJISTA)) {
            throw new LojistaPagadorException(pagador.getCpf());
        } else if (pagador.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsuficienteException(pagador.getCpf());
        }
        return pagador;
    }
}
