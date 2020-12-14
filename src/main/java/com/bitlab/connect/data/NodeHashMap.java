package com.bitlab.connect.data;

import com.bitlab.message.data.NetAddr;

import java.util.concurrent.ConcurrentHashMap;
/**
 * Kontener odpowiadający za przechowywanie peerów
 */
public class NodeHashMap {

    private final ConcurrentHashMap<String, Node> peery;

    public NodeHashMap () {
        peery = new ConcurrentHashMap<>();
    }

    synchronized public void insert (NetAddr netAddr, StateBundle bundle) {
        String ip = netAddr.getIp().toString();

        if(!peery.containsKey(ip)) {
            peery.put(ip, new Node(netAddr, bundle));
        } else { // ten peer już istenieje, więc dodaje informacje o powtórzeniu się 
            Node node = peery.get(ip);
            node.add(netAddr, bundle);
            node.duplicated();
        }
    }

    public int size () {
        return peery.size();
    }

    public void clear () {
        peery.clear();
    }

    public ConcurrentHashMap<String, Node> getPeery () {
        return peery;
    }
}
