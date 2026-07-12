
package ppp.ppp.ppp.alert;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
import android.util.Log;

/**
 * This class contains methods that are security related.  Examples are
 * encrypting and decrypting.
 * 

 */
public final class XxxxxCryptoUtils
{
    private static final String TAG = "XxxxxCryptoUtils";
    private static final int pswdIterations = 10;
    private static final int keySize = 128;
    private static final String cypherInstance = "AES/CBC/PKCS5Padding";
    private static final String secretKeyInstance = "PBKDF2WithHmacSHA1";
    private static final String plainText = "XxxxxText";
    private static final String AESSalt = "XxxxxSalt";
    private static final String initializationVector = "8119745113154120";

    public static String encrypt(String textToEncrypt) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(getRaw(plainText, AESSalt), "AES");
            Cipher cipher = Cipher.getInstance(cypherInstance);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(initializationVector.getBytes()));
            byte[] encrypted = cipher.doFinal(textToEncrypt.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
	    Log.i(TAG, "******* InvalidKeySpecException: " + e.getMessage());
            e.printStackTrace();
	    return "";
	}
    }

    public static String decrypt(String textToDecrypt) {
        try {
            byte[] encryted_bytes = Base64.decode(textToDecrypt, Base64.DEFAULT);
            SecretKeySpec skeySpec = new SecretKeySpec(getRaw(plainText, AESSalt), "AES");
            Cipher cipher = Cipher.getInstance(cypherInstance);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(initializationVector.getBytes()));
            byte[] decrypted = cipher.doFinal(encryted_bytes);
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
	    Log.i(TAG, "******* InvalidKeySpecException: " + e.getMessage());
            e.printStackTrace();
	    return "";
	}
    }


    private static byte[] getRaw(String plainText, String salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(secretKeyInstance);
            KeySpec spec = new PBEKeySpec(plainText.toCharArray(), salt.getBytes(), pswdIterations, keySize);
            return factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
	    Log.i(TAG, "******* InvalidKeySpecException: " + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
	    Log.i(TAG, "******* NoSuchAlgorithmException: " + e.getMessage());
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static boolean isEncrypted(final String cipherText) {
      if (cipherText == null || cipherText.trim().isEmpty()) {
        return false;
      }
      try {
        Base64.decode(cipherText, Base64.DEFAULT);
      } catch (IllegalArgumentException e) {
        return false;
      }
      try {
        String text = decrypt(cipherText);
        return (text != null && !text.trim().isEmpty());
      } catch (Exception ex) {
        return false;
      }
    }

}