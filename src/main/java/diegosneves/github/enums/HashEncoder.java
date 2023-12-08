package diegosneves.github.enums;

import diegosneves.github.exception.HashEncoderException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Este enum representa um conjunto de algoritmos de codificação hash que podem ser usados para codificar um valor de string.
 */
public enum HashEncoder {

    MD5("MD5"),
    SHA_256("SHA-256");

    private final String algoritimo;

    HashEncoder(String algoritimo) {
        this.algoritimo = algoritimo;
    }

    /**
     * Codifica um determinado valor usando um algoritmo de codificação hash especificado.
     *
     * @param valor o valor a ser codificado
     * @return o valor codificado como uma string hexadecimal
     * @throws HashEncoderException se ocorrer um erro durante o processo de codificação
     */
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
