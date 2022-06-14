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

public class Chain implements Comparable<Chain> {

  private final List<String> IGNORABLES = Arrays.asList("(",")",",","-","di", "tri", "tetra", "penta", "hexa", "hepta");
  private final int length;
  private int[] nodes; //[null, 1,3,5]
  private String name;
  private Compound parentCompound;

  public Chain(int length, Compound c){
    this.length = length;
    nodes = new int[length + 1];
    parentCompound = c;
  }

  public Chain(Collection<Integer> nodes, Compound c) {
    this(nodes.size(), nodes, c);
  }

  public Chain(int length, Collection<Integer> nodes, Compound c) {
    this(length, c);

    int i = 1;
    for (int n : nodes) {
      this.nodes[i] = n;
      i++;
    }
  }

  public boolean contains(int[] arr, int x){
    for (int i : arr){
      if (i == x){
        return true;
      }
    }
    return false;
  }

  
  public boolean branchAt(int index) {
    //System.out.println("curr " + nodes[index] + ": ");
    for (int next : parentCompound.getAdjList().get(nodes[index])) {
      //System.out.print(next + " ");
      if (contains(nodes, next) == false) {
        //System.out.println("\n\n");
        //System.out.println(next + " ");
        return true;
      }
    }
    //System.out.println("\n");
    return false;
  }

  
 

  public String toString() {
    return "prefixes-" + Util.PREFIX[length] + "ane";
  }

  

  public int compareTo(Chain b){

    
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
        for (int next : parentCompound.getAdjList().get(nodes[branchPointsThis.get(index)])) {
          if (contains(nodes, next) == false) {
            List<List<Integer> > tempAdjList = parentCompound.createAdjList(next, nodes[branchPointsThis.get(index)]);

            
            List<Integer> usedIndices = tempAdjList.get(tempAdjList.size()-1);
            tempAdjList.remove(tempAdjList.size()-1);
            
            int[] newNodeTypes = new int[tempAdjList.size()];


            for (int newNode = 1; newNode < tempAdjList.size()-1; newNode++){
              newNodeTypes[newNode] = parentCompound.getNodeType()[usedIndices.get(newNode-1)];
            }

            int normalizedStart = tempAdjList.get(tempAdjList.size()-1).get(0);
            tempAdjList.remove(tempAdjList.size()-1);

            Compound branchThisCompound = new Compound(normalizedStart, tempAdjList, newNodeTypes);
            branchThis = branchThisCompound.getName(false);
          }
        }
        for (int next : b.getParentCompound().getAdjList().get(b.getNodes()[branchPointsB.get(index)])) {
          if (contains(nodes, next) == false) {
            List<List<Integer> > tempAdjList = b.getParentCompound().createAdjList(next, b.getNodes()[branchPointsB.get(index)]);
            
            List<Integer> usedIndices = tempAdjList.get(tempAdjList.size()-1);
            tempAdjList.remove(tempAdjList.size()-1);
          
            int[] newNodeTypes = new int[tempAdjList.size()];
            for (int newNode = 1; newNode < tempAdjList.size()-1; newNode++){
              newNodeTypes[newNode] = b.getParentCompound().getNodeType()[usedIndices.get(newNode-1)];
            }

            int normalizedStart = tempAdjList.get(tempAdjList.size()-1).get(0);
            tempAdjList.remove(tempAdjList.size()-1);


            Compound branchBCompound = new Compound(normalizedStart, tempAdjList, newNodeTypes);
            branchB = branchBCompound.getName(false);
          }
        }


        // comparetobranch
        if (compareNames(branchThis, branchB) > 0) {
          return 1;
        } 
        else if (compareNames(branchThis, branchB) < 0) {
          return -1;
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

  public ArrayList<Integer> returnBranchIndices() {
    ArrayList<Integer> indices = new ArrayList<>();

    for(int i = 0; i <nodes.length; i++) {
      if(branchAt(i)) {
        indices.add(nodes[i]);
      }
    }

    return indices;
  }


  /*  
  public ArrayList<Chain> findLongestChain(int start, int parent) throws IOException {

    ArrayList<Chain> possibleParentChains = new ArrayList<>();

    int[][] info = bfs(start, parent); // info[0] stores distances, info[1] stores parents
    for (int i = 1; i < info[0].length; i++) {
      if (info[0][i] + 1 > parentCompound.getParentChainLength()) {
        possibleParentChains.clear();
        parentCompound.setParentChainLength(info[0][i] + 1);
        Chain L = new Chain((findPath(info[1], start, i)));
        possibleParentChains.add(L);
      } else if (info[0][i] + 1 == getParentChainLength()) {
        Chain L = new Chain((findPath(info[1], start, i)));
        possibleParentChains.add(L);
      }
    }
    return possibleParentChains;
  }
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

 
}