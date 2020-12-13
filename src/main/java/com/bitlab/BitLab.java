package com.bitlab;

import java.util.logging.Logger;

public class BitLab {
    static Logger logger = Logger.getLogger(BitLab.class.getName());

    public static void main(String... args) {

        Client client = new Client("3.8.52.65", 8333);//próba połączenia do serwera http wp.pl
        try {
            logger.info(client.send("F9BEB4D9"));//test połączenia (wysyłany nagłówek HTTP) // usunąć w dalszej części pracy

        //komentarz
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
