package com.bitlab.constant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.bitlab.connect.data.NodeHashMap;
import com.bitlab.message.data.IPv6;
import com.bitlab.message.data.NetAddr;
import com.bitlab.util.ByteUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Constants {

    private Constants () {
    }

    public static final int THREADS = 3;// Ilość wątków do obsługi połączeń

    /* Size of message */
    public static final int DEFAULT_SIZE_OF_HEADER = 24;
    public static final int DEFAULT_SIZE_OF_VERSION = 85;

    /* Never change */
    public static final int START_STRING = 0xf9beb4d9; // Main_net
    public static final int EMPTY_HASH = 0x5df6e0e2; // SHA256(SHA256(""));
    public static final int BUFFER_SIZE = 33554432; // 32MB
    public static final NodeType[] NODE_TYPES = NodeType.values();
    public static final InvType[] INV_TYPES = InvType.values();
    public static final int MSG_WITNESS_FLAG = 1 << 30;
    public static final int MSG_TYPE_MASK = 0xffffffff >> 2;


    /* Note that of those with the service bits flag, most only support a subset of possible options */
    /* https://github.com/bitcoin/bitcoin/blob/master/src/chainparams.cpp */
    public static final String[] SEED_DNS = {
            "seed.bitcoin.sipa.be", // Pieter Wuille, only supports x1, x5, x9, and xd
            "dnsseed.bluematt.me", // Matt Corallo, only supports x9
            "dnsseed.bitcoin.dashjr.org", // Luke Dashjr
            "seed.bitcoinstats.com", // Christian Decker, supports x1 - xf
            "seed.bitcoin.jonasschnelli.ch", // Jonas Schnelli, only supports x1, x5, x9, and xd
            "seed.btc.petertodd.org" // Peter Todd, only supports x1, x5, x9, and xd
    };


    /* Properties */
    public static int PROTOCOL_VERSION;
    public static long NODE_TYPE;
    public static byte[] USER_AGENT;
    public static int USER_AGENT_BYTE;
    public static boolean DEBUG;
    public static int GOAL;

    /* Late init */
    public static long NONCE;

    static {
        Properties properties = null;

        try {
            File data = new File("data");
            if(!data.exists())
                data.mkdir();
            properties = new Properties();
            InputStream is = new FileInputStream("setting.properties");
            properties.load(is);
            is.close();
        } catch (IOException e) {
            System.out.println("Setting.properties is missing or fail to mkdir");
            System.exit(0);
        }

        for(String key : properties.stringPropertyNames()) {
            String property = properties.getProperty(key);
            switch (key) {
                case "USER_AGENT":
                    USER_AGENT = ByteUtils.stringToBytes(property);
                    USER_AGENT_BYTE = USER_AGENT.length;
                    break;
                case "PROTOCOL_VERSION":
                    PROTOCOL_VERSION = Integer.parseInt(property);
                    break;
                case "NODE_TYPE":
                    NODE_TYPE = Long.parseLong(property, 2);
                    break;
                case "DEBUG":
                    DEBUG = Boolean.parseBoolean(property);
                    break;
                case "GOAL":
                    GOAL = Integer.parseInt(property);
                    break;
            }
        }
    }
}
