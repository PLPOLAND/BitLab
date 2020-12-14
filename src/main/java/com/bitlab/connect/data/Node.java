package com.bitlab.connect.data;

import com.bitlab.message.data.NetAddr;

import java.util.LinkedList;

/**
 * Służy do przechowywania danych o peerze
 */
public class Node {

    private final LinkedList<NetAddr> list;
    private final LinkedList<Tag> tag;
    private int duplicated;

    public Node (NetAddr netAddr, StateBundle bundle) {
        duplicated = 0;
        list = new LinkedList<>();
        tag = new LinkedList<>();
        list.add(netAddr);
        tag.add(new Tag(bundle.getIp(), bundle.getConnectionTry()));
    }
    /**
     * Dodanie do istniejącego noda info o nowym adresie Ip który go ogłosił i innych danych
     */
    public void add (NetAddr netAddr, StateBundle bundle) {
        list.add(netAddr);
        tag.add(new Tag(bundle.getIp(), bundle.getConnectionTry()));
    }
    /**
     * zwiększenie licznika oznaczającego ile razy został już ponownie otrzymany
     */
    public void duplicated () {
        duplicated++;
    }

    public LinkedList<NetAddr> getList() {
        return list;
    }

    public LinkedList<Tag> getTag() {
        return tag;
    }

    public int getDuplicated() {
        return duplicated;
    }
}
