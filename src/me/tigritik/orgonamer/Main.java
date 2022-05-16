package me.tigritik.orgonamer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {

  private static int N; // number of nodes
  private static List<List<Integer>> adj_list;
  private static int parentChainLength;

  public static void main(String[] args) throws IOException {
    fillAdjacencyList();

  }

  public static void fillAdjacencyList() throws IOException {
    BufferedReader bf = inputReader("Input.in");
    
    StringTokenizer st = new StringTokenizer(bf.readLine());

    N = Integer.parseInt(st.nextToken());
    adj_list = new ArrayList<>();

    for (int i = 0; i < N + 1; i++) {
      adj_list.add(new ArrayList<>());
    }

    while (bf.ready()) {
      st = new StringTokenizer(bf.readLine());
      int a = Integer.parseInt(st.nextToken()), b = Integer.parseInt(st.nextToken());

      adj_list.get(a).add(b);
      adj_list.get(b).add(a);
    }

  }

  public static BufferedReader inputReader(String fileName) throws IOException {
    return new BufferedReader(new FileReader(fileName));
  }

  public static int findLongestChain() {
    int firstLeaf = findFirstLeaf();
    
    

  }

  public static int findFirstLeaf(){
    for (int i = 0; i < adj_list.size(); i++){
      if (adj_list.get(i).size() == 0){
        return i;
      }
    }
    return -1;
  }

}
