package diegosneves.github.exception;

public enum ManipuladorDeErro {

    USUARIO_NAO_ENCONTRADO("O usuário de CPF [ %s ] não existe.");

    private final String mensagem;

    ManipuladorDeErro(String mensagem) {
        this.mensagem = mensagem;
    }

    public String mensagemDeErro(String valor) {
        return String.format(this.mensagem, valor);
    }

}
