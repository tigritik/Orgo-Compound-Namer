package me.tigritik.orgonamer.chain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import me.tigritik.orgonamer.Compound;
import me.tigritik.orgonamer.Util;

public class Chain implements Comparable<Chain> {

  private final List<String> IGNORABLES = Arrays.asList("(", ")", ",", "-", "di", "tri", "tetra", "penta", "hexa",
      "hepta");
  private final int length;
  private int[] nodes; // [null, 1,3,5]
  private String name;
  private Compound parentCompound;
  private ArrayList<Integer> MultiBondIndices = new ArrayList<>(); // goes by index iwthin chian not compound
  private ArrayList<Integer> DBondIndices = new ArrayList<>(); // goes by index iwthin chian not compound
  private ArrayList<Integer> TBondIndices = new ArrayList<>(); // goes by index iwthin chian not compound


  public Chain(int length, Compound c) {
    this.length = length;
    nodes = new int[length + 1];
    parentCompound = c;

  }

  public Chain(Collection<Integer> nodes, Compound c) {
    this(nodes.size(), nodes, c);
  }

  public Chain(int length, Collection<Integer> nodesTemp, Compound c) {
    this(length, c);

    int i = 1;
    for (int n : nodesTemp) {
      this.nodes[i] = n;
      i++;
    }

    // make a getter lemme make the thing first
    // so smth like
    // ur looping through the multi array arent u ok
    // ill assume this work
    // ill make a getter asap
    // im looping thru the chain nodes and checking like if currNode and currNOde +
    // 1 form a double bond in the parentCompouund
    for (int x = 0; x < nodes.length-1; x++){
    // is this not how u access an array why is there an error
      if (parentCompound.getBondType()[nodes[x]][nodes[x+1]] == 2){
        MultiBondIndices.add(x);
        DBondIndices.add(x);
      }
      if (parentCompound.getBondType()[nodes[x]][nodes[x+1]] == 3){
        MultiBondIndices.add(x);
        TBondIndices.add(x);
      }
    }

  }

  public boolean contains(int[] arr, int x) {
    for (int i : arr) {
      if (i == x) {
        return true;
      }
    }
    return false;
  }

  public boolean branchAt(int index) {
    // System.out.println("curr " + nodes[index] + ": ");
    for (int next : parentCompound.getAdjList().get(nodes[index])) {
      // System.out.print(next + " ");
      if (contains(nodes, next) == false) {
        // System.out.println("\n\n");
        // System.out.println(next + " ");
        return true;
      }
    }
    // System.out.println("\n");
    return false;
  }

  public String toString() {
    return "prefixes-" + Util.PREFIX[length] + "ane";
  }

  public int compareTo(Chain b){

    // func groups
    
    int counterBestFuncGroupThis = 0;
    int bestFuncGroupThis = 0;
    int counterBestFuncGroupB = 0;
    int bestFuncGroupB = 0;

    


    for (int i = 1; i < nodes.length; i++){
      if (parentCompound.getNodeType()[nodes[i]] == bestFuncGroupThis){
        counterBestFuncGroupThis++;
      }
      else if (parentCompound.getNodeType()[nodes[i]] > bestFuncGroupThis){
        bestFuncGroupThis = parentCompound.getNodeType()[nodes[i]];
        counterBestFuncGroupThis = 1;
      }
    }

    for (int i = 1; i < b.getNodes().length; i++){
      if (b.getParentCompound().getNodeType()[b.getNodes()[i]] == bestFuncGroupB){
        counterBestFuncGroupB++;
      }
      else if (b.getParentCompound().getNodeType()[b.getNodes()[i]] > bestFuncGroupB){
        bestFuncGroupB = b.getParentCompound().getNodeType()[b.getNodes()[i]];
        counterBestFuncGroupB = 1;
      }
    }

    
    if (!(bestFuncGroupB <= 6 && bestFuncGroupThis <= 6)){
      if (bestFuncGroupThis > bestFuncGroupB){
        return 1;
      }
      else if (bestFuncGroupB > bestFuncGroupThis){
        return -1;
      }
      else {
        if(counterBestFuncGroupThis > counterBestFuncGroupB){
          return 1;
        }
        else if(counterBestFuncGroupB > counterBestFuncGroupThis){
          return -1;
        }
      }
    }

    /// length
    // System.out.println("thisLength: " + this.getChainLength() + " bLength: " + b.getChainLength());
    if (this.getChainLength() > b.getChainLength()){
      return 1;
    }
    else if (this.getChainLength() < b.getChainLength()){
      return -1; 
    }

   // number of multibonds

    if (this.getMultiBondIndices().size() > b.getMultiBondIndices().size()){
      return 1;
    }
    else if (this.getMultiBondIndices().size() < b.getMultiBondIndices().size()){
      return -1;
    }
    else{
      if (this.getDBondIndices().size() > b.getDBondIndices().size()){
        return 1;
      }
      else if (this.getDBondIndices().size() < b.getDBondIndices().size()){
        return -1;
      }
      else if (this.getTBondIndices().size() > b.getTBondIndices().size()){
        return 1;
      }
      else if (this.getTBondIndices().size() < b.getTBondIndices().size()){
        return -1;
      }
    }


  
    // locants of suffixes
    
    ArrayList<Integer> suffixPointsThis = new ArrayList<Integer>(); 
    ArrayList<Integer> suffixPointsB = new ArrayList<Integer>();
    
    for (int i = 1; i < nodes.length; i++) {
      if (parentCompound.getNodeType()[nodes[i]] == bestFuncGroupThis) {
        suffixPointsThis.add(i);
      }
    }
    for (int i = 1; i < b.getNodes().length; i++) {
      if (parentCompound.getNodeType()[b.getNodes()[i]] == bestFuncGroupB) {
        suffixPointsB.add(i);
      }
    }


    if (suffixPointsThis.size() > 0 && suffixPointsB.size() == 0){
      return 1;
    }
    else if (suffixPointsThis.size() == 0 && suffixPointsB.size() > 0){
      return -1;
    }
    else if (suffixPointsThis.size() > suffixPointsB.size()){
      return 1;
    }
    else if (suffixPointsB.size() > suffixPointsThis.size()){
      return -1;
    }
    else if (suffixPointsThis.size() > 0 && suffixPointsB.size() > 0){
      for (int i = 0; i < suffixPointsThis.size(); i++){
        if (suffixPointsThis.get(i) > suffixPointsB.get(i)){
          return -1;
        }
        else if (suffixPointsB.get(i) > suffixPointsThis.get(i)){
          return 1;
        }
      }
    }


   // locants for multibonds

    int multiBondIndex = 0;
    while (multiBondIndex < this.getMultiBondIndices().size()){
      if (this.getMultiBondIndices().get(multiBondIndex) > b.getMultiBondIndices().get(multiBondIndex)){
        return -1;
      }
      else if (this.getMultiBondIndices().get(multiBondIndex) < b.getMultiBondIndices().get(multiBondIndex)){
        return 1;
      }
      else{
        multiBondIndex++;
      }
    }





    // branching points

    ArrayList<Integer> branchPointsThis = new ArrayList<Integer>(); // contains points in which it branches off
    ArrayList<Integer> branchPointsB = new ArrayList<Integer>();
    

    


    for (int i = 1; i < nodes.length; i++) {
      if (this.branchAt(i)) {
        branchPointsThis.add(i);
      }
    }
    for (int i = 1; i < b.getNodes().length; i++) {
      if (b.branchAt(i)) {
        branchPointsB.add(i);
      }
    }

    

    int index = 0;


    while (index < branchPointsThis.size() && index < branchPointsB.size()) {
      if (branchPointsThis.get(index) < branchPointsB.get(index)) {
        return 1;
      } 
      else if (branchPointsThis.get(index) > branchPointsB.get(index)) {
        return -1;
      } 
      else {
        index++;
      }
    }



    if (branchPointsThis.size() == branchPointsB.size()) { // branching points are identical
      index = 0;
      
      String branchThis = "", branchB = "";
      while (index < branchPointsThis.size()) {
        ArrayList<String> branchThisNames = new ArrayList<>(); 
        ArrayList<String> branchBNames = new ArrayList<>(); 

        for (int next : parentCompound.getAdjList().get(nodes[branchPointsThis.get(index)])) {
          if (contains(nodes, next) == false && parentCompound.getNodeType()[next] != 7 ) {
            // List<List<Integer> > tempAdjList = parentCompound.createAdjList(next, nodes[branchPointsThis.get(index)]);

            
            // List<Integer> usedIndices = tempAdjList.get(tempAdjList.size()-1);
            // tempAdjList.remove(tempAdjList.size()-1);
            
            // int[] newNodeTypes = new int[tempAdjList.size()];


            // for (int newNode = 1; newNode < tempAdjList.size()-1; newNode++){
            //   newNodeTypes[newNode] = parentCompound.getNodeType()[usedIndices.get(newNode-1)];
            // }

            // int normalizedStart = tempAdjList.get(tempAdjList.size()-1).get(0);
            // tempAdjList.remove(tempAdjList.size()-1);

            Compound branchThisCompound = parentCompound.createAdjList(next, nodes[branchPointsThis.get(index)]);
            branchThis = branchThisCompound.getName(false);

            switch (branchThis) {
              case "1-methylethyl":
                branchThis = "isopropyl";
                break;
              case "1-methylpropyl":
                branchThis = "sec-butyl";
                break;
              case "2-methylpropyl":
                branchThis = "isobutyl";
                break;
              case "1,1-dimethylethyl":
                branchThis = "tert-butyl";
                break;
            }

            branchThisNames.add(branchThis);
          }
        }
        for (int next : b.getParentCompound().getAdjList().get(b.getNodes()[branchPointsB.get(index)])) {

          if (contains(nodes, next) == false && parentCompound.getNodeType()[next] != 7) {
            // List<List<Integer> > tempAdjList = b.getParentCompound().createAdjList(next, b.getNodes()[branchPointsB.get(index)]);
            
            // List<Integer> usedIndices = tempAdjList.get(tempAdjList.size()-1);
            // tempAdjList.remove(tempAdjList.size()-1);
          
            // int[] newNodeTypes = new int[tempAdjList.size()];
            // for (int newNode = 1; newNode < tempAdjList.size()-1; newNode++){
            //   newNodeTypes[newNode] = b.getParentCompound().getNodeType()[usedIndices.get(newNode-1)];
            // }

            // int normalizedStart = tempAdjList.get(tempAdjList.size()-1).get(0);
            // tempAdjList.remove(tempAdjList.size()-1);


            Compound branchBCompound =  b.getParentCompound().createAdjList(next, b.getNodes()[branchPointsB.get(index)]);
            branchB = branchBCompound.getName(false);

            switch (branchB) {
              case "1-methylethyl":
                branchB = "isopropyl";
                break;
              case "1-methylpropyl":
                branchB = "sec-butyl";
                break;
              case "2-methylpropyl":
                branchB = "isobutyl";
                break;
              case "1,1-dimethylethyl":
                branchB = "tert-butyl";
                break;
            }

            branchBNames.add(branchB);
          }
        }

        Collections.sort(branchThisNames);
        Collections.sort(branchBNames);


        // System.out.println("branchpointsthis: " + branchPointsThis.get(index) + " branchPointsB" + branchPointsB.get(index));
        // System.out.println("branchThisNames: " + branchThisNames);
        // System.out.println("branchThisB: " + branchBNames);
        if (branchThisNames.size() == 0 && branchBNames.size() == 0){
          return 1;
        }
        // comparetobranch
        if (compareNames(branchThisNames.get(0), branchBNames.get(0)) > 0) {
          return -1;
        } 
        else if (compareNames(branchThisNames.get(0), branchBNames.get(0)) < 0) {
          return 1;
        } 
        else {
          if (branchThisNames.size() == 2 && branchBNames.size() == 1){
            return 1;
          }
          else if (branchThisNames.size() == 1 && branchBNames.size() == 2){
            return -1;
          }
          else if (branchThisNames.size() == 2 && branchBNames.size() == 2){
            return -1 * compareNames(branchThisNames.get(1), branchBNames.get(1));
          }
        }
        index++;
        
      }
      // branhc name are also identical
      return 1;
    } 
    else {
      if (branchPointsThis.size() > branchPointsB.size()) {
        return 1;
      } 
      else {
        return -1;
      }
    }
    
  }

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

  public ArrayList<Integer> returnBranchIndices() {
    ArrayList<Integer> indices = new ArrayList<>();

    for (int i = 0; i < nodes.length; i++) {
      if (branchAt(i)) {
        indices.add(nodes[i]);
      }
    }

    return indices;
  }

  /*
   * public ArrayList<Chain> findLongestChain(int start, int parent) throws
   * IOException {
   * 
   * ArrayList<Chain> possibleParentChains = new ArrayList<>();
   * 
   * int[][] info = bfs(start, parent); // info[0] stores distances, info[1]
   * stores parents
   * for (int i = 1; i < info[0].length; i++) {
   * if (info[0][i] + 1 > parentCompound.getParentChainLength()) {
   * possibleParentChains.clear();
   * parentCompound.setParentChainLength(info[0][i] + 1);
   * Chain L = new Chain((findPath(info[1], start, i)));
   * possibleParentChains.add(L);
   * } else if (info[0][i] + 1 == getParentChainLength()) {
   * Chain L = new Chain((findPath(info[1], start, i)));
   * possibleParentChains.add(L);
   * }
   * }
   * return possibleParentChains;
   * }
   */

  public int[][] bfs(int start, int parent) {
    int[][] info = new int[2][parentCompound.getN() + 1]; // info[0] stores distances, info[1] stores parents
    Queue<Integer> q = new LinkedList<>();

    q.add(start);

    while (q.size() != 0) {
      int curr = q.poll();
      for (int i = 0; i < parentCompound.getAdjList().get(curr).size(); i++) {
        int next = parentCompound.getAdjList().get(curr).get(i);
        if (info[0][next] == 0 && next != start && next != parent) { // next node is not visited
          info[0][next] = info[0][curr] + 1;
          info[1][next] = curr;
          q.add(next);
        }
      }

    }

    return info;
  }

  public Compound getParentCompound() {
    return parentCompound;
  }

  public int getChainLength() {
    return length;
  }

  public int[] getNodes() {
    return nodes;
  }

  public List<Integer> getMultiBondIndices(){
    return MultiBondIndices;
  }
  public List<Integer> getDBondIndices(){
    return DBondIndices;
  }
  public List<Integer> getTBondIndices(){
    return TBondIndices;
  }

}