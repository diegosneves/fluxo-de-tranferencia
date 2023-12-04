package diegosneves.github.service;

import diegosneves.github.adapter.EnviarNotificacaoAdapter;
import diegosneves.github.adapter.ServicoAutorizadorAdapter;
import diegosneves.github.enums.TipoDeUsuario;
import diegosneves.github.exception.LojistaPagadorException;
import diegosneves.github.exception.ManipuladorDeErro;
import diegosneves.github.exception.SaldoInsuficienteException;
import diegosneves.github.model.Transacao;
import diegosneves.github.model.Usuario;
import diegosneves.github.repository.TransacaoRepository;
import diegosneves.github.request.TransacaoRequest;
import diegosneves.github.response.TransacaoResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TransacaoServiceTest {

    @InjectMocks
    private TransacaoService service;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private TransacaoRepository repository;
    @Mock
    private NotificacaoService notificacaoService;
    @Mock
    private AutorizadorService autorizadorService;

    private Transacao transacao;
    private TransacaoRequest request;
    private TransacaoResponse response;
    private Usuario pagador;
    private Usuario recebedor;

    @BeforeEach
    void setUp() {
        this.pagador = Usuario.builder()
                .id(1L)
                .nomeCompleto("Fulando do Teste")
                .cpf("32212200650")
                .email("pagador@teste.com.br")
                .senha("Teste@123")
                .tipoDeUsuario(TipoDeUsuario.COMUM)
                .saldo(BigDecimal.valueOf(200.0))
                .build();

        this.recebedor = Usuario.builder()
                .id(2L)
                .nomeCompleto("Beltrano do Teste")
                .cpf("44914562839")
                .email("recebedor@teste.com.br")
                .senha("Teste@123")
                .tipoDeUsuario(TipoDeUsuario.COMUM)
                .saldo(BigDecimal.valueOf(200.0))
                .build();
    }

    @Test
    void quandoReceberUmaTransacaoRequestComTodosDadosOkEntaoUmaTransacaoDeveSerRegistrada() {
        when(this.usuarioService.encontrarUsuarioPorId(1L)).thenReturn(this.pagador);
        when(this.usuarioService.encontrarUsuarioPorId(2L)).thenReturn(this.recebedor);

    }

    @Test
    @SneakyThrows
    void quandoEnviarNotificaoReceberEmailAndMensagemEntaoTrueDeverRetornadoEmCasoDeSucesso() {
        when(this.notificacaoService.enviarNotificacao(anyString(), anyString())).thenReturn(true);

        Method method = this.service.getClass().getDeclaredMethod("enviarNotificacao", String.class, String.class);
        method.setAccessible(true);

        Boolean retorno = (Boolean) method.invoke(this.service, "email", "mensagem");

        assertTrue(retorno);

    }

    @Test
    @SneakyThrows
    void quandoValidarUsuarioPagadorReceverUmUsuarioComumComSaldoSuficienteEntaoUsuarioDeveSerRetornando() {
        Method method = this.service.getClass().getDeclaredMethod("validarUsuarioPagador", Usuario.class, BigDecimal.class);
        method.setAccessible(true);

        Usuario actual = (Usuario) method.invoke(this.service, this.pagador, BigDecimal.TEN);

        assertEquals(this.pagador, actual);

    }

    @Test
    @SneakyThrows
    void quandoValidarUsuarioPagadorReceverUmUsuarioLojistaComSaldoSuficienteEntaoLojistaPagadorExceptionDeveSerLancada() {
        this.pagador.setTipoDeUsuario(TipoDeUsuario.LOJISTA);

        Method method = this.service.getClass().getDeclaredMethod("validarUsuarioPagador", Usuario.class, BigDecimal.class);
        method.setAccessible(true);

        InvocationTargetException actual =  assertThrows(InvocationTargetException.class, () -> method.invoke(this.service, this.pagador, BigDecimal.TEN));
        Throwable realException = actual.getTargetException();

        assertTrue(realException instanceof LojistaPagadorException);
        assertEquals(ManipuladorDeErro.LOJISTA_PAGADOR.mensagemDeErro(this.pagador.getCpf()), realException.getMessage());

    }

    @Test
    @SneakyThrows
    void quandoValidarUsuarioPagadorReceverUmUsuarioComumComSaldoInsuficienteEntaoSaldoInsuficienteExceptionDeveSerLancada() {
        this.pagador.setSaldo(BigDecimal.ONE);

        Method method = this.service.getClass().getDeclaredMethod("validarUsuarioPagador", Usuario.class, BigDecimal.class);
        method.setAccessible(true);

        InvocationTargetException actual =  assertThrows(InvocationTargetException.class, () -> method.invoke(this.service, this.pagador, BigDecimal.TEN));
        Throwable realException = actual.getTargetException();

        assertTrue(realException instanceof SaldoInsuficienteException);
        assertEquals(ManipuladorDeErro.SALDO_INSUFICIENTE.mensagemDeErro(this.pagador.getCpf()), realException.getMessage());

    }

}
