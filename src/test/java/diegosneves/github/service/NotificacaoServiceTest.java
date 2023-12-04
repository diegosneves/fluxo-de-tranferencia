package diegosneves.github.service;

import diegosneves.github.adapter.EnviarNotificacaoAdapter;
import diegosneves.github.exception.EnvioNotificacaoException;
import diegosneves.github.exception.ManipuladorDeErro;
import diegosneves.github.response.EnviarNotificacaoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class NotificacaoServiceTest {

    @InjectMocks
    private NotificacaoService service;

    @Mock
    private EnviarNotificacaoAdapter adapter;

    private EnviarNotificacaoResponse response;

    @BeforeEach
    void setUp() {
        this.response = EnviarNotificacaoResponse.builder()
                .message(true)
                .build();
    }

    @Test
    void quandoEnviarNotificacaoReceberUmEmailEAutorizacaoEntaoUmUsuariDeveReceberUmEmail() {
        when(this.adapter.postAutorizacaoDeEnvio(anyString(), anyString())).thenReturn(this.response);

        Boolean resultado = this.service.enviarNotificacao(anyString(), anyString());

        assertTrue(resultado);

    }

    @Test
    void quandoEnviarNotificacaoReceberUmEmailEAutorizacaoMasServicoExternoEstiverForaEntaoUmaEnvioNotificacaoExceptionDeveSerLancada() {
        String url = "www.notificar.com.br";
        when(this.adapter.postAutorizacaoDeEnvio(anyString(), anyString())).thenThrow(new EnvioNotificacaoException(url));

        EnvioNotificacaoException exception = assertThrows(EnvioNotificacaoException.class, () -> this.service.enviarNotificacao(anyString(), anyString()));

        assertEquals(ManipuladorDeErro.ERRO_API_NOTIFICACAO.mensagemDeErro(url), exception.getMessage());

    }


}
