package me.tigritik.orgonamer.chain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import me.tigritik.orgonamer.Compound;
import me.tigritik.orgonamer.Util;
import me.tigritik.orgonamer.nodes.Node;

public class Chain extends Compound{

  private final int length;
  private final Node[] nodes;
  private String name;

  public Chain(int length) {
    this.length = length;
    nodes = new Node[length + 1];
  }

  public Chain(Collection<Node> nodes) {
    this(nodes.size(), nodes);
  }

  public Chain(int length, Collection<Node> nodes) {
    this(length);
    int i = 1;
    for (Node n : nodes) {
      this.nodes[i] = n;
      i++;
    }
  }

  public int getChainLength() {
    return length;
  }

  public Node[] getNodes() {
    return nodes;
  }

  public boolean branchAt(int index) {
    return (nodes[index].getConnections().size() == 4);
  }

  public String toString() {
    return "prefixes-" + Util.PREFIX[length] + "ane";
  }

  public int compareTo(Chain b) {

    ArrayList<Integer> branchPointsThis = new ArrayList<Integer>(); // contains pooints in which it braches off
    ArrayList<Integer> branchPointsB = new ArrayList<Integer>();
    for (int i = 1; i < this.getNodes().length; i++) {
      if (this.branchAt(i)) {
        branchPointsThis.add(i);
      }
    }
    for (int i = 1; i < b.getNodes().length; i++) {
      if (this.branchAt(i)) {
        branchPointsB.add(i);
      }
    }

    int index = 0;

    if (branchPointsThis.size() < branchPointsB.size())
      while (index < branchPointsThis.size() && index < branchPointsB.size()) {
        if (branchPointsThis.get(index) < branchPointsB.get(index)) {
          return 1;
        } else if (branchPointsThis.get(index) > branchPointsB.get(index)) {
          return -1;
        } else {
          index++;
        }
      }

    if (branchPointsThis.size() == branchPointsB.size()) { // branching points are identical
      index = 0;
      String branchA = "", branchB = "";
      while (index < branchPointsThis.size()) {
        for (Node next : nodes[branchPointsThis.get(index)].getConnections()) {
          if (next != null && next.getIsPartOfParentChain() == false) {
            branchA = nameBranch(next, nodes[branchPointsThis.get(index)]);
          }
        }
        for (Node next : b.getNodes()[branchPointsB.get(index)].getConnections()) {
          if (next != null && next.getIsPartOfParentChain() == false) {
            branchB = nameBranch(next, nodes[branchPointsB.get(index)]);
          }
        }

        // comparetobranch
        if (compareName(branchA, branchB) > 0) {
          return 1;
        } 
        else if (compareName(branchA, branchB) < 0) {
          return -1;
        } 
        else {
          index++;
        }
      }
      // branhc name are also identical
      return 1;
    } else {
      if (branchPointsThis.size() > branchPointsB.size()) {
        return 1;
      } else {
        return -1;
      }
    }

  }

  public String nameBranch(Node start, Node parent) {
    String name = "";
    findLongestChain(start, parent);

    return name + nameBranch();
  }

  public ArrayList<Chain> findLongestChain(Node start, Node parent) {

    ArrayList<Chain> possibleParentChains = new ArrayList<>();

    int[][] info = bfs(start, parent); // info[0] stores distances, info[1] stores parents
    for (int i = 1; i < info[0].length; i++) {
      if (info[0][i] + 1 > getParentChainLength()) {
        possibleParentChains.clear();
        setParentChainLength(info[0][i] + 1);
        Chain L = new Chain(convertIntegersToNodes(findPath(info[1], start, i)));
        possibleParentChains.add(L);
      } else if (info[0][i] + 1 == getParentChainLength()) {
        Chain L = new Chain(convertIntegersToNodes(findPath(info[1], start, i)));
        possibleParentChains.add(L);
      }
    }
    return possibleParentChains;
  }

  public int[][] bfs(int start, int parent) {
    int[][] info = new int[2][getN() + 1]; // info[0] stores distances, info[1] stores parents
    Queue<Integer> q = new LinkedList<>();

    q.add(start);

    while (q.size() != 0) {
      int curr = q.poll();
      for (int i = 0; i < getAdjList().get(curr).size(); i++) {
        int next = getAdjList().get(curr).get(i);
        if (info[0][next] == 0 && next != start && next != parent) { // next node is not visited
          info[0][next] = info[0][curr] + 1;
          info[1][next] = curr;
          q.add(next);
        }
      }

    }

    return info;
  }

  public int compareName(String A, String B) {

    return 0;

  }

}