package zhusu.backend.util

import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

class CipherUtil {

    static String desEncrypt(String source, String key) {
        def cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeyFactory.getInstance('DES')
                .generateSecret(new DESKeySpec(key.getBytes('UTF-8'))))
        cipher.doFinal(source.bytes).encodeBase64().toString()
    }

    static String desDecrypt(String secret, String key) {
        def cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, SecretKeyFactory.getInstance('DES')
                .generateSecret(new DESKeySpec(key.getBytes('UTF-8'))))
        new String(cipher.doFinal(secret.decodeBase64()))
    }

}
