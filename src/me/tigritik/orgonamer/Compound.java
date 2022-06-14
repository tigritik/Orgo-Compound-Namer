package me.tigritik.orgonamer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.util.*;

import javax.naming.directory.AttributeInUseException;

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
    private int[] nodeType;
    // 0: carbon, 1: N3(azido), 2: Br(bromo), 3: Cl(Chloro), 4: F(fluoro), 5: I(iodo), 6: NO2(nitro)
    
    // 1 2
    // 2 3
    // 3 4
    // 
    // FUNCTIONAL
    // 4 
    // 5 br
 
    
    public Compound() throws IOException {
      fillAdjacencyList();
    }

    public Compound(int start, List<List<Integer> > tempAdjList, int[] nodeTypes){
      headNode = start;
      N = tempAdjList.size()-1;
      adjList = tempAdjList;
      nodeType = nodeTypes;
    }

    //returns a list of chains with size N (1<=N<=possibleParentChains.size())
    // public List<Chain> getLongestChainIncludingFunctionals() {
      
    //     // Map<String, Integer> priorities = Map.ofEntries(
    //     //     entry("Azido",1),
    //     //     entry("Bromo",2),
    //     //     entry("Chloro",3),
    //     //     entry("Fluoro")

    //     // )

        
    //     List<Chain> priorityList = new ArrayList<>();   
    //     List<Integer> carbonsIndex = new ArrayList<>();
    //     List<Integer> functionalIndex = new ArrayList<>();
        
    //     //create all possible chains of connections, return the chain that has the indexWithHighestPriority in it, if there are multiple, compare and return
    //     for(int i = 1; i<=adjList.size(); i++) {
    //       for(int index : adjList.get(i)) {
    //           if(nodeType[index]==0 && !(carbonsIndex.contains(index))) {
    //             carbonsIndex.add(index);
    //           } else if (nodeType[index]>6){
    //             functionalIndex.add(index);
    //           }
    //       }
    //     }

    //     if(functionalIndex.size()==0) {
    //       return findLongestChain();
    //     }
    //     Collections.sort(functionalIndex); 
    //     int indexWithHighestPriority = functionalIndex.get(functionalIndex.size()-1); 

    //     return priorityList;
        
    // }

    // public int getNumCarbonConnections(int index) {
      
    // }

    public void fillAdjacencyList() throws IOException {
      Input input = new Input("Input.in");
      StringTokenizer st = input.getStringTokenizer();
      BufferedReader bf = input.getBufferedReader();
      
      N = (Integer.parseInt(st.nextToken()));
      nodeType = new int[N+1];
      nodeList = (new Node[N + 1]);
  
      for (int i = 0; i < N + 1; i++) {
        adjList.add(new ArrayList<>());
        
      }
      
      
      while (bf.ready()) {
        st = new StringTokenizer(bf.readLine());

        String temp1 = st.nextToken();
        String temp2 = st.nextToken();
        if (temp1.equals("FUNCTIONAL")){
          break;
        }

        int a = Integer.parseInt(temp1), b = Integer.parseInt(temp2);
  
        adjList.get(a).add(b);
        adjList.get(b).add(a);
  
        //nodeList[a].addConnection(nodeList[b]);
        //nodeList[b].addConnection(nodeList[a]);
      }
      
      Map<String, Integer> typeToInt = new HashMap<String, Integer>();
      typeToInt.put("N3", 1);
      typeToInt.put("Br", 2);
      typeToInt.put("Cl", 3);
      typeToInt.put("F", 4);
      typeToInt.put("I", 5);
      typeToInt.put("NO2", 6);

      while(bf.ready()) {
        StringTokenizer stNew = new StringTokenizer(bf.readLine());
        int c = Integer.parseInt(stNew.nextToken());
        String b = stNew.nextToken();
        
        nodeType[c] = typeToInt.get(b);
        
      
  
      

      //read in input
      }
  
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
      

      
      int[][] info = bfs(firstLeaf, -1);

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
            Chain L1 = new Chain((findReversePath(info[1], start, i)), this);

            //System.out.println(this.getAdjList());
            possibleParentChains.add(L);
            possibleParentChains.add(L1);
          } 
          else if (info[0][i] + 1 == parentChainLength) {
            Chain L = new Chain((findPath(info[1], start, i)), this);
            Chain L1 = new Chain((findReversePath(info[1], start, i)), this);

            //System.out.println(this.getAdjList());
            possibleParentChains.add(L);
            possibleParentChains.add(L1);

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
    //System.out.println("NEW BFS\n\n");
    while (q.size() != 0) {
      int curr = q.poll();

      // System.out.println("curr: " + curr);
      // for (int s : q){
      //   System.out.print(q + " ");
      // }
     // System.out.println("Queue done");
      for (int i = 0; i < adjList.get(curr).size(); i++) {
        int next = adjList.get(curr).get(i);
        if (info[0][next] == 0 && next != start && next != parent && nodeType[next] == 0) { // next node is not visited
          info[0][next] = info[0][curr] + 1;
          info[1][next] = curr;
          q.add(next);
        }
      }

    }

    return info;
  }

  public int findFirstLeaf() {
    for (int i = 1; i < adjList.size(); i++) {
      if (adjList.get(i).size() == 1  && nodeType[i] == 0) {
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

  public ArrayList<Integer> findReversePath(int[] parents, int start, int end){
    ArrayList<Integer> path = new ArrayList<>();
    int curr = end;
    while (curr != 0){
      path.add(curr);
      curr = parents[curr];
    }
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

    /*PriorityQueue<Chain> pq = new PriorityQueue<>(possibleParentChainList);
    finalParentChain = pq.remove();*/
  }

  //TODO
  public String getName(Boolean isPartOfFinalParentChain) {

    if (adjList.size() == 2){
      switch(nodeType[2]){
        case 0: 
          return "methyl";
        case 1: 
          return "azido";
        case 2:
          return "bromo";
        case 3: 
          return "chloro";
        case 4:
          return "fluoro";
        case 5:
          return "iodo";
        case 6:
          return "nitro";
      }
    }
    ArrayList<Chain> possibleParentChains = findLongestChain();
    

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
          List<Integer> usedIndices = newAdjList.get(newAdjList.size()-1);
          newAdjList.remove(newAdjList.size()-1);
          
          int[] newNodeTypes = new int[newAdjList.size()];
          for (int newNode = 1; newNode < newAdjList.size()-1; newNode++){
            newNodeTypes[newNode] = nodeType[usedIndices.get(newNode-1)];
          }

          int normalizedStart = newAdjList.get(newAdjList.size()-1).get(0);
          newAdjList.remove(newAdjList.size()-1);

          
          Compound fromBranch = new Compound(normalizedStart, newAdjList, newNodeTypes);  
        

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
    if (tempAdjList.size() == 2){
      //tempAdjList.add(new ArrayList<Integer>());
      usedIndices.add(0);
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
    tempAdjList.add(new ArrayList<Integer>(Arrays.asList(normalizedStart)));
    tempAdjList.add(usedIndices);
    
    

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
  
  public int[] getNodeType(){
    return nodeType;
  }

}

