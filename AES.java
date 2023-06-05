import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class AES {
    private static SecretKeySpec secretKey;
    private static byte[] key;

    // Montando a chave
    public  void setKey(final String minhaChave) {
        MessageDigest sha = null;
        try {
            key = minhaChave.getBytes("UTF-8"); // pega o vetor de bytes da chave
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES"); // cria um chave utlizando AES
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) { // tratamento de exeção
            e.printStackTrace();
        }
    }

    // Criptografando
    /*
     * Método que recebe dois argumentos: a mensagem a ser cifrada e um senha para cifrar. 
     * É retornado a mensagem cifrada.
     */
    public  String encrypt(final String msgEncript, final String chave) {
        try {
            setKey(chave); // altera o valor da chave para a chave digitada no terminal
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder() // retorna a chave criptografada
                    .encodeToString(cipher.doFinal(msgEncript.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString()); // tratamento de erro
        }
        return null;
    }

    // Descriptografando
    // Método que recebe dois argumentos; a mensagem cifrada e a senha para descifrar
    // É retornado a mensagem descifrada.
    public  String decrypt(final String msgDescript, final String chave) {
        try {
            setKey(chave);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder()
                    .decode(msgDescript)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString()); // mensagem de erro
        }
        return null;
    }


}

