package me.tigritik.orgonamer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
<<<<<<< Updated upstream
=======
import java.util.Collection;
>>>>>>> Stashed changes
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

<<<<<<< Updated upstream
import me.tigritik.orgonamer.Chain.Chain;
=======
import me.tigritik.orgonamer.chain.Chain;
import me.tigritik.orgonamer.nodes.CarbonNode;
>>>>>>> Stashed changes
import me.tigritik.orgonamer.nodes.FunctionalNode;

public class Main {

  // Nodes are 1-indexed
  private static int N; // number of nodes
  private static List<List<Integer>> adjList; // the edges between nodes
  private static int parentChainLength = 0; // length of parent chain

  public static void main(String[] args) throws IOException {

    // reads in inputs and fills out (adjList)
    fillAdjacencyList();

    
    ArrayList<Chain> possibleParentChains = findLongestChain();

<<<<<<< Updated upstream
    for (Chain c : possibleParentChains){
      for (int n : c.getNode()){
        System.out.print(n + " ");
      }
=======
    for (Chain c : possibleParentChains) {
      for (int i = 1; i < c.getNodes().length; i++) {
        System.out.print(c.getNodes()[i] + " ");
        System.out.println();
        if (c.branchAt(i)) {
          System.out.println(i + "-" + nodeList[i].getConnections() + "-");
        }
      }
      System.out.println(c);
>>>>>>> Stashed changes
      System.out.println();
    }

  }

  public static void fillAdjacencyList() throws IOException {
    BufferedReader bf = inputReader("Input.in");

    StringTokenizer st = new StringTokenizer(bf.readLine());

    N = Integer.parseInt(st.nextToken());
    adjList = new ArrayList<>(N);

    for (int i = 0; i < N+1; i++) {
      adjList.add(new ArrayList<>());
    }

    while (bf.ready()) {
      st = new StringTokenizer(bf.readLine());
      int a = Integer.parseInt(st.nextToken()), b = Integer.parseInt(st.nextToken());

      adjList.get(a).add(b);
      adjList.get(b).add(a);
    }

  }

  public static BufferedReader inputReader(String fileName) throws IOException {
    return new BufferedReader(new FileReader(fileName));
  }

  // not sure if the best way to store the paths (given by start and end carbon,
  // so its an arraylist of pairs)
  // is ArrayList<List<Integer>>
  public static ArrayList<Chain> findLongestChain() {

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
          Chain L = new Chain(findPath(info[1], start, i));
          possibleParentChains.add(L);
        } else if (info[0][i] + 1 == parentChainLength) {
          Chain L = new Chain(findPath(info[1], start, i));
          possibleParentChains.add(L);
        }
      }
    }

    return possibleParentChains;

  }

  // breadth first search from (start) node
  public static int[][] bfs(int start) {
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

  public static int findFirstLeaf() {
    for (int i = 0; i < adjList.size(); i++) {
      if (adjList.get(i).size() == 0) {
        return i;
      }
    }
    return -1;
  }

  // givne an array of parents, return the path
  public static ArrayList<Integer> findPath(int[] parents, int start, int end){
    ArrayList<Integer> path = new ArrayList<>();
    int curr = end;
    while (curr != 0){
      path.add(curr);
      curr = parents[curr];
    }
    Collections.reverse(path);

    return path;
  } 

  public static int getConnections(FunctionalNode n){
    return -1; 
  }

  public static void findBranches(Chain c){
    for (int i = 1; i < c.getChainLength(c) + 1; i++){
      
    }
  }

  
  
}
