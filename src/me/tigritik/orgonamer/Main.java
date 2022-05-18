package me.tigritik.orgonamer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

  private static int N; // number of nodes
  private static List<List<Integer>> adjList; // the edges between nodes
  private static int parentChainLength  = 0; // length of parent chain

  public static void main(String[] args) throws IOException {

    //reads in inputs and fills out (adjList)
    fillAdjacencyList();
    
    //finds longest path in graph (stored in (parentChainLength))
    // (possibleParentChains) stores all the paths w length parentChainLength, stored as (start node, end node)
    ArrayList<List<Integer>> possibleParentChains = findLongestChain();

    
  }

  public static void fillAdjacencyList() throws IOException {
    BufferedReader bf = inputReader("Input.in");
    
    StringTokenizer st = new StringTokenizer(bf.readLine());

    N = Integer.parseInt(st.nextToken());
    adjList = new ArrayList<>(N);

    for (int i = 0; i < N + 1; i++) {
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

  // not sure if the best way to store the paths (given by start and end carbon, so its an arraylist of pairs)
  // is ArrayList<List<Integer>>
  public static ArrayList<List<Integer>> findLongestChain() {

    /*
    Idea is as follows:
    (1) find a leaf and run a bfs from it, noting the nodes furthest away from it, stored in (furthestNodes)
    (2) from all nodes in (furthestNodes), run another bfs, noting nodes furthest away from it
    (3) the paths with max length are possible parent chains
    */

    // finds leaf for (1)
    int firstLeaf = findFirstLeaf();

    // first bfs to find correct ending nodes
    int[] dist = bfs(firstLeaf);

    int maxLength = 0;
    ArrayList<Integer> furthestNodes = new ArrayList<>();

    for (int i = 0; i < dist.length; i++){
      if (dist[i] + 1 > maxLength){
        furthestNodes.clear();
        maxLength = dist[i] + 1;
        furthestNodes.add(i);
      }

      else if (dist[i] + 1 == maxLength){
        furthestNodes.add(i);
      }
    }
    
    // stores all possible start and end nodes 
    ArrayList<List<Integer>> pairs = new ArrayList<>();


    // runs a bfs from all ending nodes 
    for (int start : furthestNodes){
      dist = bfs(start);
      for (int i = 0; i < dist.length; i++){
        if (dist[i] + 1 > parentChainLength){
          pairs.clear();
          parentChainLength = dist[i] + 1;
          List<Integer> L = Arrays.asList(start, i);
          pairs.add(L);
        }
        else if (dist[i] + 1 == parentChainLength){
          List<Integer> L = Arrays.asList(start, i);
          pairs.add(L);
        }
      }
    }

    return pairs;

  }

  // breadth first search from (start) node
  public static int[] bfs(int start){
    int[] dist = new int[N+1];
    Queue<Integer> q = new LinkedList<>();

    q.add(start);
    
    while (q.size() != 0){
      int curr = q.poll();
      for (int i = 0; i < adjList.get(curr).size(); i++){
        int next = adjList.get(curr).get(i);
        if (dist[next] == 0 && next != start){ // next node is not visited
          dist[next] = dist[curr]+1;
          q.add(next);
        }
      }

    }

    return dist;
  }

  public static int findFirstLeaf(){
    for (int i = 0; i < adjList.size(); i++){
      if (adjList.get(i).size() == 0){
        return i;
      }
    }
    return -1;
  }

    //idk what you wanted to do jaiden but i wrote some temp code so we can get something working
    //idk what you guys intended for this so i commented it out for now
    /*
    int maxChainLength = 0;

    for (int node : findEdges()) {
      int length = 0;
      boolean[] visited = new boolean[N];

      while (!visited[node]) {
        if (adjList.get(node).size() <= 2) {
          visited[node] = true;
          length++;

        }
      }

      maxChainLength = Math.max(length, maxChainLength);
    }
  }

  private static int getChainLength(int node, boolean[] visited) {

    if (adjList.get(node).size() <= 2) {
      //for () the plan was to run this thing recursively
    }

  }

  private static List<Integer> findEdges() {
    List<Integer> list = new ArrayList<>();
    for (int i = 1; i < adjList.size(); i++){
      if (adjList.get(i).size() == 1){
        list.add(i);
      }
    }

    return list;
  }*/

  

}
