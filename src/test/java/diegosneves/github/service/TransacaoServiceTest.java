package diegosneves.github.service;

import diegosneves.github.enums.TipoDeTransacao;
import diegosneves.github.enums.TipoDeUsuario;
import diegosneves.github.exception.*;
import diegosneves.github.model.Transacao;
import diegosneves.github.model.Usuario;
import diegosneves.github.repository.TransacaoRepository;
import diegosneves.github.request.TransacaoRequest;
import diegosneves.github.response.TransacaoPagadorResponse;
import diegosneves.github.response.TransacaoRecebedorResponse;
import diegosneves.github.response.TransacaoResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

    @Captor
    private ArgumentCaptor<Transacao> transacaoCaptor;

    private Transacao transacao;
    private TransacaoRequest request;
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

        this.transacao = Transacao.builder()
                .id(1L)
                .valorTransacao(BigDecimal.TEN)
                .recebedor(this.recebedor)
                .pagador(this.pagador)
                .build();

        this.request = TransacaoRequest.builder()
                .idPagador(1L)
                .idRecebedor(2L)
                .valor(BigDecimal.TEN)
                .build();
    }

    @Test
    void quandoReceberUmaTransacaoRequestComTodosDadosOkEntaoUmaTransacaoDeveSerRegistrada() {
        this.transacao.setRecebedor(null);
        this.transacao.setPagador(null);
        this.transacao.setDataTransacao(LocalDateTime.now());

        when(this.usuarioService.encontrarUsuarioPorId(1L)).thenReturn(this.pagador);
        when(this.usuarioService.encontrarUsuarioPorId(2L)).thenReturn(this.recebedor);
        when(this.autorizadorService.autorizarTransacao(any(Transacao.class))).thenReturn(this.transacao);
        doNothing().when(this.usuarioService).atualizarUsuarioNaBaseDeDados(this.pagador);
        doNothing().when(this.usuarioService).atualizarUsuarioNaBaseDeDados(this.recebedor);
        when(this.repository.save(this.transacao)).thenReturn(this.transacao);
        when(this.notificacaoService.enviarNotificacao(this.pagador.getEmail(), TipoDeTransacao.ENVIADA.enviar(this.recebedor.getCpf()))).thenReturn(true);
        when(this.notificacaoService.enviarNotificacao(this.recebedor.getEmail(), TipoDeTransacao.RECEBIDA.enviar(this.pagador.getCpf()))).thenReturn(true);

        TransacaoResponse resultado = this.service.transferenciaFinanceira(this.request);

        verify(this.autorizadorService, times(1)).autorizarTransacao(this.transacaoCaptor.capture());
        verify(this.repository, times(1)).save(any(Transacao.class));
        verify(this.usuarioService, times(2)).atualizarUsuarioNaBaseDeDados(any(Usuario.class));
        verify(this.usuarioService, times(2)).encontrarUsuarioPorId(anyLong());

        assertEquals(this.pagador,this.transacaoCaptor.getValue().getPagador());
        assertEquals(this.recebedor,this.transacaoCaptor.getValue().getRecebedor());
        assertEquals(BigDecimal.TEN, resultado.getValorTransacao());
        assertEquals("Autorizado", resultado.getStatusDaTransacao());
        assertTrue(resultado.getNotificacoesEnviadas());
        assertNotNull(resultado.getDataTransacao());
    }

    @Test
    void quandoReceberUmaTransacaoRequestComTodosDadosOkMasNotificacaoAoPagadorNaoForEnviadaEntaoUmaTransacaoResponseComNotificacoesEnviadasFalsaDeveSerRetornada() {
        this.transacao.setRecebedor(null);
        this.transacao.setPagador(null);
        this.transacao.setDataTransacao(LocalDateTime.now());

        when(this.usuarioService.encontrarUsuarioPorId(1L)).thenReturn(this.pagador);
        when(this.usuarioService.encontrarUsuarioPorId(2L)).thenReturn(this.recebedor);
        when(this.autorizadorService.autorizarTransacao(any(Transacao.class))).thenReturn(this.transacao);
        doNothing().when(this.usuarioService).atualizarUsuarioNaBaseDeDados(this.pagador);
        doNothing().when(this.usuarioService).atualizarUsuarioNaBaseDeDados(this.recebedor);
        when(this.repository.save(this.transacao)).thenReturn(this.transacao);
        when(this.notificacaoService.enviarNotificacao(this.pagador.getEmail(), TipoDeTransacao.ENVIADA.enviar(this.recebedor.getCpf()))).thenReturn(false);
        when(this.notificacaoService.enviarNotificacao(this.recebedor.getEmail(), TipoDeTransacao.RECEBIDA.enviar(this.pagador.getCpf()))).thenReturn(true);

        TransacaoResponse resultado = this.service.transferenciaFinanceira(this.request);

        verify(this.autorizadorService, times(1)).autorizarTransacao(this.transacaoCaptor.capture());
        verify(this.repository, times(1)).save(any(Transacao.class));
        verify(this.usuarioService, times(2)).atualizarUsuarioNaBaseDeDados(any(Usuario.class));
        verify(this.usuarioService, times(2)).encontrarUsuarioPorId(anyLong());

        assertEquals(this.pagador,this.transacaoCaptor.getValue().getPagador());
        assertEquals(this.recebedor,this.transacaoCaptor.getValue().getRecebedor());
        assertEquals(BigDecimal.TEN, resultado.getValorTransacao());
        assertEquals("Autorizado", resultado.getStatusDaTransacao());
        assertFalse(resultado.getNotificacoesEnviadas());
        assertNotNull(resultado.getDataTransacao());
    }

    @Test
    void quandoReceberUmaTransacaoRequestComTodosDadosOkMasNotificacaoAoPagadorERecebedorNaoForEnviadaEntaoUmaTransacaoResponseComNotificacoesEnviadasFalsaDeveSerRetornada() {
        this.transacao.setRecebedor(null);
        this.transacao.setPagador(null);
        this.transacao.setDataTransacao(LocalDateTime.now());

        when(this.usuarioService.encontrarUsuarioPorId(1L)).thenReturn(this.pagador);
        when(this.usuarioService.encontrarUsuarioPorId(2L)).thenReturn(this.recebedor);
        when(this.autorizadorService.autorizarTransacao(any(Transacao.class))).thenReturn(this.transacao);
        doNothing().when(this.usuarioService).atualizarUsuarioNaBaseDeDados(this.pagador);
        doNothing().when(this.usuarioService).atualizarUsuarioNaBaseDeDados(this.recebedor);
        when(this.repository.save(this.transacao)).thenReturn(this.transacao);
        when(this.notificacaoService.enviarNotificacao(this.pagador.getEmail(), TipoDeTransacao.ENVIADA.enviar(this.recebedor.getCpf()))).thenReturn(false);
        when(this.notificacaoService.enviarNotificacao(this.recebedor.getEmail(), TipoDeTransacao.RECEBIDA.enviar(this.pagador.getCpf()))).thenReturn(false);

        TransacaoResponse resultado = this.service.transferenciaFinanceira(this.request);

        verify(this.autorizadorService, times(1)).autorizarTransacao(this.transacaoCaptor.capture());
        verify(this.repository, times(1)).save(any(Transacao.class));
        verify(this.usuarioService, times(2)).atualizarUsuarioNaBaseDeDados(any(Usuario.class));
        verify(this.usuarioService, times(2)).encontrarUsuarioPorId(anyLong());

        assertEquals(this.pagador,this.transacaoCaptor.getValue().getPagador());
        assertEquals(this.recebedor,this.transacaoCaptor.getValue().getRecebedor());
        assertEquals(BigDecimal.TEN, resultado.getValorTransacao());
        assertEquals("Autorizado", resultado.getStatusDaTransacao());
        assertFalse(resultado.getNotificacoesEnviadas());
        assertNotNull(resultado.getDataTransacao());
    }

    @Test
    void quandoReceberUmaTransacaoRequestComTodosDadosOkMasNotificacaoAoRecebedorNaoForEnviadaEntaoUmaTransacaoResponseComNotificacoesEnviadasFalsaDeveSerRetornada() {
        this.transacao.setRecebedor(null);
        this.transacao.setPagador(null);
        this.transacao.setDataTransacao(LocalDateTime.now());

        when(this.usuarioService.encontrarUsuarioPorId(1L)).thenReturn(this.pagador);
        when(this.usuarioService.encontrarUsuarioPorId(2L)).thenReturn(this.recebedor);
        when(this.autorizadorService.autorizarTransacao(any(Transacao.class))).thenReturn(this.transacao);
        doNothing().when(this.usuarioService).atualizarUsuarioNaBaseDeDados(this.pagador);
        doNothing().when(this.usuarioService).atualizarUsuarioNaBaseDeDados(this.recebedor);
        when(this.repository.save(this.transacao)).thenReturn(this.transacao);
        when(this.notificacaoService.enviarNotificacao(this.pagador.getEmail(), TipoDeTransacao.ENVIADA.enviar(this.recebedor.getCpf()))).thenReturn(true);
        when(this.notificacaoService.enviarNotificacao(this.recebedor.getEmail(), TipoDeTransacao.RECEBIDA.enviar(this.pagador.getCpf()))).thenReturn(false);

        TransacaoResponse resultado = this.service.transferenciaFinanceira(this.request);

        verify(this.autorizadorService, times(1)).autorizarTransacao(this.transacaoCaptor.capture());
        verify(this.repository, times(1)).save(any(Transacao.class));
        verify(this.usuarioService, times(2)).atualizarUsuarioNaBaseDeDados(any(Usuario.class));
        verify(this.usuarioService, times(2)).encontrarUsuarioPorId(anyLong());

        assertEquals(this.pagador,this.transacaoCaptor.getValue().getPagador());
        assertEquals(this.recebedor,this.transacaoCaptor.getValue().getRecebedor());
        assertEquals(BigDecimal.TEN, resultado.getValorTransacao());
        assertEquals("Autorizado", resultado.getStatusDaTransacao());
        assertFalse(resultado.getNotificacoesEnviadas());
        assertNotNull(resultado.getDataTransacao());
    }

    @Test
    void quandoReceberUmaTransacaoRequestComTodosDadosDeUmLojistaComoPagadorEntaoUmaLojistaPagadorExceptionDeveSerLancada() {
        this.pagador.setTipoDeUsuario(TipoDeUsuario.LOJISTA);

        when(this.usuarioService.encontrarUsuarioPorId(1L)).thenReturn(this.pagador);
        when(this.usuarioService.encontrarUsuarioPorId(2L)).thenReturn(this.recebedor);

        LojistaPagadorException resultado = assertThrows(LojistaPagadorException.class, () -> this.service.transferenciaFinanceira(this.request));

        verify(this.autorizadorService, never()).autorizarTransacao(any(Transacao.class));
        verify(this.repository, never()).save(any(Transacao.class));
        verify(this.usuarioService, never()).atualizarUsuarioNaBaseDeDados(any(Usuario.class));
        verify(this.usuarioService, times(2)).encontrarUsuarioPorId(anyLong());
        verify(this.notificacaoService, never()).enviarNotificacao(anyString(), anyString());

        assertEquals(ManipuladorDeErro.LOJISTA_PAGADOR.mensagemDeErro(this.pagador.getCpf()), resultado.getMessage());
    }

    @Test
    @SneakyThrows
    void quandoEnviarNotificaoReceberEmailAndMensagemEntaoTrueDeverRetornadoEmCasoDeSucesso() {
        when(this.notificacaoService.enviarNotificacao(anyString(), anyString())).thenReturn(true);

        Method method = this.service.getClass().getDeclaredMethod("enviarNotificacaoDeTransferencia", Usuario.class, Usuario.class, TipoDeTransacao.class);
        method.setAccessible(true);

        Boolean retorno = (Boolean) method.invoke(this.service, this.pagador, this.recebedor, TipoDeTransacao.ENVIADA);

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

        assertInstanceOf(LojistaPagadorException.class, realException);
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

        assertInstanceOf(SaldoInsuficienteException.class, realException);
        assertEquals(ManipuladorDeErro.SALDO_INSUFICIENTE.mensagemDeErro(this.pagador.getCpf()), realException.getMessage());
    }

    @Test
    @SneakyThrows
    void quandoAutorizarTransacaoReceberParametrosValidosEntaoUmaTransacaoComDataTransacaoDiferenteDeNuloDeveSerRetornada() {
        this.transacao.setDataTransacao(LocalDateTime.now());

        when(this.autorizadorService.autorizarTransacao(any(Transacao.class))).thenReturn(this.transacao);

        Method method = this.service.getClass().getDeclaredMethod("autorizarTransacao", Usuario.class, Usuario.class, BigDecimal.class);
        method.setAccessible(true);
        Transacao retorno = (Transacao) method.invoke(this.service, this.pagador, this.recebedor, BigDecimal.TEN);

        verify(this.autorizadorService, times(1)).autorizarTransacao(this.transacaoCaptor.capture());

        assertEquals(retorno.getRecebedor(), this.transacaoCaptor.getValue().getRecebedor());
        assertEquals(retorno.getPagador(), this.transacaoCaptor.getValue().getPagador());
        assertNull(this.transacaoCaptor.getValue().getDataTransacao());
        assertNotNull(retorno.getDataTransacao());
    }

    @Test
    @SneakyThrows
    void quandoAutorizarTransacaoReceberParametrosValidosMasATransacaoNaoForAutorizadaEntaoUmaAutorizacaoTransacaoExceptionDeveSerLancada() {
        String valor = BigDecimal.TEN.toString();
        when(this.autorizadorService.autorizarTransacao(any(Transacao.class))).thenThrow(new AutorizacaoTransacaoException(valor));

        Method method = this.service.getClass().getDeclaredMethod("autorizarTransacao", Usuario.class, Usuario.class, BigDecimal.class);
        method.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> method.invoke(this.service, this.pagador, this.recebedor, BigDecimal.TEN));

        assertInstanceOf(AutorizacaoTransacaoException.class, exception.getTargetException());
        assertEquals(ManipuladorDeErro.TRANSACAO_NAO_AUTORIZADA.mensagemDeErro(valor), exception.getTargetException().getMessage());
    }

    @Test
    @SneakyThrows
    void quandoAutorizarTransacaoReceberParametrosValidosMasServicoExternoDownEntaoUmaAutorizacaoTransacaoExceptionDeveSerLancada() {
        String url = "www.autorizar-transacao.com.br";
        when(this.autorizadorService.autorizarTransacao(any(Transacao.class))).thenThrow(new ServicoAutorizadorException(url));

        Method method = this.service.getClass().getDeclaredMethod("autorizarTransacao", Usuario.class, Usuario.class, BigDecimal.class);
        method.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> method.invoke(this.service, this.pagador, this.recebedor, BigDecimal.TEN));

        assertInstanceOf(ServicoAutorizadorException.class, exception.getTargetException());
        assertEquals(ManipuladorDeErro.ERRO_API_AUTORIZADOR.mensagemDeErro(url), exception.getTargetException().getMessage());
    }

    @Test
    @SneakyThrows
    void quandoRealizarTransferenciaFinanceiraReceberParametrosValidosETransacaoForAutorizadaEntaoUmaTransacaoDeverSerRealizadaERetornada() {
        BigDecimal saldoRecebedor = BigDecimal.valueOf(210.0);
        BigDecimal saldoPagador = BigDecimal.valueOf(190.0);

        when(this.autorizadorService.autorizarTransacao(any(Transacao.class))).thenReturn(this.transacao);
        doNothing().when(this.usuarioService).atualizarUsuarioNaBaseDeDados(this.pagador);
        doNothing().when(this.usuarioService).atualizarUsuarioNaBaseDeDados(this.recebedor);
        when(this.repository.save(this.transacao)).thenReturn(this.transacao);

        Method method = this.service.getClass().getDeclaredMethod("realizarTranferenciaFinanceira", Usuario.class, Usuario.class, BigDecimal.class);
        method.setAccessible(true);

        Transacao resultado = (Transacao) method.invoke(this.service, this.pagador, this.recebedor, BigDecimal.TEN);

        assertEquals(saldoRecebedor, resultado.getRecebedor().getSaldo());
        assertEquals(saldoPagador, resultado.getPagador().getSaldo());
    }

    @Test
    void quandoObterTransacoesDebitadasReceberUmCpfValidoEntaoUmaListaDeTransacaoPagadorResponseDeveSerRetornada(){
        String cpf = "32212200650";
        this.transacao.setHashTransacao(AutorizadorServiceTest.SHA_256_TEST);
        LocalDateTime dataTransacao = LocalDateTime.parse(AutorizadorServiceTest.DATA_TRANSACAO_TESTE);
        this.transacao.setDataTransacao(dataTransacao);
        when(this.repository.findTransacaoByPagador_Cpf(cpf)).thenReturn(List.of(this.transacao));

        List<TransacaoPagadorResponse> responses = this.service.obterTransacoesDebitadas(cpf);
        TransacaoPagadorResponse response = responses.stream().findFirst().orElse(new TransacaoPagadorResponse());

        verify(this.repository, times(1)).findTransacaoByPagador_Cpf(cpf);

        assertFalse(responses.isEmpty());
        assertEquals(AutorizadorServiceTest.SHA_256_TEST, response.getHashTransacao());
        assertEquals(this.recebedor.getCpf(), response.getDebitadoPara().getCpf());
        assertEquals(this.recebedor.getNomeCompleto(), response.getDebitadoPara().getNomeCompleto());
    }

    @Test
    void quandoObterTransacoesDebitadasReceberUmCpfNaoCadastradoOuQueNaoTenhaTransacoesDebitadasEntaoUmaListaVaziaDeveSerRetornada(){
        when(this.repository.findTransacaoByPagador_Cpf(anyString())).thenReturn(List.of());

        List<TransacaoPagadorResponse> responses = this.service.obterTransacoesDebitadas(anyString());
        TransacaoPagadorResponse response = responses.stream().findFirst().orElse(new TransacaoPagadorResponse());

        verify(this.repository, times(1)).findTransacaoByPagador_Cpf(anyString());

        assertTrue(responses.isEmpty());
        assertNull(response.getHashTransacao());
        assertNull(response.getDebitadoPara());
    }

    @Test
    void quandoObterTransacoesCreditadasReceberUmCpfValidoEntaoUmaListaDeTransacaoRecebedorResponseDeveSerRetornada(){
        String cpf = "44914562839";
        this.transacao.setHashTransacao(AutorizadorServiceTest.SHA_256_TEST);
        LocalDateTime dataTransacao = LocalDateTime.parse(AutorizadorServiceTest.DATA_TRANSACAO_TESTE);
        this.transacao.setDataTransacao(dataTransacao);
        when(this.repository.findTransacaoByRecebedor_Cpf(cpf)).thenReturn(List.of(this.transacao));

        List<TransacaoRecebedorResponse> responses = this.service.obterTransacoesCreditadas(cpf);
        TransacaoRecebedorResponse response = responses.stream().findFirst().orElse(new TransacaoRecebedorResponse());

        verify(this.repository, times(1)).findTransacaoByRecebedor_Cpf(cpf);

        assertFalse(responses.isEmpty());
        assertEquals(AutorizadorServiceTest.SHA_256_TEST, response.getHashTransacao());
        assertEquals(this.pagador.getCpf(), response.getCreditadoDe().getCpf());
        assertEquals(this.pagador.getNomeCompleto(), response.getCreditadoDe().getNomeCompleto());
    }

    @Test
    void quandoObterTransacoesCreditadasReceberUmCpfNaoCadastradoOuQueNaoTenhaTransacoesCreditadaEntaoUmaListaVaziaDeveSerRetornada(){
        when(this.repository.findTransacaoByRecebedor_Cpf(anyString())).thenReturn(List.of());

        List<TransacaoRecebedorResponse> responses = this.service.obterTransacoesCreditadas(anyString());
        TransacaoRecebedorResponse response = responses.stream().findFirst().orElse(new TransacaoRecebedorResponse());

        verify(this.repository, times(1)).findTransacaoByRecebedor_Cpf(anyString());

        assertTrue(responses.isEmpty());
        assertNull(response.getHashTransacao());
        assertNull(response.getCreditadoDe());
    }

    @Test
    @SneakyThrows
    void quandoConstruirRespostaTransacaoReceberInformacoesValidasEntaoUmaTransacaoResponseDeveSerRetornada() {
        LocalDateTime dataEsperada = LocalDateTime.parse(AutorizadorServiceTest.DATA_TRANSACAO_TESTE);
        this.transacao.setDataTransacao(dataEsperada);
        this.transacao.setHashTransacao(AutorizadorServiceTest.SHA_256_TEST);
        when(this.autorizadorService.autorizarTransacao(any(Transacao.class))).thenReturn(this.transacao);
        when(this.repository.save(any(Transacao.class))).thenReturn(this.transacao);

        Method method = this.service.getClass().getDeclaredMethod("construirRespostaTransacao", Usuario.class, Usuario.class, BigDecimal.class, Boolean.class);
        method.setAccessible(true);

        TransacaoResponse actual = (TransacaoResponse) method.invoke(this.service, this.pagador, this.recebedor, BigDecimal.TEN, true);

        verify(this.usuarioService, times(2)).atualizarUsuarioNaBaseDeDados(any(Usuario.class));
        verify(this.repository, times(1)).save(any(Transacao.class));

        assertEquals(dataEsperada, actual.getDataTransacao());
        assertEquals(AutorizadorServiceTest.AUTORIZADO, actual.getStatusDaTransacao());
        assertEquals(BigDecimal.TEN, actual.getValorTransacao());
        assertTrue(actual.getNotificacoesEnviadas());
    }

}
