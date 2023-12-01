package diegosneves.github.exception;

import org.springframework.http.HttpStatus;

public enum ManipuladorDeErro {

    USUARIO_NAO_ENCONTRADO("O usuário de CPF [ %s ] não existe.", HttpStatus.NOT_FOUND),
    FALHA_NO_MAPEAMENTO("Ocorreu um erro ao tentar mapear a classe [ %s ].", HttpStatus.INTERNAL_SERVER_ERROR),
    CPF_DUPLICADO("O CPF [ %s ] já foi cadastrado na base de dados", HttpStatus.CONFLICT),
    EMAIL_DUPLICADO("O E-mail [ %s ] já foi cadastrado na base de dados", HttpStatus.CONFLICT),
    ERRO_API_AUTORIZADOR("Não foi possivel acessar a URL [ %s ] para autorizar a transação", HttpStatus.NOT_FOUND),
    ERRO_API_NOTIFICACAO("Não foi possivel acessar a URL [ %s ] para notificar os usuários", HttpStatus.NOT_FOUND),
    CONTRUTOR_PADRAO_NAO_DEFINIDO("Classe [ %s ] deve declarar um construtor padrão.", HttpStatus.NOT_IMPLEMENTED);

    private final String mensagem;
    private final HttpStatus statusCode;

    ManipuladorDeErro(String mensagem, HttpStatus statusCode) {
        this.mensagem = mensagem;
        this.statusCode = statusCode;
    }

    public String mensagemDeErro(String valor) {
        return String.format(this.mensagem, valor);
    }

    public HttpStatus getStatusCode(){
        return this.statusCode;
    }

}
