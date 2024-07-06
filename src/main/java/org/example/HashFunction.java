package org.example;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Wrapper of MD5 hash function.
 */
public class HashFunction {
    private final MessageDigest messageDigest;

    public HashFunction() {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public int hash(String text) {
        messageDigest.reset();
        messageDigest.update(text.getBytes(StandardCharsets.UTF_8));
        var digest = messageDigest.digest();
        // extract the first 4 bytes of the hash (since MD5 produces a 128-bit hash, and 4 bytes = 32 bits).
        // convert the first 4 bytes of the digest to an integer
        // sift first byte to 24 bit, then second byte to 16 bit, then third byte to 8 bit and finally append fourth bit
        // or-ing is appending...
        return (((digest[0] & 0xFF) << 24) | ((digest[1] & 0xFF) << 16) | ((digest[2] & 0xFF) << 8) | (digest[3] & 0xFF)) & 0xFF;
    }
}
