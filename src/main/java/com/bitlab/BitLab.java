package com.bitlab;

import com.bitlab.connect.ConnectionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitLab {
    private static final Logger logger = LoggerFactory.getLogger(BitLab.class);

    public static void main(String... args) {
        ConnectionManager cMenager = new ConnectionManager();
        try {
            cMenager.run();//TODO: zablokowanie ponownego skanowania tego samego peera!
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
