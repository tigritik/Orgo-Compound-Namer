package me.tigritik.orgonamer.exceptions;

import me.tigritik.orgonamer.nodes.Node;

public class NodeSaturatedException extends RuntimeException {

    public NodeSaturatedException() {
        this("Node exceeded maximum possible connections.");
    }

    private NodeSaturatedException(String message) {
        super(message);
    }

    public NodeSaturatedException(Node n) {
        super("Exception at " + n.getClass() + ". Exceeded maximum of " + n.getMaxConnections() + " node connections.");
    }
}
