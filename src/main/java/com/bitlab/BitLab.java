package com.bitlab;

import com.bitlab.connect.ConnectionManager;

import com.bitlab.ui.UIConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitLab {
    private static final Logger logger = LoggerFactory.getLogger(BitLab.class);

    public static void main(String... args) {

        String command = UIConsole.read();
        logger.info("Command is : " + command);

        ConnectionManager cManager = new ConnectionManager();
        try {
            cManager.getPeersByDNS();
            cManager.getAddr(ConnectionManager.queue.take());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
