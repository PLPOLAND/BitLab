package com.bitlab;

import java.util.ArrayList;
import java.util.Scanner;

import com.bitlab.connect.ConnectionManager;
import com.bitlab.ui.UIConsole;
import com.bitlab.ui.UserCommandMap;
import com.bitlab.util.Watek;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitLab {
    private static final Logger logger = LoggerFactory.getLogger(BitLab.class);

    public static void main(String... args) {

        String command;
        ArrayList<Watek> watki = new ArrayList<>();
        ConnectionManager cManager = new ConnectionManager();
        while (!(command = UIConsole.read()).equals("exit")) {
            logger.info("Command is: " + command);
            try {
                switch (UserCommandMap.valueOf(command.toUpperCase())) {
                    case GETADDR:
                        cManager.getPeersByDNS();
                        cManager.getAddr(ConnectionManager.queue.take());
                        break;
                    case GETDATA:
                        cManager.getPeersByDNS();

                        System.out.println("Type in hash and press enter:");
                        Scanner scanner = new Scanner(System.in);
                        String hash = scanner.nextLine();

                        cManager.getData(ConnectionManager.queue.take(), hash);

                        break;
                    case PRINT:
                        logger.info("Wypisuję aktualnieznane ip peerów");
                        for (String ip : ConnectionManager.peers.getPeery()) {
                            logger.info(ip);
                        }
                        logger.info("Wypisano peerów: " + ConnectionManager.peers.getPeery().size());
                        break;
                    case SCAN:
                        Watek w = new Watek(Watek.WhatToRun.SCAN,cManager);
                        new Thread(w).start();
                        watki.add(w);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.info("Kończę działanie programu");
        if (watki.size()>0) {
            for (Watek watek : watki) {
                watek.shouldRun = false;
            }
        }
        cManager.stop();
    }
}
