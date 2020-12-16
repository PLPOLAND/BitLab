package com.bitlab.constant;

import com.bitlab.util.ByteUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public enum CommandMap {

    VERSION("version"),
    VERACK("verack"),
    PING("ping"),
    PONG("pong"),
    GETADDR("getaddr"),
    ADDR("addr"),
    INV("inv"),
    ALERT("alert"),
    GETUTXOS("getutxos"),
    UTXOS("utxos"),
    TX("tx"),
    GETDATA("getdata"),
    REJECT("reject"),
    BLOCK("block");

    private final String command;
    public final int length;

    CommandMap (String command) {
        this.command = command;
        this.length = ByteUtils.stringToBytes(command).length;
    }

    @Override
    public String toString () {
        return command;
    }

    public byte[] toByteArray() {
        byte[] bytes = new byte[12];
        System.arraycopy(ByteUtils.stringToBytes(command), 0, bytes, 0, length);
        return bytes;
    }

    public static String info(CommandMap name) {

        String response;

        switch (name){
            case VERSION:
                response = "version\n" +
                        "When a node creates an outgoing connection, it will immediately advertise its version. The remote node will respond with its version. No further communication is possible until both peers have exchanged their version.";
                break;
            case VERACK:
                response = "verack\n" +
                        "The verack message is sent in reply to version. This message consists of only a message header with the command string \"verack\".";
                break;
            case PING:
                response = "ping\n" +
                        "The ping message is sent primarily to confirm that the TCP/IP connection is still valid. An error in transmission is presumed to be a closed connection and the address is removed as a current peer.\n" +
                        "\n";
                break;
            case PONG:
                response = "pong\n" +
                        "The pong message is sent in response to a ping message. In modern protocol versions, a pong response is generated using a nonce included in the ping.";
                break;
            case GETADDR:
                response = "getaddr\n" +
                        "The getaddr message sends a request to a node asking for information about known active peers to help with finding potential nodes in the network. The response to receiving this message is to transmit one or more addr messages with one or more peers from a database of known active peers. The typical presumption is that a node is likely to be active if it has been sending a message within the last three hours.\n" +
                        "\n" +
                        "No additional data is transmitted with this message.";
                break;
            case ADDR:
                response = "addr\n" +
                        "Provide information on known nodes of the network. Non-advertised nodes should be forgotten after typically 3 hours";
                break;
            case INV:
                response = "inv\n" +
                        "Allows a node to advertise its knowledge of one or more objects. It can be received unsolicited, or in reply to getblocks.";
                break;
            case ALERT:
                response = "alert\n" +
                        "Note: Support for alert messages has been removed from bitcoin core in March 2016. Read more here\n" +
                        "\n" +
                        "\n" +
                        "An alert is sent between nodes to send a general notification message throughout the network. If the alert can be confirmed with the signature as having come from the core development group of the Bitcoin software, the message is suggested to be displayed for end-users. Attempts to perform transactions, particularly automated transactions through the client, are suggested to be halted. The text in the Message string should be relayed to log files and any user interfaces.";
                break;
            case GETUTXOS:
                response = "non";
                break;
            case UTXOS:
                response = "null";
                break;
            case TX:
                response = "tx\n" +
                        "tx describes a bitcoin transaction, in reply to getdata. When a bloom filter is applied tx objects are sent automatically for matching transactions following the merkleblock.";
                break;
            case GETDATA:
                response = "getdata\n" +
                        "getdata is used in response to inv, to retrieve the content of a specific object, and is usually sent after receiving an inv packet, after filtering known elements. It can be used to retrieve transactions, but only if they are in the memory pool or relay set - arbitrary access to transactions in the chain is not allowed to avoid having clients start to depend on nodes having full transaction indexes (which modern nodes do not).";
                break;
            case REJECT:
                response = "reject\n" +
                        "The reject message is sent when messages are rejected.";
                break;
            case BLOCK:
                response = "block\n" +
                        "The block message is sent in response to a getdata message which requests transaction information from a block hash.";
                break;
            default:
                response = "ERROR";
        }

        return response;
    }

//    public static String info(String name) {
//        var c = Arrays.stream(CommandMap.values())
//                .filter(v -> v.name().equals(name))
//                .findFirst();
//        return c.map(CommandMap::info).orElse(null);
//    }
}
