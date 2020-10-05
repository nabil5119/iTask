package com.example.todotest.Model;


import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class CryptoHash {
    public String hash(String txt) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance( "SHA-256" );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update( txt.getBytes(Charset.forName("UTF-8")));
        byte[] digest = md.digest();
        return String.format( "%064x", new BigInteger( 1, digest ) );
    }

}
