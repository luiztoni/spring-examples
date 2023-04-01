package br.luiztoni.demo.webflux.user.config.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
public class PasswordUtil implements PasswordEncoder {

    @Value("${password.encoder.secret}")
    private String secret;

    @Value("${password.encoder.iteration}")
    private Integer iteration;

    @Value("${password.encoder.keylength}")
    private Integer keylength;

    @Override
    public String encode(CharSequence sequence) {
        try {
            byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
                    .generateSecret(new PBEKeySpec(sequence.toString().toCharArray(), secret.getBytes(), iteration, keylength))
                    .getEncoded();
            return Base64.getEncoder().encodeToString(result);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public boolean matches(CharSequence sequence, String string) {
        return encode(sequence).equals(string);
    }

}
