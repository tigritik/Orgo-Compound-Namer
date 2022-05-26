package me.tigritik.orgonamer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import me.tigritik.orgonamer.chain.Chain;
import me.tigritik.orgonamer.nodes.FunctionalNode;
import me.tigritik.orgonamer.nodes.Node;

public class Main {
  // Nodes are 1-indexed
  

  public static void main(String[] args) throws IOException {

    Compound c = new Compound();

    // reads in inputs and fills out (adjList)
    fillAdjacencyList(c);

    
    ArrayList<Chain> possibleParentChains = findLongestChain();

    for (Chain c : possibleParentChains){
      for (Node n : c.getNodes()){
        System.out.print(n + " ");
      }
      System.out.println();
    }

    for (Chain c : possibleParentChains) {
      for (int i = 1; i < c.getNodes().length; i++) {
        if (c.branchAt(i)) {
          System.out.print(i + "-" + nodeList[i].getConnections() + "-");
        }
      }
      System.out.println(c);
    }



  }

  

  
  

  
}
