package de.enflexit.common.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * The Class EnDecrypt can be used to encrypt or decrypt a string value in order to not save 
 * String values (such as passwords and other) in clear text within a file.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class EnDecrypt {

	private static final String cipherTransformation = "AES/CBC/PKCS5Padding";
	
	private static final String pswd = "AWB-SeCrEtE";
	private static final String salt = "EnFlex.IT-AgEnT";
	
	
	/**
	 * Encrypts the specified string.
	 *
	 * @param stringValue the string value
	 * @return the encrypted string
	 */
	public static String encrypt(String stringValue) {
		
	    String encryptedString = null;
		try {
			IvParameterSpec ivParameterSpec = EnDecrypt.generateIv();
			SecretKey key = EnDecrypt.getKeyFromPassword(pswd, salt);
			encryptedString = EnDecrypt.encryptPasswordBased(stringValue, key, ivParameterSpec);

		} catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | NoSuchPaddingException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException ex) {
			ex.printStackTrace();
		}
		return encryptedString;
	}
	private static String encryptPasswordBased(String plainText, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance(cipherTransformation);
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
	}

	
	/**
	 * Decrypts the specified string.
	 *
	 * @param encryptedString the encrypted string
	 * @return the decrypted string
	 */
	public static String decrypt(String encryptedString) {
		
		String decryptedString = null;
		try {
			IvParameterSpec ivParameterSpec = EnDecrypt.generateIv();
			SecretKey key = EnDecrypt.getKeyFromPassword(pswd, salt);
			decryptedString = EnDecrypt.decryptPasswordBased(encryptedString, key, ivParameterSpec);

		} catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | NoSuchPaddingException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException ex) {
			ex.printStackTrace();
		}
	    return decryptedString;
	}
	private static String decryptPasswordBased(String cipherText, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance(cipherTransformation);
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
	}
	
	
	
	private static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
	private static SecretKey getKeyFromPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
		SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
		return secret;
	}
	
}
