package com.iptv.core.hls.session;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class AESDecryptInputStream extends InputStream {
    private CipherInputStream mCipherInput;

    public AESDecryptInputStream(InputStream input, byte[] key, byte[] initVector) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

            Key cipherKey = new SecretKeySpec(key, "AES");
            AlgorithmParameterSpec cipherIV = new IvParameterSpec(initVector);

            cipher.init(Cipher.DECRYPT_MODE, cipherKey, cipherIV);

            mCipherInput = new CipherInputStream(input, cipher);
        }
        catch (NoSuchAlgorithmException e) {
            //ignore
        }
        catch (NoSuchPaddingException e) {
            //ignore
        }
        catch (InvalidAlgorithmParameterException e) {
            //ignore
        }
        catch (InvalidKeyException e) {
            //ignore
        }
    }

    @Override
    public int read() throws IOException {
        if (mCipherInput == null) {
            throw new IllegalStateException("can not decrypt");
        }

        return mCipherInput.read();
    }

    @Override
    public void close() throws IOException {
        if (mCipherInput != null) {
            mCipherInput.close();
            mCipherInput = null;
        }
    }
}
