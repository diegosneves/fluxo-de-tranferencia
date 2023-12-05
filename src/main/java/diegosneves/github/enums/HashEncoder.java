package diegosneves.github.enums;

import diegosneves.github.exception.HashEncoderException;
import diegosneves.github.model.Usuario;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

public enum HashEncoder {

    MD5("MD5"),
    SHA_256("SHA-256");

    private final String algoritimo;

    HashEncoder(String algoritimo) {
        this.algoritimo = algoritimo;
    }

    public String encode(String valor) {
        try {
            MessageDigest encodeAlgorithm = MessageDigest.getInstance(this.algoritimo);
            byte[] messageDigest = encodeAlgorithm.digest(valor.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", 0xFF & b));
            }
            return hexString.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new HashEncoderException(valor, e);
        }
    }

}

class Teste {
    public static void main(String[] args) {
//        Usuario um = Usuario.builder()
//                .id(1L)
//                .cpf("32212200650")
//                .email("pagador@teste.com.br")
//                .senha("Teste@123")
//                .tipoDeUsuario(TipoDeUsuario.COMUM)
//                .saldo(BigDecimal.valueOf(200.0))
//                .build();
//
//        Usuario dois = Usuario.builder()
//                .id(2L)
//                .cpf("93547048168")
//                .email("pagador@teste.com.br")
//                .senha("Teste@123")
//                .tipoDeUsuario(TipoDeUsuario.COMUM)
//                .saldo(BigDecimal.valueOf(200.0))
//                .build();

        String dataTransacao = "2023-12-05T15:02:52.098552651";
        String cpfPagador = "32212200650";
        String cpfRecebedor = "93547048168";

        LocalDateTime dateTime = LocalDateTime.now();

        System.out.println(dateTime);

        String criptografar = cpfPagador + cpfRecebedor + dataTransacao;

        System.out.println("====== MD5 ======");
        System.out.println("9091c58ab7fdb50ee769406c55cddfe2");
        System.out.println(HashEncoder.MD5.encode(criptografar));

        System.out.println("====== SHA 256 ======");
        System.out.println("87c28bdb7b0a48d4cc6b882b47f9fb1c1179691f7797fc30ba48cbf20c5b4fc3");
        System.out.println(HashEncoder.SHA_256.encode(criptografar));

    }
}
