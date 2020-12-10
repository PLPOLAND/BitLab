package com.bitlab;

import java.util.logging.Logger;

public class BitLab {
    static Logger logger = Logger.getLogger(BitLab.class.getName());

    public static void main(String... args) {

        Client client = new Client("wp.pl", 80);//próba połączenia do serwera http wp.pl
        try {
            logger.info(client.send("GET /start/HTTP/1.1"));//test połączenia (wysyłany nagłówek HTTP) // usunąć w dalszej części pracy

        //komentarz
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
