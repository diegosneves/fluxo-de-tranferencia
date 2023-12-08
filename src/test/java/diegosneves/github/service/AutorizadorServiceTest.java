package diegosneves.github.service;

import diegosneves.github.adapter.ServicoAutorizadorAdapter;
import diegosneves.github.enums.TipoDeUsuario;
import diegosneves.github.exception.AutorizacaoTransacaoException;
import diegosneves.github.exception.ManipuladorDeErro;
import diegosneves.github.exception.ServicoAutorizadorException;
import diegosneves.github.model.Transacao;
import diegosneves.github.model.Usuario;
import diegosneves.github.response.ServicoAutorizadorResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AutorizadorServiceTest {

    public static final String AUTORIZADO = "Autorizado";
    public static final String SHA_256_TEST = "a3b79b5aff0a150c5b44b8a82a05f9a57c3f1d5d9151f32ecc0844d3c29e9719";
    public static final String DATA_TRANSACAO_TESTE = "2023-12-05T15:02:52.098552651";
    public static final String CPF_PAGADOR_TESTE = "32212200650";
    public static final String CPF_RECEBEDOR_TESTE = "93547048168";
    @InjectMocks
    private AutorizadorService service;

    @Mock
    private ServicoAutorizadorAdapter adapter;

    private Transacao transacao;
    private ServicoAutorizadorResponse response;
    private Usuario pagador;
    private Usuario recebedor;

    private LocalDateTime dateTimeTest;

    @BeforeEach
    void setUp() {

        this.dateTimeTest = LocalDateTime.now();

        this.pagador = Usuario.builder()
                .id(1L)
                .nomeCompleto("Fulando do Teste")
                .cpf(CPF_PAGADOR_TESTE)
                .email("teste@teste.com.br")
                .senha("Teste@123")
                .tipoDeUsuario(TipoDeUsuario.COMUM)
                .saldo(BigDecimal.valueOf(2000.0))
                .build();

        this.recebedor = Usuario.builder()
                .id(2L)
                .nomeCompleto("Beltrano do Teste")
                .cpf("86520699640")
                .email("teste2@teste.com.br")
                .senha("Teste@123")
                .tipoDeUsuario(TipoDeUsuario.COMUM)
                .saldo(BigDecimal.valueOf(2000.0))
                .build();

        this.transacao = Transacao.builder()
                .pagador(this.pagador)
                .recebedor(this.recebedor)
                .valorTransacao(BigDecimal.TEN)
                .build();

        this.response = ServicoAutorizadorResponse.builder()
                .message(AUTORIZADO)
                .dataDaAprovacao(this.dateTimeTest)
                .build();

    }

    @Test
    void quandoAutorizarTransacaoReceberUmaTransacaoValidaEntaoUmaTransacaoAutorizadaDeveSerRetornada(){
        when(this.adapter.postAutorizacaoParaTransferencia(this.transacao)).thenReturn(this.response);

        Transacao retorno = this.service.autorizarTransacao(this.transacao);

        verify(this.adapter, times(1)).postAutorizacaoParaTransferencia(any(Transacao.class));

        assertNotNull(retorno);
        assertEquals(this.dateTimeTest, retorno.getDataTransacao());
    }

    @Test
    void quandoAutorizarTransacaoReceberUmaTransacaoQueNaoSeraValidadaEntaoUmaAutorizacaoTransacaoExceptionDeveSerLancada() {
        this.response.setMessage("Não Autorizado");
        when(this.adapter.postAutorizacaoParaTransferencia(this.transacao)).thenReturn(this.response);

        AutorizacaoTransacaoException exception = assertThrows(AutorizacaoTransacaoException.class, () -> this.service.autorizarTransacao(this.transacao));

        assertEquals(ManipuladorDeErro.TRANSACAO_NAO_AUTORIZADA.mensagemDeErro(this.transacao.getValorTransacao().toString()), exception.getMessage());

    }

    @Test
    void quandoAutorizarTransacaoReceberUmaTransacaoComValorAbaixoDeUmEntaoUmaAutorizacaoTransacaoExceptionDeveSerLancada() {
        this.response.setDataDaAprovacao(null);
        when(this.adapter.postAutorizacaoParaTransferencia(this.transacao)).thenReturn(this.response);

        AutorizacaoTransacaoException exception = assertThrows(AutorizacaoTransacaoException.class, () -> this.service.autorizarTransacao(this.transacao));

        assertEquals(ManipuladorDeErro.TRANSACAO_NAO_AUTORIZADA.mensagemDeErro(this.transacao.getValorTransacao().toString()), exception.getMessage());

    }

    @Test
    void quandoAutorizarTransacaoReceberUmaTransacaoValidaMasServicoExternoEstiverForaEntaoUmaServicoAutorizadorExceptionDeveSerLancada()  {
        String url = "www.teste.com.br";

        when(this.adapter.postAutorizacaoParaTransferencia(this.transacao)).thenThrow(new ServicoAutorizadorException(url));

        ServicoAutorizadorException exception = assertThrows(ServicoAutorizadorException.class, () -> this.service.autorizarTransacao(this.transacao));

        assertNotNull(exception);

        assertEquals(ServicoAutorizadorException.ERRO.mensagemDeErro(url), exception.getMessage());

    }

    @Test
    @SneakyThrows
    void quandoGerarHashTransacaoReceberUmaTransacaoAutorizadaEntaoUmHashDeveSerGeradoEAdicionadoNaTransacao(){
        this.response.setDataDaAprovacao(LocalDateTime.parse(DATA_TRANSACAO_TESTE));
        this.recebedor.setCpf(CPF_RECEBEDOR_TESTE);
        this.pagador.setCpf(CPF_PAGADOR_TESTE);

        when(this.adapter.postAutorizacaoParaTransferencia(this.transacao)).thenReturn(this.response);

        Method method = this.service.getClass().getDeclaredMethod("gerarHashTransacao", Transacao.class);
        method.setAccessible(true);

        Transacao resposta = (Transacao) method.invoke(this.service, this.transacao);

        assertEquals(SHA_256_TEST, resposta.getHashTransacao());
    }


}
