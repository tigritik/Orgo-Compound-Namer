package me.tigritik.orgonamer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {

  private static int N; // number of nodes
  private static List<List<Integer>> adjList;
  private static int parentChainLength;

  public static void main(String[] args) throws IOException {
    fillAdjacencyList();

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

  public static int findLongestChain() {
    int firstLeaf = findFirstLeaf();

    //idk what you wanted to do jaiden but i wrote some temp code so we can get something working
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
  }

  public static int findFirstLeaf(){
    for (int i = 0; i < adjList.size(); i++){
      if (adjList.get(i).size() == 0){
        return i;
      }
    }
    return -1;
  }

}
