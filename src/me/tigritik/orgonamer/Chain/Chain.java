package me.tigritik.orgonamer.chain;

import me.tigritik.orgonamer.Util;
import me.tigritik.orgonamer.nodes.Node;

import java.util.Collection;

public class Chain {

    private final int length;
    private final Node[] nodes;

    public Chain(int length) {
      this.length = length;
      nodes = new Node[length+1];
    }

    public Chain(Collection<Node> nodes) {
        this(nodes.size(), nodes);
    }

    public Chain(int length, Collection<Node> nodes) {
        this(length);
        int i = 1;
        for (Node n: nodes) {
            this.nodes[i] = n;
            i++;
        }
    }
    
    public int getChainLength(){
      return length;
    }

    public Node[] getNodes(){
      return nodes; 
    }

    public boolean branchAt(int index) {
        return (nodes[index].getConnections().size() == 4);
    }

    public String toString(){
      return "prefixes-" + Util.PREFIX[length] + "ane";

    }

}