package com.quma.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@SpringBootApplication
public class QumaCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(QumaCoreApplication.class, args);
	}

//	public static void main(String[] args) throws Exception {
//		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
//		generator.initialize(4096);
//
//		KeyPair keyPair = generator.generateKeyPair();
//
//		String publicKey = Base64.getEncoder()
//				.encodeToString(keyPair.getPublic().getEncoded());
//
//		String privateKey = Base64.getEncoder()
//				.encodeToString(keyPair.getPrivate().getEncoded());
//
//		System.out.println("Public Key (X.509):");
//		System.out.println(publicKey);
//		System.out.println();
//		System.out.println("Private Key (PKCS#8):");
//		System.out.println(privateKey);
//	}

//	public static void main(String[] args) throws NoSuchAlgorithmException {
//		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//		keyGen.init(256);
//		SecretKey key = keyGen.generateKey();
//
//		String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
//		System.out.println(base64Key);
//	}

}
