package me.tigritik.orgonamer.nodes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CarbonNode extends Node {

    private Node n1;
    private Node n2;
    private Node n3;
    private Node n4;

    public List<Node> getConnections() {
        Set<Node> s = new HashSet<>(4);
        s.add(n1);
        s.add(n2);
        s.add(n3);
        s.add(n4);
        return new ArrayList<>(s);
    }
}
