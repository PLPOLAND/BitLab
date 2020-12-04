package com.bitlab;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
/**
 * Służy do hashowania według wymagań BitCoin-a
 * @author Marek Pałdyna
 */
public class Hash {
    public static String hash(String text_to_Hash) {
        
        return Hashing.sha256().hashString(text_to_Hash, StandardCharsets.UTF_8).toString();
    }
}
