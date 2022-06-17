package me.tigritik.orgonamer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.ParentBuilder;

import java.util.Queue;
import java.util.StringTokenizer;

import me.tigritik.orgonamer.chain.Chain;
import me.tigritik.orgonamer.nodes.FunctionalNode;
import me.tigritik.orgonamer.nodes.Node;

public class Compound {

  private final List<String> IGNORABLES = Arrays.asList("(", ")", ",", "-", "di", "tri", "tetra", "penta", "hexa",
      "hepta", "1", "2", "3", "4", "5", "6", "7", "8", "9", "sec", "tert");
  private int N; // number of nodes
  private List<List<Integer>> adjList = new ArrayList<List<Integer>>(); // the edges between nodes
  private int parentChainLength = 0; // length of parent chain
  private Node[] nodeList; // representation of nodes using objects
  private Chain finalParentChain;
  private String name;
  private int headNode = -1;
  public static int counter = 0;
  private int[] nodeType;
  private List<Integer> functionalNodeIndex;
  private int highestFunctionalNodeType;
  private int[][] bondType;

  // 0: carbon, 1: N3(azido), 2: Br(bromo), 3: Cl(Chloro), 4: F(fluoro), 5:
  // I(iodo), 6: NO2(nitro),
  // 7 ether, 8 amine, 9 thiol (-SH), 10 alcohol, ... 16 carboxylic acid

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

  public Compound(int start, List<List<Integer>> tempAdjList, int[] nodeTypes, int[][] bondTypes) {
    headNode = start;
    N = tempAdjList.size() - 1;
    bondType = bondTypes;
    adjList = tempAdjList;
    nodeType = nodeTypes;
    for (int x : nodeType) {
      if (x > highestFunctionalNodeType)
        highestFunctionalNodeType = x;
    }

  }

  public void fillAdjacencyList() throws IOException {
    Input input = new Input("Input.in");
    StringTokenizer st = input.getStringTokenizer();
    BufferedReader bf = input.getBufferedReader();

    N = (Integer.parseInt(st.nextToken()));
    nodeType = new int[N + 1];
    bondType = new int[N + 1][N + 1];
    nodeList = (new Node[N + 1]);

    for (int i = 0; i < N + 1; i++) {
      adjList.add(new ArrayList<>());

    }

    while (bf.ready()) {
      st = new StringTokenizer(bf.readLine());

      String temp1 = st.nextToken();
      String temp2 = st.nextToken();
      if (temp1.equals("FUNCTIONAL")) {
        break;
      }

      int a = Integer.parseInt(temp1), b = Integer.parseInt(temp2);
      if (bondType[a][b] == 0){
        adjList.get(a).add(b);
        adjList.get(b).add(a);
      }

      bondType[a][b]++;
      bondType[b][a]++;

      // nodeList[a].addConnection(nodeList[b]);
      // nodeList[b].addConnection(nodeList[a]);
    }

    Map<String, Integer> typeToInt = new HashMap<String, Integer>();
    typeToInt.put("N3", 1);
    typeToInt.put("Br", 2);
    typeToInt.put("Cl", 3);
    typeToInt.put("F", 4);
    typeToInt.put("I", 5);
    typeToInt.put("NO2", 6);
    typeToInt.put("ETH", 7);
    typeToInt.put("NH2", 8);
    typeToInt.put("S", 9);
    typeToInt.put("OH", 10);
    typeToInt.put("O", 11);
    typeToInt.put("COH", 12);
    typeToInt.put("CN", 13);
    typeToInt.put("CONH2", 14);
    typeToInt.put("Ester", 15);
    typeToInt.put("COOH", 16);

    while (bf.ready()) {
      StringTokenizer stNew = new StringTokenizer(bf.readLine());
      int c = Integer.parseInt(stNew.nextToken());
      String b = stNew.nextToken();

      nodeType[c] = typeToInt.get(b);
      if (typeToInt.get(b) > highestFunctionalNodeType && typeToInt.get(b) > 6) {
        highestFunctionalNodeType = typeToInt.get(b);
      }

      // read in input
    }

    //
  }

  

  // returns a list of chains with size N (1<=N<=possibleParentChains.size())
  public ArrayList<Chain> getLongestChainIncludingFunctionals() {


    // Map<String, Integer> priorities = Map.ofEntries(
    // entry("Azido",1),
    // entry("Bromo",2),
    // entry("Chloro",3),
    // entry("Fluoro")

    // )

    List<Chain> priorityList = new ArrayList<>();
    List<Integer> carbonsIndex = new ArrayList<>();
    List<Integer> functionalIndex = new ArrayList<>();

    // create all possible chains of connections, return the chain that has the
    // indexWithHighestPriority in it, if there are multiple, compare and return

  
    for (int i = 1; i < adjList.size(); i++) {
      for (int index : adjList.get(i)) {
        if (nodeType[index] == 0 && !(carbonsIndex.contains(index))) {
          carbonsIndex.add(index);
        } else if (nodeType[index] > 6) {
          functionalIndex.add(index);
        }
      }
    }
    // if no functional groups with nodeType>6 are found, calls findLongestChain()
    // (default arg)
    // finds indexes of carbon and functionals

    if (functionalIndex.size() == 0) {
      return findLongestChain();
    }
    

    // nodeType[index] ive never seen this syntax b4 no idea
    Collections.sort(functionalIndex, new Comparator<Integer>() {
      @Override
      public int compare(Integer F1, Integer F2) {
        return nodeType[F1] - nodeType[F2];
      }
    });

    int currBestFunctional = 0;

    List<Integer> indicesHighestF = new ArrayList<>();
    for (int i : functionalIndex) {
      if (nodeType[i] == currBestFunctional) {
        currBestFunctional = nodeType[i];
        indicesHighestF.add(i);
      } else if (nodeType[i] > currBestFunctional) {
        indicesHighestF.clear();
        currBestFunctional = nodeType[i];
        indicesHighestF.add(i);
      }
    }

    List<Integer> leafIndices = new ArrayList<>();
    for (int i = 1; i <= N; i++) {

      int counter = 0;
      for (int x : adjList.get(i)) {
        if (nodeType[x] == 0 || nodeType[x] > 7) {
          counter++;
        }
      }
      if (counter == 1 && (nodeType[i] == 0 || nodeType[i] > 7)) {
        leafIndices.add(i);
      }
    }

    ArrayList<Chain> possibleParentChains = new ArrayList<>();

    for (int leaf : leafIndices) {
      ArrayList<Chain> parentChainsFromLeaf = pathsFromLeaf(leaf, leafIndices);
      for (Chain c : parentChainsFromLeaf) {
        possibleParentChains.add(c);
      }
    }

    // current idea:
    // find indices of highest similar functional nodes
    // traverse graph and get distinct traversals that incluyde all necessary
    // indices
    // 5 2,3,4,6
    // bfs (2, 5)

    return possibleParentChains;

  }

  public ArrayList<Chain> pathsFromLeaf(int start, List<Integer> leafIndices) {

    ArrayList<Chain> paths = new ArrayList<>();

    

    int[] parents = bfs(start, -1)[1];

    for (int leaf : leafIndices) {
      if (leaf != start) {
        paths.add(new Chain(findPath(parents, start, leaf), this));
      }
    }

    return paths;
  }

  public void findCorrectLongestParentChainIncludingFunctionalGroups(List<Chain> possibleParentChains) {
    Chain currentBest = possibleParentChains.get(0);
    

    for (int i = 1; i < possibleParentChains.size(); i++) {

      
      if (currentBest.compareTo(possibleParentChains.get(i)) < 0) {
        currentBest = possibleParentChains.get(i);
      }

    }
    finalParentChain = currentBest;

    parentChainLength = finalParentChain.getChainLength();

  }

  public int getNumCarbonConnections(int index) {
    int carbonCount = 0;
    if (adjList.size() == 0) {
      return -1;
    }

    for (int i = 1; i <= adjList.get(index).size(); i++) {
      if (nodeType[i] == 0) {
        carbonCount++;
      }
    }
    return carbonCount;
  }

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


    int[][] info = bfs(firstLeaf, -1);


    int maxLength = 0;
    ArrayList<Integer> furthestNodes = new ArrayList<>();

    if (headNode == -1) {
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
    } else {
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

          // System.out.println(this.getAdjList());
          possibleParentChains.add(L);
          possibleParentChains.add(L1);

        } 
        else if (info[0][i] + 1 == parentChainLength) {
          Chain L = new Chain((findPath(info[1], start, i)), this);
          Chain L1 = new Chain((findReversePath(info[1], start, i)), this);

          // System.out.println(this.getAdjList());
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
    // System.out.println("NEW BFS\n\n");
    while (q.size() != 0) {
      int curr = q.poll();

      // System.out.println("curr: " + curr);
      // for (int s : q){
      // System.out.print(q + " ");
      // }
      // System.out.println("Queue done");
      for (int i = 0; i < adjList.get(curr).size(); i++) {
        int next = adjList.get(curr).get(i);
        if (info[0][next] == 0 && next != start && next != parent && (nodeType[next] == 0 || nodeType[next] > 7)) { // next
                                                                                                                    // node
                                                                                                                    // is
                                                                                                                    // not
                                                                                                                    // visited
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
      int counter = 0;
      for (int x : adjList.get(i)) {
        if (nodeType[x] == 0 || nodeType[x] > 6) {
          counter++;
        }
      }

      if (counter == 1 && nodeType[i] == 0) {
        // System.out.println("Returned: " + i);
        return i;
      }
    }
    // System.out.println("Returned: " + -1);
    return -1;
  }

  public ArrayList<Integer> findPath(int[] parents, int start, int end) {
    ArrayList<Integer> path = new ArrayList<>();
    int curr = end;
    while (curr != 0) {
      path.add(curr);
      curr = parents[curr];
    }
    Collections.reverse(path);

    return path;
  }

  public ArrayList<Integer> findReversePath(int[] parents, int start, int end) {
    ArrayList<Integer> path = new ArrayList<>();
    int curr = end;
    while (curr != 0) {
      path.add(curr);
      curr = parents[curr];
    }
    return path;
  }

  // TODO
  public String getName(Boolean isPartOfFinalParentChain) {
    // System.out.println("nodeTypes:" );
    // for (int x : nodeType){
    // System.out.print(x + " ");
    // }
    // System.out.println();
    // for (int x : nodeType){
    //   System.out.print(x + " ");
    // }
    // System.out.println();

    if (adjList.size() == 2) {
      return Util.funcPrefix[nodeType[0]];
    }

    ArrayList<Chain> possibleParentChains = getLongestChainIncludingFunctionals();



    // go thru final parent chain in order
    // at branch points, store indices + call nameBranch
    // Map < String, ArrayList<Int>>
    // sort branch names by compareNameBranch
    // combine evrything

    findCorrectLongestParentChainIncludingFunctionalGroups(possibleParentChains);

    // for (int x : finalParentChain.getNodes()){
    // System.out.print(x + " ");
    // }
    // System.out.println();

    if (nodeType[finalParentChain.getNodes()[finalParentChain.getNodes().length - 1]] > 6
        && nodeType[finalParentChain.getNodes()[finalParentChain.getNodes().length - 1]] < highestFunctionalNodeType) {
      ArrayList<Integer> tempParentChainNodes = new ArrayList<>();
      for (int i = 1; i < finalParentChain.getNodes().length - 1; i++) {
        tempParentChainNodes.add(finalParentChain.getNodes()[i]);
      }
      finalParentChain = new Chain(tempParentChainNodes, this);
    }
    parentChainLength = finalParentChain.getChainLength();

    // for (int x : finalParentChain.getNodes()){
    // System.out.print(x + " ");
    // }
    // System.out.println(parentChainLength);

    // System.out.print("final parent chain: ");
    // for (int x : finalParentChain.getNodes()){
    // System.out.print(x + " ");
    // }
    // System.out.println();
    // System.out.println("highest func node type: " + highestFunctionalNodeType);
    // System.out.println(N);

    String name = "";
    ArrayList<Integer> branchPoints = finalParentChain.returnBranchIndices();


    // System.out.println("branchPOints: " + branchPoints);

    // System.out.println("branchPoints: " + branchPoints);
    Map<String, ArrayList<Integer>> branchNames = new HashMap<String, ArrayList<Integer>>();
    // takes branch name to indicies it occurs at

    List<Integer> highestFuncIndex = new ArrayList<>();
    String suffix = "";
    if (highestFunctionalNodeType > 6) {

      // System.out.println("reached");
      suffix = Util.funcSuffix[highestFunctionalNodeType];

      if (highestFunctionalNodeType == 7) {
        name += indexOf(finalParentChain.getNodes(), branchPoints.get(0)) + "-";
        if (nameEther().length() != 0){
          String s = nameEther();
          if (Character.isDigit(s.charAt(0))){
            name += "(" + s + ")";
          }
          else{
            name += s;        
          }
        }
      }

      // we odnt know if this is right
      for (int i = 0; i < finalParentChain.getNodes().length; i++) {
        int x = finalParentChain.getNodes()[i];
        if (nodeType[x] == highestFunctionalNodeType) {
          highestFuncIndex.add(indexOf(finalParentChain.getNodes(), x)); // kinda sketchy but it works
        } else if (nodeType[x] > 6) {
          String prefix1 = Util.funcPrefix[nodeType[x]];
          if (!branchNames.containsKey(prefix1)) {
            branchNames.put(prefix1, new ArrayList<Integer>());
          }
          branchNames.get(prefix1).add(indexOf(finalParentChain.getNodes(), x));
        }
      }
    }

    // System.out.println("highestfuncnodetype: " + highestFunctionalNodeType);

    for (int i = 0; i < branchPoints.size(); i++) {

      for (int next : adjList.get(branchPoints.get(i))) {
        if (nodeType[next] != highestFunctionalNodeType || highestFunctionalNodeType <= 6) {
          if (contains(finalParentChain.getNodes(), next) == false) {

            // List<List<Integer>> newAdjList = createAdjList(next, (int) branchPoints.get(i));
            



            // List<Integer> usedIndices = newAdjList.get(newAdjList.size() - 1);
            // newAdjList.remove(newAdjList.size() - 1);

            // int[] newNodeTypes = new int[newAdjList.size()];
            // // System.out.println("Used indices: ");
            // // for (int x : usedIndices){
            // // System.out.print(x + " ");
            // // }
            // // System.out.println();
            // // System.out.println("newadjlistsize: " + newAdjList.size());

            // for (int newNode = 1; newNode < newAdjList.size() - 1; newNode++) {
            //   newNodeTypes[newNode] = nodeType[usedIndices.get(newNode - 1)];
            // }

            // int normalizedStart = newAdjList.get(newAdjList.size() - 1).get(0);
            // newAdjList.remove(newAdjList.size() - 1);

            Compound fromBranch = createAdjList(next, (int) branchPoints.get(i));

            String nameOfBranch = fromBranch.getName(false);

            switch (nameOfBranch) {
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

            if (!branchNames.containsKey(nameOfBranch)) {
              branchNames.put(nameOfBranch, new ArrayList<Integer>());
            }
            branchNames.get(nameOfBranch).add(indexOf(finalParentChain.getNodes(), branchPoints.get(i)));
          }
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

    if (highestFunctionalNodeType == 7 && prefixSort.size() != 0){
      name += "-";
    }


    Collections.sort(prefixSort, (s1, s2) -> compareNames(s1, s2));
    int n = 0;
    for (String s : prefixSort) {
      for (int i = 0; i < branchNames.get(s).size(); i++) {

        if (i == branchNames.get(s).size() - 1) {
          name += branchNames.get(s).get(i) + "-" + Util.NUMERICPREFIX[branchNames.get(s).size()];
          if (Character.isDigit(s.charAt(0))) {
            name += "(" + s + ")";
          } else {
            name += s;
          }
          if (n != branchNames.entrySet().size() - 1) {
            name += "-";
          }
        } else {
          name += branchNames.get(s).get(i) + ",";
        }
      }
      n++;
    }

    List<Integer> multiBondIndices = finalParentChain.getMultiBondIndices();
    List<Integer> DBondIndices = finalParentChain.getDBondIndices();
    List<Integer> TBondIndices = finalParentChain.getTBondIndices();

    // System.out.println(multiBondIndices.size());

    if (multiBondIndices.size() == 0){
      if (isPartOfFinalParentChain == true) {
        // System.out.println("parentchainLenght: " + parentChainLength);
        // System.out.println(Util.PREFIX[parentChainLength]);
        name += Util.PREFIX[parentChainLength] + "ane";
      } else {
        name += Util.PREFIX[parentChainLength] + "yl";

      }
    }
    else{
      if (isPartOfFinalParentChain){ 
        // add double bond indiices
        if (DBondIndices.size() > 0){
          if (name.equals("") == false){
            name += "-";
          }
          for (int i = 0; i < DBondIndices.size(); i++){
            name += DBondIndices.get(i);
            name += ",";
          }
          name = name.substring(0, name.length()-1);
          name += "-" + Util.PREFIX[parentChainLength] + Util.NUMERICPREFIX[DBondIndices.size()]+  "ene";
        }
        

        if (TBondIndices.size() > 0){
          if (DBondIndices.size() > 0){
            name = name.substring(0, name.length() - 3 - Util.NUMERICPREFIX[DBondIndices.size()].length());
            name += "en";
          }
          name += "-";
          for (int i = 0; i < TBondIndices.size(); i++){
            name += TBondIndices.get(i);
            name += ",";
          }
          name = name.substring(0, name.length()-1);
          if (DBondIndices.size() == 0){
            name += "-" + Util.PREFIX[parentChainLength] + Util.NUMERICPREFIX[TBondIndices.size()]+ "yne";
          }
          else{
            name += "-" + Util.NUMERICPREFIX[TBondIndices.size()]+ "yne";
          }
        }
      }
      else{
        if (DBondIndices.size() > 0){
          name += "-";
        }
        for (int i = 0; i < DBondIndices.size(); i++){
          name += finalParentChain.getNodes()[DBondIndices.get(i)];
          name += ",";
        }
        name += Util.PREFIX[DBondIndices.size()] + "enyl";

        if (TBondIndices.size() > 0){
          if (DBondIndices.size() > 0){
            name = name.substring(0, name.length() -1);
          }
          name += "-";
        }
        for (int i = 0; i < TBondIndices.size(); i++){
          name += finalParentChain.getNodes()[TBondIndices.get(i)];
          name += ",";
        }
        name += Util.PREFIX[TBondIndices.size()] + "ynyl";
      }
    }
    /*
     * System.out.println(name);
     * String masterBondEdit;
     * //masterBondEdit, String that stores the indices and name of the bonds that
     * //may be present. For example, "1,2 - diene-3-yne"
     * List<Integer> multibondList = finalParentChain.getMultiBondIndices();
     * List<Integer> doubleBondList = new ArrayList<>();
     * List<Integer> tripleBondList = new ArrayList<>();
     * if(multibondList.size()>0) {
     * for(int x : MultiBondList) {
     * if(doubleOrTriple[x]==2) {
     * doubleBondList.add(x);
     * } else {
     * tripleBondList.add(x);
     * }
     * 
     * }
     * }
     * 
     * for(int i = 0; i<doubleBondList.size)
     * 
     */

    if (highestFunctionalNodeType > 7) {
      // pentane
      // pentan + dioic acid
      Collections.sort(highestFuncIndex);
      // ane -> an
      if (highestFunctionalNodeType != 13){
        name = name.substring(0, name.length() - 1);
      }

      if (!(highestFuncIndex.size() == 1
          && (highestFunctionalNodeType == 16 || highestFunctionalNodeType == 14 || highestFunctionalNodeType == 13))) {
        name += "-";
        for (int i = 0; i < highestFuncIndex.size(); i++) {
          if (i != highestFuncIndex.size() - 1) {
            name += highestFuncIndex.get(i) + ",";
          } else {
            name += highestFuncIndex.get(i) + "-";
          }
        }
      }
      name += Util.NUMERICPREFIX[highestFuncIndex.size()] + suffix;

    }
    // we have to put the double bond edit before the functionals hexenoic acid for
    // example
    //

    this.name = name;
    return name;
  }

  // returns -1 if String a is alphabetically first than String b, and +1 if
  // String is alphabetically first. returns 0 if the strings are equal,.
  public int compareNames(String a, String b) {

    if (a.equals(b)) {
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
  // 2,3 - di ethylprop
  // eth

  public Compound createAdjList(int start, int parent) {
    List<List<Integer>> tempAdjList = new ArrayList<List<Integer>>();
    ArrayList<Integer> usedIndices = new ArrayList<>();

    int[][] info = bfsForAdjList(start, parent);

    tempAdjList.add(new ArrayList<Integer>());
    for (int i = 1; i <= N; i++) {
      if (info[0][i] != 0) {
        ArrayList<Integer> connections = new ArrayList<>();
        for (int next : adjList.get(i)) {
          connections.add(next);
          // usedIndices.add(next);
        }
        tempAdjList.add(connections);
        usedIndices.add(i);
      } else if (i == start) {
        ArrayList<Integer> connections = new ArrayList<>();
        for (int next : adjList.get(i)) {
          if (next != parent) {
            connections.add(next);
            // usedIndices.add(next);
          }
        }
        tempAdjList.add(connections);
        usedIndices.add(i);
      }
    }
    
    int[] newNodeTypes = new int[tempAdjList.size()];
    int[][] newBondTypes = new int[tempAdjList.size()][tempAdjList.size()];

    for (int i = 0; i < usedIndices.size(); i++){
      newNodeTypes[i] = nodeType[usedIndices.get(i)];
    }

    for (int i = 0; i < usedIndices.size(); i++){
      for (int j = 0; j < usedIndices.size(); j++){
        newBondTypes[i][j] = bondType[usedIndices.get(i)][usedIndices.get(j)];
      }
    }


    // normalize ints
    Collections.sort(usedIndices);
    Map<Integer, Integer> normalizedIndices = new HashMap<>();
    int normalizedStart = 0;

    int index = 1;
    for (int i : usedIndices) {
      if (normalizedIndices.containsKey(i) == false) {
        if (i == start) {
          normalizedStart = index;
        }
        normalizedIndices.put(i, index);
        index++;
      }
    }
    for (int i = 1; i < tempAdjList.size(); i++) {
      for (int j = 0; j < tempAdjList.get(i).size(); j++) {
        tempAdjList.get(i).set(j, normalizedIndices.get(tempAdjList.get(i).get(j)));
      }
    }
    

    return new Compound(normalizedStart, tempAdjList, newNodeTypes, newBondTypes);
  }

  public int[][] bfsForAdjList(int start, int parent) {
    int[][] info = new int[2][N + 1]; // info[0] stores distances, info[1] stores parents
    Queue<Integer> q = new LinkedList<>();

    q.add(start);
    // System.out.println("NEW BFS\n\n");
    while (q.size() != 0) {
      int curr = q.poll();

      // System.out.println("curr: " + curr);
      // for (int s : q){
      // System.out.print(q + " ");
      // }
      // System.out.println("Queue done");
      for (int i = 0; i < adjList.get(curr).size(); i++) {
        int next = adjList.get(curr).get(i);
        if (info[0][next] == 0 && next != start && next != parent) { // next node is not visited
          info[0][next] = info[0][curr] + 1;
          info[1][next] = curr;
          q.add(next);
        }
      }

    }

    return info;
  }

  public String nameEther(){
    int etherIndex = 0;
    for (int i = 1; i < nodeType.length; i++){
      if (nodeType[i] == 7){
        etherIndex = i;
      }
    }
    
    int parentChainAdjacentNode = 0;
    for (int next : adjList.get(etherIndex)){
      if (contains(finalParentChain.getNodes(), next)){
        parentChainAdjacentNode = next;
      }
    }

    String nameOfBranch = ""; 

    for (int next : adjList.get(etherIndex)){
      if (next != parentChainAdjacentNode){
        // List<List<Integer>> newAdjList = createAdjList(next, etherIndex);
        // List<Integer> usedIndices = newAdjList.get(newAdjList.size() - 1);
        // newAdjList.remove(newAdjList.size() - 1);

        

        // int normalizedStart = newAdjList.get(newAdjList.size() - 1).get(0);
        // newAdjList.remove(newAdjList.size() - 1);

        Compound fromBranch = createAdjList(next, etherIndex);
        nameOfBranch = fromBranch.getName(false);

        switch (nameOfBranch) {
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

        nameOfBranch = nameOfBranch.substring(0, nameOfBranch.length()-2) + "oxy";
      }
    }
    
    return nameOfBranch;

    
    
  }

  public int getConnections(FunctionalNode n) {
    return -1;
  }

  public void findBranches(Chain c) {
    for (int i = 1; i < c.getChainLength() + 1; i++) {

    }
  }

  public Collection<Node> convertIntegersToNodes(Collection<Integer> ints) {
    Collection<Node> c = new ArrayList<>(ints.size());
    for (int i : ints) {
      c.add(nodeList[i]);
    }
    return c;
  }

  public boolean contains(int[] arr, int x) {
    for (int i : arr) {
      if (i == x) {
        return true;
      }
    }
    return false;
  }

  public int indexOf(int[] arr, int x) {
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == x) {
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
    adjList = list;
  }

  public void setNodeList(Node[] list) {
    nodeList = list;
  }

  public void setParentChainLength(int length) {
    parentChainLength = length;
  }

  public int getCounter() {
    return counter;
  }

  public int[] getNodeType() {
    return nodeType;
  }

  public int[][] getBondType(){
  return bondType;
  }

}
