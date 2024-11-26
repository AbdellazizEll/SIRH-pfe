package com.example.anywrpfe.auth;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;

@Slf4j
public class KeyGenerator {
    public static void main(String[] args) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String base64Key = Encoders.BASE64.encode(key.getEncoded());
        log.info("Base64-encoded key: " + base64Key);
    }

}
