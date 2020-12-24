package info.agilite.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherUtils {
	public static byte[] encrypt(String text, String cipherKey, String IV) {
		try {
			Cipher encripta = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
			SecretKeySpec key = new SecretKeySpec(cipherKey.getBytes("UTF-8"), "AES");
			encripta.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
			return encripta.doFinal(text.getBytes("UTF-8"));
		} catch (Exception e) {
			throw new RuntimeException("Encrypt error", e);
		}
	}

	public static byte[] decrypt(byte[] cipher, String cipherKey, String IV) {
		try {
			Cipher decripta = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
			SecretKeySpec key = new SecretKeySpec(cipherKey.getBytes("UTF-8"), "AES");
			decripta.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
			return decripta.doFinal(cipher);
		} catch (Exception e) {
			throw new RuntimeException("Dencrypt error", e);
		}
	}
}
