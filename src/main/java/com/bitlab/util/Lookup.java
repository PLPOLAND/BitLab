package com.bitlab.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
/**Klasa do konwersji stringu na adresy*/
public class Lookup {

    private Lookup () {

    }

    public static InetAddress[] lookup (String host) {
        try {
            return InetAddress.getAllByName(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }
}
