package diegosneves.github.exception;

public class HashEncoderException extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.FALHA_AO_GERAR_HASH;

    public HashEncoderException(String valor) {
        super(ERRO.mensagemDeErro(valor));
    }

    public HashEncoderException(String valor, Throwable throwable) {
        super(ERRO.mensagemDeErro(valor), throwable);
    }

}
