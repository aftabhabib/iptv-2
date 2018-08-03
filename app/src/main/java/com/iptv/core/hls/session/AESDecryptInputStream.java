package com.iptv.core.hls.session;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class AESDecryptInputStream extends InputStream {
    private CipherInputStream mCipherInput;

    public AESDecryptInputStream(InputStream input, byte[] key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

            Key cipherKey = new SecretKeySpec(key, "AES");
            AlgorithmParameterSpec cipherIV = new IvParameterSpec(iv);

            cipher.init(Cipher.DECRYPT_MODE, cipherKey, cipherIV);

            mCipherInput = new CipherInputStream(input, cipher);
        }
        catch (GeneralSecurityException e) {
            throw new IllegalStateException("Cipher init fail");
        }
    }

    @Override
    public int read() throws IOException {
        return mCipherInput.read();
    }

    @Override
    public void close() throws IOException {
        mCipherInput.close();
    }
}
