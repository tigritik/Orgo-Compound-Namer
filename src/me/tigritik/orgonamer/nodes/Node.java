package me.tigritik.orgonamer.nodes;

import java.util.List;

public abstract class Node {

    protected final int maxConnections;

    private boolean isPartOfParentChain;

    protected Node(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    protected Node(boolean isPartOfParentChain) {
        this.isPartOfParentChain = isPartOfParentChain;
        maxConnections = 4;
    }

    public abstract List<Node> getConnections();

    public abstract void addConnection(Node n);

    public int getMaxConnections() {
        return maxConnections;
    }

    public boolean getIsPartOfParentChain() {
      return isPartOfParentChain;
    }

    public void setIsPartOfParentChain(boolean bn) {
      isPartOfParentChain = bn; 
    }
     
}
