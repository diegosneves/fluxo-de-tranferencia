package diegosneves.github.service;

import diegosneves.github.enums.TipoDeUsuario;
import diegosneves.github.exception.ManipuladorDeErro;
import diegosneves.github.exception.UsuarioNaoEncontradoException;
import diegosneves.github.model.Usuario;
import diegosneves.github.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService service;

    @Mock
    private UsuarioRepository repository;

    private Usuario usuarioTest;

    @BeforeEach
    void setUp() {

        this.usuarioTest = Usuario.builder()
                .id(1L)
                .nomeCompleto("Fulando do Teste")
                .cpf("32212200650")
                .email("teste@teste.com.br")
                .tipoDeUsuario(TipoDeUsuario.COMUM)
                .saldo(BigDecimal.valueOf(200.0))
                .build();

    }

    @Test
    void quandoReceberUmCpfValidoEntaoDeveRetornarUmUsuario() {
        when(this.repository.findUsuarioByCpf("32212200650")).thenReturn(Optional.of(this.usuarioTest));

        Usuario retorno = this.service.findUsuarioByCpf("32212200650");

        verify(this.repository, times(1)).findUsuarioByCpf(anyString());

        assertEquals(this.usuarioTest.getEmail(), retorno.getEmail());

    }

    @Test
    void quandoReceberUmCpfNaoCadastradoEntaoUmUsuarioNaoEncontradoDeveSerLancada() {
        String cpfNaoCadastrado = "01073919102";

        when(this.repository.findUsuarioByCpf(anyString())).thenReturn(Optional.empty());

        Exception resultado = assertThrows(UsuarioNaoEncontradoException.class, () -> this.service.findUsuarioByCpf(cpfNaoCadastrado));

        assertEquals(ManipuladorDeErro.USUARIO_NAO_ENCONTRADO.mensagemDeErro(cpfNaoCadastrado), resultado.getMessage());

    }



}