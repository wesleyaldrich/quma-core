package com.quma.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class CryptoService {

    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    /* Constructor to correctly load keys */
    public CryptoService(
            @Value("${key.encryption}") String encKey,
            @Value("${key.decryption}") String decKey
    ) {
        this.publicKey = loadPublicKey(encKey);
        this.privateKey = loadPrivateKey(decKey);
    }

    /* Base64 */

    public String encodeBase64(String text) {
        return Base64.getEncoder()
                .encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    public String decodeBase64ToString(String base64) {
        return new String(
                Base64.getDecoder().decode(base64),
                StandardCharsets.UTF_8
        );
    }

    /* RSA */

    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encrypted = cipher.doFinal(
                    plainText.getBytes(StandardCharsets.UTF_8)
            );

            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            throw new IllegalStateException("RSA encryption failed", e);
        }
    }

    public String decrypt(String base64CipherText) {
        try {
            byte[] encryptedBytes = Base64.getDecoder()
                    .decode(base64CipherText);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] decrypted = cipher.doFinal(encryptedBytes);

            return new String(decrypted, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new IllegalStateException("RSA decryption failed", e);
        }
    }

    /* Key loaders */

    private PublicKey loadPublicKey(String keyValue) {
        try {
            byte[] keyBytes = extractKeyBytes(keyValue);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (Exception e) {
            throw new IllegalStateException("Invalid RSA public key", e);
        }
    }

    private PrivateKey loadPrivateKey(String keyValue) {
        try {
            byte[] keyBytes = extractKeyBytes(keyValue);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("RSA").generatePrivate(spec);
        } catch (Exception e) {
            throw new IllegalStateException("Invalid RSA private key", e);
        }
    }

    private byte[] extractKeyBytes(String keyValue) {
        try {
            byte[] firstDecode = Base64.getDecoder().decode(keyValue);

            String decoded = new String(firstDecode, StandardCharsets.UTF_8);

            if (decoded.contains("BEGIN")) {
                // PEM detected
                String clean = decoded
                        .replaceAll("-----BEGIN (.*)-----", "")
                        .replaceAll("-----END (.*)-----", "")
                        .replaceAll("\\s", "");

                return Base64.getDecoder().decode(clean);
            }

            return firstDecode;

        } catch (Exception e) {
            throw new IllegalStateException("Failed to extract key.", e);
        }
    }
}
