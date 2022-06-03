package me.tigritik.orgonamer.chain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import me.tigritik.orgonamer.Compound;
import me.tigritik.orgonamer.Util;

public class Chain extends Compound{

  private final List<String> IGNORABLES = Arrays.asList("(",")",",","-","di", "tri", "tetra", "penta", "hexa", "hepta");
  private final int length;
  private int[] nodes; //[null, 1,3,5]
  private String name;

  public Chain(int length) throws IOException{
    this.length = length;
    nodes = new int[length + 1];
  }

  public Chain(Collection<Integer> nodes) throws IOException{
    this(nodes.size(), nodes);
  }

  public Chain(int length, Collection<Integer> nodes)  throws IOException{
    this(length);
    int i = 1;
    for (int n : nodes) {
      this.nodes[i] = n;
      i++;
    }
  }

  
  public boolean branchAt(int index) {
    for (int next : getAdjList().get(nodes[index])) {
      if (contains(nodes, next) == false) {
        return true;
      }
    }
    return false;
  }

 

  public String toString() {
    return "prefixes-" + Util.PREFIX[length] + "ane";
  }

  public int compareTo(Chain b) throws IOException{

    ArrayList<Integer> branchPointsThis = new ArrayList<Integer>(); // contains points in which it branches off
    ArrayList<Integer> branchPointsB = new ArrayList<Integer>();
    


    for (int i = 1; i < nodes.length; i++) {
      //change this 
      if (this.branchAt(i)) {
        branchPointsThis.add(i);
      }
    }
    for (int i = 1; i < b.getNodes().length; i++) {
      if (b.branchAt(i)) {
        branchPointsB.add(i);
      }
    }
    // System.out.println("BranchPointsThis: ");
    // for (int branchNumber : branchPointsThis){
    //   System.out.print(branchNumber + " ");
    // }
    // System.out.println();
    // System.out.println("BranchPointsB:");
    // for (int branchNumber : branchPointsB){
    //   System.out.print(branchNumber + " ");
    // }
    // System.out.println("DONE");
    
    //return 0;
    
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
        for (int next : getAdjList().get(nodes[branchPointsThis.get(index)])) {
          if (contains(nodes, next) == false) {
            Compound branchThisCompound = new Compound(1, createAdjList(next, nodes[branchPointsThis.get(index)]));
            branchThis = branchThisCompound.getName(false);
          }
        }
        for (int next : b.getAdjList().get(b.getNodes()[branchPointsB.get(index)])) {
          if (contains(nodes, next) == false) {
            Compound branchBCompound = new Compound(1, createAdjList(next, b.getNodes()[branchPointsB.get(index)]));
            branchB = branchBCompound.getName(false);
          }
        }

        // comparetobranch
        if (compareName(branchThis, branchB) > 0) {
          return 1;
        } 
        else if (compareName(branchThis, branchB) < 0) {
          return -1;
        } 
        else {
          index++;
        }
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

  public ArrayList<Integer> returnBranchIndices() {
    ArrayList<Integer> indices = new ArrayList<>();

    for(int i = 0; i <nodes.length; i++) {
      if(branchAt(i)) {
        indices.add(nodes[i]);
      }
    }

    return indices;
  }


    
  public ArrayList<Chain> findLongestChain(int start, int parent) throws IOException {

    ArrayList<Chain> possibleParentChains = new ArrayList<>();

    int[][] info = bfs(start, parent); // info[0] stores distances, info[1] stores parents
    for (int i = 1; i < info[0].length; i++) {
      if (info[0][i] + 1 > getParentChainLength()) {
        possibleParentChains.clear();
        setParentChainLength(info[0][i] + 1);
        Chain L = new Chain((findPath(info[1], start, i)));
        possibleParentChains.add(L);
      } else if (info[0][i] + 1 == getParentChainLength()) {
        Chain L = new Chain((findPath(info[1], start, i)));
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

  // TODO
  public int compareName(String A, String B) {

    return 0;

  }

  
public int getChainLength() {
    return length;
  }

  public int[] getNodes() {
    return nodes;
  }

}