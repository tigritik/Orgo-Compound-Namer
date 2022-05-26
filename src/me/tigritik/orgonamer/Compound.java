package me.tigritik.orgonamer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import me.tigritik.orgonamer.chain.Chain;
import me.tigritik.orgonamer.nodes.FunctionalNode;
import me.tigritik.orgonamer.nodes.Node;


public class Compound{

    private int N; // number of nodes
    private List<List<Integer>> adjList; // the edges between nodes
    private int parentChainLength = 0; // length of parent chain
    private Node[] nodeList; //representation of nodes using objects

    public ArrayList<Chain> findLongestChain() {

    /*
     * Idea is as follows:
     * (1) find a leaf and run a bfs from it, noting the nodes furthest away from
     * it, stored in (furthestNodes)
     * (2) from all nodes in (furthestNodes), run another bfs, noting nodes furthest
     * away from it
     * (3) the paths with max length are possible parent chains
     */

    // finds leaf for (1)
    int firstLeaf = findFirstLeaf();

    // first bfs to find correct ending nodes
    int[][] info = bfs(firstLeaf);

    int maxLength = 0;
    ArrayList<Integer> furthestNodes = new ArrayList<>();

    for (int i = 1; i < info[0].length; i++) {
      if (info[0][i] + 1 > maxLength) {
        furthestNodes.clear();
        maxLength = info[0][i] + 1;
        furthestNodes.add(i);
      }

      else if (info[0][i] + 1 == maxLength) {
        furthestNodes.add(i);
      }
    }

    // stores all possible start and end nodes
    ArrayList<Chain> possibleParentChains = new ArrayList<>();

    // runs a bfs from all ending nodes
    for (int start : furthestNodes) {
      info = bfs(start); // info[0] stores distances, info[1] stores parents
      for (int i = 1; i < info[0].length; i++) {
        if (info[0][i] + 1 > parentChainLength) {
          possibleParentChains.clear();
          parentChainLength = info[0][i] + 1;
          Chain L = new Chain(convertIntegersToNodes(findPath(info[1], start, i)));
          possibleParentChains.add(L);
        } else if (info[0][i] + 1 == parentChainLength) {
          Chain L = new Chain(convertIntegersToNodes(findPath(info[1], start, i)));
          possibleParentChains.add(L);
        }
      }
    }

    return possibleParentChains;

  }

  // breadth first search from (start) node
  public int[][] bfs(int start) {
    int[][] info = new int[2][N + 1]; // info[0] stores distances, info[1] stores parents
    Queue<Integer> q = new LinkedList<>();

    q.add(start);

    while (q.size() != 0) {
      int curr = q.poll();
      for (int i = 0; i < adjList.get(curr).size(); i++) {
        int next = adjList.get(curr).get(i);
        if (info[0][next] == 0 && next != start) { // next node is not visited
          info[0][next] = info[0][curr] + 1;
          info[1][next] = curr;
          q.add(next);
        }
      }

    }

    return info;
  }

  public int findFirstLeaf() {
    for (int i = 0; i < adjList.size(); i++) {
      if (adjList.get(i).size() == 0) {
        return i;
      }
    }
    return -1;
  }

  // givne an array of parents, return the path
  public ArrayList<Integer> findPath(int[] parents, int start, int end){
    ArrayList<Integer> path = new ArrayList<>();
    int curr = end;
    while (curr != 0){
      path.add(curr);
      curr = parents[curr];
    }
    Collections.reverse(path);

    return path;
  } 

  public int getConnections(FunctionalNode n){
    return -1; 
  }

  public  void findBranches(Chain c){
    for (int i = 1; i < c.getChainLength() + 1; i++){
      
    }
  }

  private Collection<Node> convertIntegersToNodes(Collection<Integer> ints) {
    Collection<Node> c = new ArrayList<>(ints.size());
    for (int i : ints) {
      c.add(nodeList[i]);
    }
    return c;
  }

  public int getN() {
    return N;
  }

  public List<List<Integer>> getAdjList() {
    return adjList;
  }

  public int getParentChainLength() {
    return parentChainLength;
  }

  public Node[] getNodeList() {
    return nodeList;
  }

  public void setN(int n) {
    N = n;
  }
  
  public void setAdjList(List<List<Integer>> list) {
    adjList  = list;
  }

  public void setNodeList(Node[] list){
    nodeList = list;
  }

  
}