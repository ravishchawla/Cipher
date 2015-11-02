/**Ravish Chawla**/
package cipher.root.com.cipher.Adapters;

import android.util.Log;
import java.math.BigInteger;
import java.security.*;
import java.util.Arrays;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Static Encryption class that encrypts and decrypts messages. This class
 * also retreives the symmetric key used for encryption.
 */
public class Encryption {

    /**p is a prime number and g is its primitive root modulo. **/
    private static BigInteger p = new BigInteger("71");
    private static BigInteger g = new BigInteger("69");

    /**Seed value for a Source of Randomness for the CBC Encryption algorithm**/
    public static String IV = "AAAAAAAAAAAAAAAA";

    /**Encryption key is based on the D-H encryption method.*/
    public static byte[] getEncryptionKey() {

        /**Secret color is the sending user's Secret key. It is unique and only availaible to the user itself*/
        BigInteger a = Session.getSendingUser().getSecretColor();

        /**Public paint is the receiving user's Combined key. It is created as (g^B)%p, it is availaible to other users*/
        BigInteger B = Session.getReceivingUser().getPublicPaint();

        /**By performing (b^a)%p, we generate a shared symmetric key that is the same for both users*/
        BigInteger res = B.modPow(a, Encryption.p);

        /**This value is hashed to create a larger 128-bit key**/
        return hashBigInt(res);
    }

    /**generates and returns the SHA-256 representation of an integer**/
    public static byte[] hashBigInt(BigInteger val) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            String key = val.toString();
            messageDigest.update(key.getBytes("UTF-8"));
            byte[] hashArray = messageDigest.digest();
            return hashArray;
        } catch(Exception e) {
            Log.d("Encryption::hashBigInt", e.getMessage());
        }

        return null;
    }

    /** Some encryption algorithms don't pad the data. Utility function
     * if such an algorithm is used.
     */
    public static byte[] padText(byte[] text, byte[] encryptionKey) {

        if(text.length % encryptionKey.length == 0) {
            return text;
        }

        int padding = encryptionKey.length - (text.length % encryptionKey.length);
        byte[] newText = new byte[text.length + (padding)];
        System.arraycopy(text, 0, newText, 0, text.length);
        return newText;
    }

    /**Encryption is based on a blackbox AES algorithm. It uses the encryption key to
     * encrypt the text using CBC algorithm.
     */
    public static byte[] encrypt(byte[] nText, byte[] encryptionKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));

            /**The algorithm encrypts all but the last block using update, and the last block when
             * doFinal is called. These results are combined and returned.
             */
            byte[] first = cipher.update(nText);
            byte[] last = cipher.doFinal(nText);
            return combine(first, last);

        } catch(Exception e) {
            Log.d("Encryption::encrypt", e.getMessage());
        }

        return null;
    }

    public static byte[] decrypt(byte[] text, byte[] encryptionKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));

            /**Similarly, the text is decrypted in 2 steps. */
            byte[] first = cipher.update(text);
            byte[] last = cipher.doFinal(text);
            return combine(first, last);
        } catch (Exception e) {
            Log.d("Encryption::decrypt", e.getMessage());
        }
        return null;
    }

    /**A utility function to combine the data of two byte arrays*/
    public static byte[] combine(byte[] orig, byte[] more) {
        byte[] total = new byte[orig.length + more.length];
        for(int i = 0; i < orig.length; i++) {
            total[i] = orig[i];
        }

        for(int i = orig.length; i < total.length; i++) {
            total[i] = more[i - orig.length];
        }

        return total;
    }
}
