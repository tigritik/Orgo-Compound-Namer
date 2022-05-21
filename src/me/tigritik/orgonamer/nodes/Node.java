package me.tigritik.orgonamer.nodes;

import java.util.List;

public abstract class Node {

    protected final int maxConnections;

    protected Node(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public abstract List<Node> getConnections();

    public abstract void addConnection(Node n);

    public int getMaxConnections() {
        return maxConnections;
    }
}
