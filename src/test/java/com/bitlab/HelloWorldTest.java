package com.bitlab;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

public class HelloWorldTest {


    @Test
    public void testImmutableCollections() {

        List<String> fruits = List.of("Mangosteen", "Durian fruit", "Longan");

        assertThrows(UnsupportedOperationException.class, () -> {
            fruits.add("Mango");
            fruits.add("Mango2");
            fruits.remove(1);
        });

        assertEquals(3, fruits.size());

    }
}
