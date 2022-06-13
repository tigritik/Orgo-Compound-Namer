package me.tigritik.orgonamer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.StringTokenizer;

import me.tigritik.orgonamer.chain.Chain;
import me.tigritik.orgonamer.nodes.CarbonNode;
import me.tigritik.orgonamer.nodes.FunctionalNode;
import me.tigritik.orgonamer.nodes.Node;


public class Compound{

  private final List<String> IGNORABLES = Arrays.asList("(",")",",","-","di", "tri", "tetra", "penta", "hexa", "hepta", "1", "2", "3", "4", "5", "6", "7", "8","9", "sec", "tert");
    private int N; // number of nodes
    private List<List<Integer>> adjList = new ArrayList<List<Integer>>(); // the edges between nodes
    private int parentChainLength = 0; // length of parent chain
    private Node[] nodeList; //representation of nodes using objects
    private Chain finalParentChain;
    private String name;
    private int headNode = -1;
    public static int counter = 0;
    //private boolean[] isCarbon = new boolean[N+1]; 

    public Compound() throws IOException{
      fillAdjacencyList();
    }

    public Compound(int start, List<List<Integer> > tempAdjList){
      headNode = start;
      N = tempAdjList.size()-1;
      adjList = tempAdjList;
      //this.isCarbon = isCarbon;
    }

    public void fillAdjacencyList() throws IOException {
      Input input = new Input("Input.in");
      StringTokenizer st = input.getStringTokenizer();
      BufferedReader bf = input.getBufferedReader();
      
      N = (Integer.parseInt(st.nextToken()));
      nodeList = (new Node[N + 1]);
  
      for (int i = 0; i < N + 1; i++) {
        adjList.add(new ArrayList<>());
        nodeList[i] = new CarbonNode(i);
      }
  
      while (bf.ready()) {
        st = new StringTokenizer(bf.readLine());
        int a = Integer.parseInt(st.nextToken()), b = Integer.parseInt(st.nextToken());
  
        adjList.get(a).add(b);
        adjList.get(b).add(a);
      
        //nodeList[a].addConnection(nodeList[b]);
        //nodeList[b].addConnection(nodeList[a]);
      }

      //read in input
    
  
    }

    public ArrayList<Chain> findLongestChain(){
        
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
      System.out.println("headNode: " + headNode);
      System.out.println("First leaf: " + firstLeaf);
      System.out.println(adjList);

      // first bfs to find correct ending nodes
      int[][] info = bfs(firstLeaf, -1);
      System.out.println(Arrays.toString(info[0]));

      int maxLength = 0;
      ArrayList<Integer> furthestNodes = new ArrayList<>();

      if (headNode == -1){
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
      }
      else{
        furthestNodes.add(headNode);
      }

      // stores all possible start and end nodes
      ArrayList<Chain> possibleParentChains = new ArrayList<>();

      // runs a bfs from all ending nodes
      for (int start : furthestNodes) {
        info = bfs(start, -1); // info[0] stores distances, info[1] stores parents
        for (int i = 1; i < info[0].length; i++) {
          if (info[0][i] + 1 > parentChainLength) {
            possibleParentChains.clear();
            parentChainLength = info[0][i] + 1;
            Chain L = new Chain((findPath(info[1], start, i)), this);
            //System.out.println(this.getAdjList());
            possibleParentChains.add(L);
          } else if (info[0][i] + 1 == parentChainLength) {
            Chain L = new Chain((findPath(info[1], start, i)), this);
            //System.out.println(this.getAdjList());
            possibleParentChains.add(L);
          }
        }
      }

      return possibleParentChains;

  }


  // breadth first search from (start) node
  public int[][] bfs(int start, int parent) {
    int[][] info = new int[2][N + 1]; // info[0] stores distances, info[1] stores parents
    Queue<Integer> q = new LinkedList<>();

    q.add(start);
    System.out.println("NEW BFS\n\n");
    while (q.size() != 0) {
      int curr = q.poll();

      System.out.println("curr: " + curr);
      for (int s : q){
        System.out.print(q + " ");
      }
      System.out.println("Queue done");
      for (int i = 0; i < adjList.get(curr).size(); i++) {
        int next = adjList.get(curr).get(i);
        if (info[0][next] == 0 && next != start && next != parent) { // next node is not visited
          info[0][next] = info[0][curr] + 1;
          info[1][next] = curr;
          System.out.println("parent: " + curr + " child: " + next);
          q.add(next);
        }
      }

    }

    return info;
  }

  public int findFirstLeaf() {
    for (int i = 0; i < adjList.size(); i++) {
      if (adjList.get(i).size() == 1  ) {
        //System.out.println("Returned: " + i);
        return i;
      }
    }
    //System.out.println("Returned: " + -1);
    return -1;
  }

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

  public void findFinalParentChain(ArrayList<Chain> possibleParentChainList) {
    Chain currentBest = possibleParentChainList.get(0);

    for (int i = 1; i < possibleParentChainList.size(); i++){
      if(currentBest.compareTo(possibleParentChainList.get(i))<0) {
        currentBest = possibleParentChainList.get(i);
      }
    }
    finalParentChain = currentBest; 
  }

  //TODO
  public String getName(Boolean isPartOfFinalParentChain) {
    if (adjList.size() == 2){
      return "methyl";
    }
    ArrayList<Chain> possibleParentChains = findLongestChain();
    
    // for (Chain c : possibleParentChains){
    //   for (int i : c.getNodes()){
    //     System.out.print(i + " ");
    //   }
    //   System.out.println();
    // }
    

    // for (int i = 0; i < adjList.size(); i++){
    //   List<Integer> A = adjList.get(i);
    //   System.out.print(i + ": "); 
    //   for (int I : A){
    //     System.out.print(I + " ");
    //   }
    //   System.out.println(" size: " + A.size());
    // }
    // System.out.println("Done");
    // System.out.println("\n");
    // for (int i : finalParentChain.getNodes()){
    //   System.out.print(i + " ");
    // }
    // System.out.println();
    // System.out.println("HeadNode: " + headNode);

    //return "";

    //go thru final parent chain in order
    //at branch points, store indices + call nameBranch
    // Map < String, ArrayList<Int>> 
    // sort branch names by compareNameBranch
    //combine evrything

    
    findFinalParentChain(possibleParentChains);


    String name = "";
    ArrayList<Integer> branchPoints = finalParentChain.returnBranchIndices();

    Map<String, ArrayList<Integer>> branchNames = new HashMap<String,ArrayList<Integer>>();
    // takes branch name to indicies it occurs at
    
    for (int i = 0; i < branchPoints.size(); i++){
      for(int next : adjList.get(branchPoints.get(i))){
        if (contains(finalParentChain.getNodes(), next) == false){

          List<List<Integer> > newAdjList = createAdjList(next, (int)branchPoints.get(i));
          int normalizedStart = newAdjList.get(newAdjList.size()-1).get(0);
          newAdjList.remove(newAdjList.size()-1);
          
          Compound fromBranch = new Compound(normalizedStart, newAdjList);  
        

          String nameOfBranch = fromBranch.getName(false);
          
          switch (nameOfBranch){
            case "1-methylethyl": 
              nameOfBranch = "isopropyl";
              break;
            case "1-methylpropyl":
              nameOfBranch = "sec-butyl";
              break;
            case "2-methylpropyl":
              nameOfBranch = "isobutyl";
              break;
            case "1,1-dimethylethyl":
              nameOfBranch = "tert-butyl";
              break;
          }
          
          if (!branchNames.containsKey(nameOfBranch)){
            branchNames.put(nameOfBranch, new ArrayList<Integer>());
          }
          branchNames.get(nameOfBranch).add(indexOf(finalParentChain.getNodes(), branchPoints.get(i)));
        }
      }
    }

    // one branch name is (2,3-dimethyl)
    // one is (4-ethyl)
  
    // 1-(2,3-dietyhlpropyl) - 5 -ethyloctane

    ArrayList<String> prefixSort = new ArrayList<>();

    for (Map.Entry<String, ArrayList<Integer>> entry : branchNames.entrySet()) {
      prefixSort.add(entry.getKey());
    }
    
    Collections.sort(prefixSort, (s1, s2) -> compareNames(s1, s2));
    int n = 0;
    for (String s : prefixSort){
      for (int i  = 0; i < branchNames.get(s).size(); i++){
        if (i == branchNames.get(s).size() -1){
          name += branchNames.get(s).get(i) + "-" + Util.NUMERICPREFIX[branchNames.get(s).size()];
          if (Character.isDigit(s.charAt(0))){
            name += "(" + s + ")";
          }
          else{
            name += s;
          }
          if (n != branchNames.entrySet().size()-1){
            name += "-";
          }
        }
        else{
          name += branchNames.get(s).get(i) + ",";
        }
      }
      n++;
    }


    if (isPartOfFinalParentChain == true){
      name += Util.PREFIX[parentChainLength] + "ane";
    }    
    else{
      name += Util.PREFIX[parentChainLength] + "yl";

    }

    this.name = name;
    return name;
  }
  
  //returns -1 if String a is alphabetically first than String b, and +1 if String is alphabetically first. returns 0 if the strings are equal,.
  public int compareNames(String a, String b) {

    if(a.equals(b)) {
      return 0;
    }

    for (int i = 0; i < IGNORABLES.size(); i++) {
      String s = IGNORABLES.get(i);
      while (a.contains(s)) {
        int index = a.indexOf(s);
        a = a.substring(0, index) + a.substring(s.length() + index);
      }
    }

    for (int i = 0; i < IGNORABLES.size(); i++) {
      String s = IGNORABLES.get(i);
      while (b.contains(s)) {
        int index = b.indexOf(s);
        b = b.substring(0, index) + b.substring(s.length() + index);
      }
    }

    return a.compareTo(b);

  }

  // (), , , di, tri , 
  // 2,3 - di   ethylprop
  // eth


  public List<List<Integer>> createAdjList(int start, int parent){
    List<List<Integer> > tempAdjList = new ArrayList<List<Integer>>();
    ArrayList<Integer> usedIndices = new ArrayList<>();

    
    int[][] info = bfs(start, parent);

    tempAdjList.add(new ArrayList<Integer>());
    for (int i = 1; i <= N; i++){
      if (info[0][i] != 0){
        ArrayList<Integer> connections = new ArrayList<>();
        for (int next : adjList.get(i)){
          connections.add(next);
          //usedIndices.add(next);
        }    
        tempAdjList.add(connections);
        usedIndices.add(i);
      }
      else if (i == start){
        ArrayList<Integer> connections = new ArrayList<>();
        for (int next : adjList.get(i)){
          if (next != parent){
            connections.add(next);
            //usedIndices.add(next);
          }
        }    
        tempAdjList.add(connections);
        usedIndices.add(i);
      }
    }
    if (tempAdjList.size() == 1){
      tempAdjList.add(new ArrayList<Integer>());
    }

    // normalize ints
    Collections.sort(usedIndices);
    Map<Integer, Integer> normalizedIndices = new HashMap<>();
    int normalizedStart = 0;

    int index = 1;
    for (int i : usedIndices){
      if (normalizedIndices.containsKey(i) == false){
        if (i == start){
          normalizedStart = index;
        }
        normalizedIndices.put(i, index);
        index++;
      }
    }
    for (int i = 1; i < tempAdjList.size(); i++){
      for (int j = 0; j < tempAdjList.get(i).size(); j++){
        tempAdjList.get(i).set(j, normalizedIndices.get(tempAdjList.get(i).get(j)));
      }
    }

    tempAdjList.add(new ArrayList<>(Arrays.asList(normalizedStart)));
    
    

    return tempAdjList;
  }

  


  public int getConnections(FunctionalNode n){
    return -1; 
  }

  public  void findBranches(Chain c){
    for (int i = 1; i < c.getChainLength() + 1; i++){
      
    }
  }

  public Collection<Node> convertIntegersToNodes(Collection<Integer> ints) {
    Collection<Node> c = new ArrayList<>(ints.size());
    for (int i : ints) {
      c.add(nodeList[i]);
    }
    return c;
  }

  public boolean contains(int[] arr, int x){
    for (int i : arr){
      if (i == x){
        return true;
      }
    }
    return false;
  }

  public int indexOf(int[] arr, int x){
    for (int i = 0; i < arr.length; i++){
      if (arr[i] == x){
        return i;
      }
    }
    return -1;
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

  public void setParentChainLength(int length){
    parentChainLength = length;
  }

  public int getCounter() {
    return counter;
  }
  
}
