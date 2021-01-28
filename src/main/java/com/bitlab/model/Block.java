package com.bitlab.model;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Block {
    private String hash;
    private int height;
    private String chain;
    private long total;
    private int fees;
    private int size;
    private int ver;
    private Date time;
    private Date received_time;
    private String coinbase_addr;
    private String relayed_by;
    private int bits;
    private int nonce;
    private int n_tx;
    private String prev_block;
    private String mrkl_root;
    private List<String> txids;
    private int depth;
    private String prev_block_url;
    private String tx_url;
    private String next_txids;

    public String getHash() {
        return hash;
    }

    public int getHeight() {
        return height;
    }

    public String getChain() {
        return chain;
    }

    public long getTotal() {
        return total;
    }

    public int getFees() {
        return fees;
    }

    public int getSize() {
        return size;
    }

    public int getVer() {
        return ver;
    }

    public Date getTime() {
        return time;
    }

    public Date getReceived_time() {
        return received_time;
    }

    public String getCoinbase_addr() {
        return coinbase_addr;
    }

    public String getRelayed_by() {
        return relayed_by;
    }

    public int getBits() {
        return bits;
    }

    public int getNonce() {
        return nonce;
    }

    public int getN_tx() {
        return n_tx;
    }

    public String getPrev_block() {
        return prev_block;
    }

    public String getMrkl_root() {
        return mrkl_root;
    }

    public List<String> getTxids() {
        return txids;
    }

    public int getDepth() {
        return depth;
    }

    public String getPrev_block_url() {
        return prev_block_url;
    }

    public String getTx_url() {
        return tx_url;
    }

    public String getNext_txids() {
        return next_txids;
    }

    public String baseInfo() {
        return "";
    }

    @Override
    public String toString() {
        return "Block{" + "\n" +
                "   hash='" + hash + '\'' + ",\n" +
                "   height=" + height + ",\n" +
                "   chain='" + chain + '\'' + ",\n" +
                "   total=" + total + ",\n" +
                "   fees=" + fees + ",\n" +
                "   size=" + size + ",\n" +
                "   ver=" + ver + ",\n" +
                "   time=" + time + ",\n" +
                "   received_time=" + received_time + ",\n" +
                "   coinbase_addr='" + coinbase_addr + '\'' + ",\n" +
                "   relayed_by='" + relayed_by + '\'' + ",\n" +
                "   bits=" + bits + ",\n" +
                "   nonce=" + nonce + ",\n" +
                "   n_tx=" + n_tx + ",\n" +
                "   prev_block='" + prev_block + '\'' + "\n" +
                "   mrkl_root='" + mrkl_root + '\'' + "\n" +
                "   txids=[\n" + txids.stream().map(id -> "       \'" + id + "\',\n").collect(Collectors.joining()) + "     ],\n" +
                '}';
    }
}
