package com.bitlab.util;

import com.bitlab.connect.ConnectionManager;
/**
 * Klasa do prowadzenia kilki operacjii jednocześnie
 * 
 */
public class Watek extends Thread {
    public WhatToRun whatToRun;
    public boolean shouldRun = true;
    private ConnectionManager  cm;
    public Watek(WhatToRun what, ConnectionManager connectionManager) {
        whatToRun = what;
        cm = connectionManager;
    }

    public enum WhatToRun {
        SCAN;
    }

    @Override
    public void run() {
        switch (whatToRun) {
            case SCAN:
                try {
                    cm.getPeersByDNS();
                    cm.runScan(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        
            default:
                break;
        }
    }
    
}
