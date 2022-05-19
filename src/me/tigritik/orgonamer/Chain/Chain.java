package me.tigritik.orgonamer.Chain;

import java.util.Collection;

import me.tigritik.orgonamer.nodes.Node;

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
    
    public int getChainLength(Chain c){
      return c.length; 
    }

    public int[] getNode(){
      return nodes; 
    }

    public String toString(){
      return "";

    }

}