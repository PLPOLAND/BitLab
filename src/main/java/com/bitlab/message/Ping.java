package com.bitlab.message;

import com.bitlab.constant.CommandMap;
import com.bitlab.util.ByteParser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

public class Ping extends Message implements Sendable, Receivable{
    private long nonce;
    static Random random = new Random();

    public Ping(ByteBuffer buffer) {
        super(CommandMap.PING);
        deserialize(buffer);
    }

    public Ping(long nonce) {
        super(CommandMap.PING);
        this.nonce = nonce;
    }

    public Ping(Header header) {
        super(CommandMap.PING);
        deserialize(header);
    }
    public Ping(){
        super(CommandMap.PING);
        do{
            this.nonce = random.nextLong();
        }while(this.nonce<0);

    }

    @Override
    public ByteBuffer serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN).putLong(nonce);
        return insertHeader(buffer);
    }

    @Override
    public void deserialize(ByteBuffer buffer) {
        header = new Header(buffer);
        if (header.getPayloadSize() == 0) {
            nonce = 0;
            return;
        }

        ByteParser parser = new ByteParser(header.getContent());
        nonce = parser.parseLong(true);
    }

    public void deserialize(Header header) {
        this.header = header;
        if (header.getPayloadSize() == 0) {
            nonce = 0;
            return;
        }

        ByteParser parser = new ByteParser(header.getContent());
        nonce = parser.parseLong(true);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Ping{");
        sb.append("nonce=").append(String.format("0x%x", nonce));
        sb.append('}');
        return sb.toString();
    }

    public long getNonce() {
        return nonce;
    }

    @Override
    public CommandMap getMessageName() {
        return command;
    }
}
