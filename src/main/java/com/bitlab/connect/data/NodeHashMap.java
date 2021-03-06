package com.bitlab.connect.data;

import com.bitlab.message.data.NetAddr;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Kontener odpowiadający za przechowywanie peerów
 */
public class NodeHashMap {

    private final TreeMap<String, Node> peery;

    public NodeHashMap () {
        peery = new TreeMap<>();
    }
    /**
     * Dodaje pera do listy
     * @param netAddr
     * @param bundle
     * @return wartość logiczną odpowiadająca czy dany adres był już sprawdzany
     */
    synchronized public boolean insert (NetAddr netAddr, StateBundle bundle) {
        String ip = netAddr.getIp().toString();

        if(!peery.containsKey(ip)) {
            peery.put(ip, new Node(netAddr, bundle));//Dodaj nowego Peera
            return false;
        } else { // ten peer już istenieje, więc dodaje informacje o powtórzeniu się 
            Node node = peery.get(ip);
            node.add(netAddr, bundle);//Dodaj info
            node.duplicated();
            return true;
        }
    }

    public int size () {
        return peery.size();
    }

    public void clear () {
        peery.clear();
    }

    public Set<String> getPeery () {
        return peery.keySet();
    }
}
