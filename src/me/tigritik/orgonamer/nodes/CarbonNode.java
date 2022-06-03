package me.tigritik.orgonamer.nodes;

import me.tigritik.orgonamer.exceptions.NodeSaturatedException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import me.tigritik.orgonamer.Util;

public class CarbonNode extends Node {

    private Node n1;
    private Node n2;
    private Node n3;
    private Node n4;
    private int num;
    private boolean isPartOfParentChain;
    
    public List<Node> getConnections() {
        Set<Node> s = new HashSet<>(4);
        s.add(n1);
        s.add(n2);
        s.add(n3);
        s.add(n4);
        return new ArrayList<>(s);
    }

    public CarbonNode(int n) {
        super(4);
        if (n < 0) {
            throw new IllegalArgumentException("Carbon Node cannot be numbered as " + n);
        }
        num = n;
    }

    public CarbonNode(int n, boolean bn) {
        super(n);
        isPartOfParentChain = bn;
        
    }

    @Override
    public void addConnection(Node n) {
        if (n1 == null) {
            n1 = n;
            return;
        }
        if (n2 == null) {
            n2 = n;
            return;

        }
        if (n3 == null) {
            n3 = n;
            return;
        }
        if (n4 == null) {
            n4 = n;
            return;
        }
        throw new NodeSaturatedException(n);
    }

    public String toString() {
        return "Carbon " + num;
    }

    
}
