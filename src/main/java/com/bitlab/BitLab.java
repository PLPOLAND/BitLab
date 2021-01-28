package com.bitlab;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import com.bitlab.connect.ConnectionManager;
import com.bitlab.connect.data.Node;
import com.bitlab.constant.Constants;
import com.bitlab.message.data.NetAddr;

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
                        logger.info("Wypisuję aktualnie znane ip peerów");
                        File file = new File("scan_result.txt");
                        FileWriter writer = new FileWriter(file);
                        for (String ip : ConnectionManager.peers.getPeery()) {
                            logger.info(ip);
                            writer.write(ip+"\n");
                        }
                        writer.close();
                        logger.info("Wypisano peerów: " + ConnectionManager.peers.getPeery().size());
                        logger.info("Zapisanych peerów do pliku: " + ConnectionManager.peers.getPeery().size());
                        break;
                    case SCAN:
                        Watek w = new Watek(Watek.WhatToRun.SCAN,cManager);
                        new Thread(w).start();
                        watki.add(w);
                        break;
                    case STOP:
                        for (Watek watek : watki) {
                            watek.shouldRun = false;
                        }
                        break;
                    case PING:
                    
                        cManager.getPeersByDNS();
                        ConnectionManager.queue.take();//zdjęcie pierwszego z góry
                        cManager.ping(ConnectionManager.queue.take());
                        // cManager.ping(new NetAddr("0000:0000:0000:0000:0000:ffff:2e65:1f5b", 8333));
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
