package com.bitlab.message;

import com.bitlab.constant.CommandMap;

import java.nio.ByteBuffer;

public class GetData extends Message implements Sendable, Receivable {

    private int count;
    private int[] inventory;

    public GetData(){
        super(CommandMap.GETDATA);
    }

    @Override
    public ByteBuffer serialize() {
        return null;
    }

    @Override
    public void deserialize(ByteBuffer buffer) {

    }

    @Override
    public CommandMap getMessageName() {
        return command;
    }
}
