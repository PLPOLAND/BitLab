package com.bitlab.message;


import com.bitlab.constant.CommandMap;

import java.nio.ByteBuffer;
/**
 * Odpowiada za Komendę GetAddr
 */
public class GetAddr extends Message implements Sendable {

    public GetAddr() {
        super(CommandMap.GETADDR);
    }

    @Override
    public ByteBuffer serialize() {
        return insertHeader(null);
    }

    @Override
    public CommandMap getMessageName() {
        return command;
    }
}
