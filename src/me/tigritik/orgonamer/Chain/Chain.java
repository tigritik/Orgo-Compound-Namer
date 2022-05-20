package me.tigritik.orgonamer.chain;

import me.tigritik.orgonamer.Util;

import java.util.Collection;

public class Chain {

    private final int length;
    //private final Node[] nodes;
    private final int[] nodes;

    public Chain(int length) {
      this.length = length;
      nodes = new int[length];
    }

    public Chain(Collection<Integer> nodes) {
        this(nodes.size(), nodes);
    }

    public Chain(int length, Collection<Integer> nodes) {
        this(length);
        int i = 0;
        for (int n: nodes) {
            this.nodes[i] = n;
            i++;
        }
    }
    
    public int getChainLength(){
      return length;
    }

    public int[] getNodes(){
      return nodes; 
    }

    public boolean branchAt(int index) {
        return (nodes[index].getConnections().size() >= 3);
    }

    public String toString(){
      return "prefixes-" + Util.PREFIX[length] + "ane";

    }

}