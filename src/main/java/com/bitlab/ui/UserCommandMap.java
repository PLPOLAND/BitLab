package com.bitlab.ui;

import com.bitlab.constant.CommandMap;

import java.util.stream.Stream;

/**
 * Enum with all available command for user
 * @author Jacek Giedronowicz
 */
public enum UserCommandMap {
    VERSION("version"),
    VERACK("verack"),
    GETADDR("getaddr"),
    GETDATA("getdata"),
    ADDR("addr"),
    PRINT("print"),
    SCAN("scan"),
    EXIT("exit"), 
    STOP("stop"), 
    PING("ping");

    private String name;
    UserCommandMap(String name) {
        this.name = name;
    }

    public static String getInfo(UserCommandMap command) {
        String response;

        switch (command){
            case VERSION:
                response = "version\n" +
                        "When a node creates an outgoing connection, it will immediately advertise its version. The remote node will respond with its version. No further communication is possible until both peers have exchanged their version.";
                break;
            case VERACK:
                response = "verack\n" +
                        "The verack message is sent in reply to version. This message consists of only a message header with the command string \"verack\".";
                break;
            case GETADDR:
                response = "getaddr\n" +
                        "The getaddr message sends a request to a node asking for information about known active peers to help with finding potential nodes in the network. The response to receiving this message is to transmit one or more addr messages with one or more peers from a database of known active peers. The typical presumption is that a node is likely to be active if it has been sending a message within the last three hours.\n" +
                        "\n" +
                        "No additional data is transmitted with this message.";
                break;
            case GETDATA:
                response = "getdata\n"+
                        "The getdata message sends a request for single block or transaction specified by hash";
                break;
            case ADDR:
                response = "addr\n" +
                        "Provide information on known nodes of the network. Non-advertised nodes should be forgotten after typically 3 hours";
                break;
            case PRINT:
                response = "print\n" +
                        "print list on the screen ";
                break;
            case STOP:
                response = "stop\n"+
                        "ending scanning task";
                break;
            case PING:
                response = "ping\n"+
                        "sending ping to target";
            default:
                response = "ERROR";
        }

        return response;
    }

    public static String getInfo(String command) {

        for (UserCommandMap value : UserCommandMap.values()) {
            if (value.name.toLowerCase().equals( command.toLowerCase() ))
                return UserCommandMap.getInfo(value);
        }
        return "Unknown command";
    }

    @Override
    public String toString() {
        return name.toLowerCase();
    }
}
