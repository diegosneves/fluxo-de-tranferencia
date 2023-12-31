package diegosneves.github.exception;

import org.springframework.http.HttpStatus;

/**
 * Este enum representa um conjunto de mensagens de erro e códigos de status HTTP correspondentes
 * que pode ser usado para lidar com erros em um sistema.
 */
public enum ManipuladorDeErro {

    CPF_USUARIO_NAO_ENCONTRADO("O usuário de CPF [ %s ] não existe.", HttpStatus.NOT_FOUND),
    ID_USUARIO_NAO_ENCONTRADO("O ID [ %s ] de usuário não existe.", HttpStatus.NOT_FOUND),
    LOJISTA_PAGADOR("O usuário de CPF [ %s ] é um LOJISTA e não pode efetuar pagamento.", HttpStatus.BAD_REQUEST),
    SALDO_INSUFICIENTE("O usuário de CPF [ %s ] não possui saldo suficiente.", HttpStatus.BAD_REQUEST),
    TRANSACAO_NAO_AUTORIZADA("Transação no valor de [ %s ] não Autorizada.", HttpStatus.BAD_REQUEST),
    FALHA_NO_MAPEAMENTO("Ocorreu um erro ao tentar mapear a classe [ %s ].", HttpStatus.INTERNAL_SERVER_ERROR),
    FALHA_AO_GERAR_HASH("Ocorreu um erro ao tentar gerar o Hash de [ %s ].", HttpStatus.INTERNAL_SERVER_ERROR),
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

    /**
     * Retorna uma mensagem de erro formatada.
     *
     * @param valor o valor a ser inserido na mensagem de erro
     * @return a mensagem de erro formatada
     */
    public String mensagemDeErro(String valor) {
        return String.format(this.mensagem, valor);
    }

    /**
     * Recupera o código de status associado à instância atual.
     *
     * @return o código de status
     */
    public HttpStatus getStatusCode(){
        return this.statusCode;
    }

}
