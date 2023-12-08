package diegosneves.github.service;

import diegosneves.github.enums.TipoDeUsuario;
import diegosneves.github.exception.*;
import diegosneves.github.mapper.MapearConstrutor;
import diegosneves.github.model.Usuario;
import diegosneves.github.repository.UsuarioRepository;
import diegosneves.github.request.UsuarioRequest;
import diegosneves.github.response.UsuarioResponse;
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
import java.util.ArrayList;
import java.util.List;
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

    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    private Usuario usuarioTest;
    private UsuarioRequest request;
    private UsuarioResponse response;

    private List<Usuario> usuarioList;

    @BeforeEach
    void setUp() {

        this.usuarioTest = Usuario.builder()
                .id(1L)
                .nomeCompleto("Fulando do Teste")
                .cpf("32212200650")
                .email("teste@teste.com.br")
                .senha("Teste@123")
                .tipoDeUsuario(TipoDeUsuario.COMUM)
                .saldo(BigDecimal.valueOf(200.0))
                .build();

        this.usuarioList = List.of(this.usuarioTest);

        this.request = UsuarioRequest.builder()
                .cpf("31818974770")
                .tipoDeUsuario(TipoDeUsuario.COMUM)
                .email("teste@gmail.com")
                .nomeCompleto("Antonio Canova")
                .saldo(BigDecimal.TEN)
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

        Exception resultado = assertThrows(CpfNaoEncontradoException.class, () -> this.service.findUsuarioByCpf(cpfNaoCadastrado));

        assertEquals(ManipuladorDeErro.CPF_USUARIO_NAO_ENCONTRADO.mensagemDeErro(cpfNaoCadastrado), resultado.getMessage());

    }

    @Test
    void quandoChamarObterTodosUsuariosEntaoDeveRetornarUmaListaComValores() {
        this.response = MapearConstrutor.construirNovoDe(UsuarioResponse.class, this.usuarioTest);
        when(this.repository.findAll()).thenReturn(this.usuarioList);

        List<UsuarioResponse> obterTodosUsuarios = this.service.obterTodosUsuarios();

        verify(this.repository, times(1)).findAll();
        assertFalse(obterTodosUsuarios.isEmpty());
        assertEquals(this.response.getCpf(), obterTodosUsuarios.stream().findFirst().get().getCpf());

    }

    @Test
    void quandoChamarObterTodosUsuariosEntaoDeveRetornarUmaListaVazia() {
        when(this.repository.findAll()).thenReturn(new ArrayList<>());

        List<UsuarioResponse> obterTodosUsuarios = this.service.obterTodosUsuarios();

        verify(this.repository, times(1)).findAll();
        assertTrue(obterTodosUsuarios.isEmpty());

    }

    @Test
    void quandoCadastrarUsuarioReceberUmUsuarioRequestValidoEntaoUmNovoUsuarioDeveSerCadastrado() {
        String cpf = "31818974770";
        String nomeCompleto = "Antonio Canova";
        this.usuarioTest.setCpf(cpf);
        this.usuarioTest.setNomeCompleto(nomeCompleto);
        when(this.repository.save(any(Usuario.class))).thenReturn(this.usuarioTest);

        UsuarioResponse resultado = this.service.cadastrarUsuario(request);

        verify(this.repository, times(1)).save(this.usuarioCaptor.capture());
        assertEquals(cpf, usuarioCaptor.getValue().getCpf());
        assertEquals(nomeCompleto, usuarioCaptor.getValue().getNomeCompleto());
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(cpf, resultado.getCpf());
        assertEquals(nomeCompleto, resultado.getNomeCompleto());

    }

    @Test
    void quandoCadastrarUsuarioReceberUmUsuarioComCpfJaCadastradoEntaoUmaCpfJaCadastradoExceptionDeveSerLancada() {
        String cpf = "32212200650";
        this.request.setCpf(cpf);
        when(this.repository.findUsuarioByCpfOrEmail(eq(cpf), anyString())).thenReturn(Optional.of(this.usuarioTest));

        CpfJaCadastradoException exception = assertThrows(CpfJaCadastradoException.class, () -> this.service.cadastrarUsuario(this.request));

        verify(this.repository, times(1)).findUsuarioByCpfOrEmail(eq(cpf), anyString());
        assertEquals(ManipuladorDeErro.CPF_DUPLICADO.mensagemDeErro(cpf), exception.getMessage());

    }

    @Test
    void quandoCadastrarUsuarioReceberUmUsuarioComEmailJaCadastradoEntaoUmaEmailJaCadastradoExceptionDeveSerLancada() {
        String email = "teste@teste.com.br";
        this.request.setEmail(email);
        when(this.repository.findUsuarioByCpfOrEmail(anyString(), eq(email))).thenReturn(Optional.of(this.usuarioTest));

        EmailJaCadastradoException exception = assertThrows(EmailJaCadastradoException.class, () -> this.service.cadastrarUsuario(this.request));

        verify(this.repository, times(1)).findUsuarioByCpfOrEmail(anyString(), eq(email));
        assertEquals(ManipuladorDeErro.EMAIL_DUPLICADO.mensagemDeErro(email), exception.getMessage());

    }

    @Test
    @SneakyThrows
    void quandoValidarCpfAndEmailReceberUmCpfJaCadastradoEntaoUmaCpfJaCadastradoExceptionDeveSerLancada() {
        String cpf = "32212200650";
        when(this.repository.findUsuarioByCpfOrEmail(eq(cpf), anyString())).thenReturn(Optional.of(this.usuarioTest));
        Method method = this.service.getClass().getDeclaredMethod("validarCpfAndEmail", String.class, String.class);
        method.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> method.invoke(this.service, cpf, "anyString()"));
        Throwable realException = exception.getTargetException();

        verify(this.repository, times(1)).findUsuarioByCpfOrEmail(eq(cpf), anyString());

        assertTrue(realException instanceof CpfJaCadastradoException);
        assertEquals(ManipuladorDeErro.CPF_DUPLICADO.mensagemDeErro(cpf), realException.getMessage());

    }

    @Test
    @SneakyThrows
    void quandoValidarCpfAndEmailReceberUmEmailJaCadastradoEntaoUmEmailJaCadastradoExceptionDeveSerLancada() {
        String email = "teste@teste.com.br";
        when(this.repository.findUsuarioByCpfOrEmail(anyString(), eq(email))).thenReturn(Optional.of(this.usuarioTest));
        Method method = this.service.getClass().getDeclaredMethod("validarCpfAndEmail", String.class, String.class);
        method.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> method.invoke(this.service, "123456", email));
        Throwable realException = exception.getTargetException();

        verify(this.repository, times(1)).findUsuarioByCpfOrEmail(anyString(), eq(email));

        assertTrue(realException instanceof EmailJaCadastradoException);
        assertEquals(ManipuladorDeErro.EMAIL_DUPLICADO.mensagemDeErro(email), realException.getMessage());

    }

    @Test
    void quandoEncontrarUsuarioPorIdReceberUmIdValidoEntaoUmUsuarioDeveSerRetornado() {

        when(this.repository.findUsuarioById(1L)).thenReturn(Optional.of(this.usuarioTest));

        Usuario actual = this.service.encontrarUsuarioPorId(1L);

        verify(this.repository, times(1)).findUsuarioById(1L);

        assertEquals(this.usuarioTest, actual);

    }

    @Test
    void quandoEncontrarUsuarioPorIdReceberUmIdInvalidoEntaoUmaIdInvalidoExceptionDeveSerLancada() {

        when(this.repository.findUsuarioById(1L)).thenReturn(Optional.empty());

        IdInvalidoException exception = assertThrows(IdInvalidoException.class, () -> this.service.encontrarUsuarioPorId(1L));

        verify(this.repository, times(1)).findUsuarioById(1L);

        assertEquals(ManipuladorDeErro.ID_USUARIO_NAO_ENCONTRADO.mensagemDeErro(String.valueOf(1L)), exception.getMessage());

    }

    @Test
    void quandoAtualizarUsuarioNaBaseDeDadosReceberUmUsuarioValidoEntaoEsteUsuarioDeveSerAtualizadoNaBaseDeDados() {

        this.service.atualizarUsuarioNaBaseDeDados(this.usuarioTest);

        verify(this.repository, times(1)).save(any(Usuario.class));

    }

}
