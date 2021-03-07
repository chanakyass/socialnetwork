package com.springboot.rest.config.security.algo;

import com.auth0.jwt.algorithms.Algorithm;
import com.springboot.rest.config.exceptions.ApiAccessException;
import org.apache.commons.codec.binary.Base64;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class AlgorithmStrategy {
    public static Algorithm getAlgorithm(Strategy strategy) {
        Algorithm algorithm = null;
        try {
            switch (strategy) {
                case SYMMETRIC_ENCRYPTION:
                case AUTO:
                    algorithm = Algorithm.HMAC256(Secrets.SECRET_KEY.getSecret());
                    break;
                case ASYMMETRIC_ENCRYPTION:
                    algorithm = Algorithm.RSA256(getPublicKeyFromString(Secrets.PUBLIC_KEY.getSecret()),
                            getPrivateKeyFromString(Secrets.PRIVATE_KEY.getSecret()));
                    break;
            }
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            throw new ApiAccessException("Problem with encryption");
        }
        return algorithm;
    }

    public static RSAPrivateKey getPrivateKeyFromString(String key) throws GeneralSecurityException {

        byte[] encoded = Base64.decodeBase64(key);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return (RSAPrivateKey) kf.generatePrivate(keySpec);
    }

    public static RSAPublicKey getPublicKeyFromString(String key) throws GeneralSecurityException {
        byte[] encoded = Base64.decodeBase64(key);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
    }

}
