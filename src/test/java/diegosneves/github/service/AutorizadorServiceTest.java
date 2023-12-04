package diegosneves.github.service;

import diegosneves.github.adapter.ServicoAutorizadorAdapter;
import diegosneves.github.enums.TipoDeUsuario;
import diegosneves.github.exception.AutorizacaoTransacaoException;
import diegosneves.github.exception.ManipuladorDeErro;
import diegosneves.github.exception.ServicoAutorizadorException;
import diegosneves.github.model.Transacao;
import diegosneves.github.model.Usuario;
import diegosneves.github.response.ServicoAutorizadorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AutorizadorServiceTest {

    public static final String AUTORIZADO = "Autorizado";
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
                .cpf("32212200650")
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


}
