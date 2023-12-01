package diegosneves.github.config;

import diegosneves.github.dto.ExceptionDTO;
import diegosneves.github.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(CpfJaCadastradoException.class)
    public ResponseEntity<ExceptionDTO> cpfDuplicado(CpfJaCadastradoException exception) {
        ExceptionDTO dto = new ExceptionDTO(exception.getMessage(), CpfJaCadastradoException.ERRO.getStatusCode().value());
        return ResponseEntity.status(CpfJaCadastradoException.ERRO.getStatusCode()).body(dto);
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ExceptionDTO> emailDuplicado(EmailJaCadastradoException exception) {
        ExceptionDTO dto = new ExceptionDTO(exception.getMessage(), EmailJaCadastradoException.ERRO.getStatusCode().value());
        return ResponseEntity.status(EmailJaCadastradoException.ERRO.getStatusCode()).body(dto);
    }

    @ExceptionHandler(ConstrutorPadraoNaoDefinido.class)
    public ResponseEntity<ExceptionDTO> contrutorPadraoNaoImplementado(ConstrutorPadraoNaoDefinido exception) {
        ExceptionDTO dto = new ExceptionDTO(exception.getMessage(), ConstrutorPadraoNaoDefinido.ERRO.getStatusCode().value());
        return ResponseEntity.status(ConstrutorPadraoNaoDefinido.ERRO.getStatusCode()).body(dto);
    }

    @ExceptionHandler(MapearObjetoException.class)
    public ResponseEntity<ExceptionDTO> erroAoMapear(MapearObjetoException exception) {
        ExceptionDTO dto = new ExceptionDTO(exception.getMessage(), MapearObjetoException.ERRO.getStatusCode().value());
        return ResponseEntity.status(MapearObjetoException.ERRO.getStatusCode()).body(dto);
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ExceptionDTO> usuarioNaoEncontrado(UsuarioNaoEncontradoException exception) {
        ExceptionDTO dto = new ExceptionDTO(exception.getMessage(), UsuarioNaoEncontradoException.ERRO.getStatusCode().value());
        return ResponseEntity.status(UsuarioNaoEncontradoException.ERRO.getStatusCode()).body(dto);
    }

    @ExceptionHandler(ServicoAutorizadorException.class)
    public ResponseEntity<ExceptionDTO> usuarioNaoEncontrado(ServicoAutorizadorException exception) {
        ExceptionDTO dto = new ExceptionDTO(exception.getMessage(), ServicoAutorizadorException.ERRO.getStatusCode().value());
        return ResponseEntity.status(ServicoAutorizadorException.ERRO.getStatusCode()).body(dto);
    }

    @ExceptionHandler(EnvioNotificacaoException.class)
    public ResponseEntity<ExceptionDTO> usuarioNaoEncontrado(EnvioNotificacaoException exception) {
        ExceptionDTO dto = new ExceptionDTO(exception.getMessage(), EnvioNotificacaoException.ERRO.getStatusCode().value());
        return ResponseEntity.status(EnvioNotificacaoException.ERRO.getStatusCode()).body(dto);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> erroGeneralizado(Exception exception) {
        ExceptionDTO dto = new ExceptionDTO(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }

}
