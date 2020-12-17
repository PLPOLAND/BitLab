package com.bitlab.message;

import com.bitlab.constant.CommandMap;

import java.nio.ByteBuffer;

public interface Sendable {
    ByteBuffer serialize();
    CommandMap getMessageName();
}
