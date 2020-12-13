package com.bitlab.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.bitlab.constant.CommandMap;
import com.bitlab.constant.Constants;
import com.bitlab.util.ByteUtils;
import com.bitlab.util.Utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/**
 * Podstawowa klasa zapewniająca obsługę przesyłania komend pomiędzy peerami
 */
public class Message {

    protected final CommandMap command;
    protected Header header;

    protected Message (CommandMap command) {
        this.command = command;
    }

    public Message (Header header) {
        this.command = CommandMap.valueOf(header.getCommandName().toUpperCase());
        this.header = header;
    }
    /**
     * Zwraca zawartoś bytów do wysłania do docelowego peera/Node'a
     * @param content
     * @return
     */
    protected ByteBuffer insertHeader (ByteBuffer content) {
        int size = 0;
        if(content != null)
            size += content.capacity();
        ByteBuffer message = ByteBuffer.allocate(Constants.DEFAULT_SIZE_OF_HEADER + size);
        message.putInt(Constants.START_STRING)
                .put(command.toByteArray())
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(content != null ? content.limit() : 0)
                .order(ByteOrder.BIG_ENDIAN)
                .putInt(content != null ? (int) Long.parseLong(ByteUtils.bytesToHexString(Utils.sha256Twice(content.array()), 0, 4) , 16) : Constants.EMPTY_HASH);
        if(content != null)
            message.put(content.array());
        message.rewind();
        return message;
    }
    /**
     * @return nagłówek "wiadomości"
     */
    @JsonIgnore
    public Header getHeader () {
        return header;
    }
    /**
     * @return komenda wysyłana w "wiadomości"
     */
    @JsonIgnore
    public CommandMap getCommand () {
        return command;
    }
}
