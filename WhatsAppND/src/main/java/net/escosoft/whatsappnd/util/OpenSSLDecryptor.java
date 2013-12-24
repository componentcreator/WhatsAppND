package net.escosoft.whatsappnd.util;

import android.content.Context;

import org.apache.commons.io.FileUtils;
import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class created for StackOverflow by owlstead.
 * This is open source, you are free to copy and use for any purpose.
 */
public class OpenSSLDecryptor {

    private static final Charset ASCII = Charset.forName("ASCII");
    private static final int INDEX_KEY = 0;
    private static final int INDEX_IV = 1;
    private static final int ITERATIONS = 1;

    private static final int ARG_INDEX_FILENAME = 0;
    private static final int ARG_INDEX_PASSWORD = 1;

    private static final int SALT_OFFSET = 8;
    private static final int SALT_SIZE = 8;
    private static final int CIPHERTEXT_OFFSET = SALT_OFFSET + SALT_SIZE;

    private static final int KEY_SIZE_BITS = 256;

    /**
     * Thanks go to Ola Bini for releasing this source on his blog.
     * The source was obtained from <a href="http://olabini.com/blog/tag/evp_bytestokey/">here</a> .
     */
    public static byte[][] EVP_BytesToKey(int key_len, int iv_len, MessageDigest md,
                                          byte[] salt, byte[] data, int count) {
        byte[][] both = new byte[2][];
        byte[] key = new byte[key_len];
        int key_ix = 0;
        byte[] iv = new byte[iv_len];
        int iv_ix = 0;
        both[0] = key;
        both[1] = iv;
        byte[] md_buf = null;
        int nkey = key_len;
        int niv = iv_len;
        int i = 0;
        if (data == null) {
            return both;
        }
        int addmd = 0;
        for (; ; ) {
            md.reset();
            if (addmd++ > 0) {
                md.update(md_buf);
            }
            md.update(data);
            if (null != salt) {
                md.update(salt, 0, 8);
            }
            md_buf = md.digest();
            for (i = 1; i < count; i++) {
                md.reset();
                md.update(md_buf);
                md_buf = md.digest();
            }
            i = 0;
            if (nkey > 0) {
                for (; ; ) {
                    if (nkey == 0)
                        break;
                    if (i == md_buf.length)
                        break;
                    key[key_ix++] = md_buf[i];
                    nkey--;
                    i++;
                }
            }
            if (niv > 0 && i != md_buf.length) {
                for (; ; ) {
                    if (niv == 0)
                        break;
                    if (i == md_buf.length)
                        break;
                    iv[iv_ix++] = md_buf[i];
                    niv--;
                    i++;
                }
            }
            if (nkey == 0 && niv == 0) {
                break;
            }
        }
        for (i = 0; i < md_buf.length; i++) {
            md_buf[i] = 0;
        }
        return both;
    }

    public static File decryptFile(Context contexto, File archivo, String password, String decryptedFile) throws BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);

        try {
            // Pasa el fichero a un array de bytes.
            byte[] encrypted = FileUtils.readFileToByteArray(archivo);
            Cipher c = Cipher.getInstance("AES/ECB/NoPadding", "SC");

            // Openssl puts SALTED__ then the 8 byte salt at the start of the file.  We simply copy it out.
            byte[] salt = new byte[8];
            System.arraycopy(encrypted, 8, salt, 0, 8);
            SecretKeyFactory fact = SecretKeyFactory.getInstance("PBEWITHMD5AND128BITAES-CBC-OPENSSL", "SC");
            SecretKey sk1 = new SecretKeySpec(fact.generateSecret(new PBEKeySpec(password.toCharArray(), salt, 100)).getEncoded(), "AES");
            c.init(Cipher.DECRYPT_MODE, sk1);

            // Desencriptar el fichero.
            byte[] data = c.doFinal(encrypted, 16, encrypted.length - 16);

            //convert array of bytes into file
            FileOutputStream fileOuputStream = new FileOutputStream(new File(decryptedFile));
            fileOuputStream.write(data);
            fileOuputStream.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
