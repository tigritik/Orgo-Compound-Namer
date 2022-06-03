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
  

  public static void main(String[] args) throws IOException {

    Compound c = new Compound();

    // reads in inputs and fills out (adjList)
    c.fillAdjacencyList(c);

    
    ArrayList<Chain> possibleParentChains = c.findLongestChain();
    /* 
    for (Chain chain : possibleParentChains){
      for (int n : chain.getNodes()){
        System.out.print(n + " ");
      }
      System.out.println();
    }

    for (Chain chain : possibleParentChains) {
      for (int i = 1; i < chain.getNodes().length; i++) {
        if (chain.branchAt(i)) {
          System.out.print(i + "-" + c.getNodeList()[i].getConnections() + "-");
        } 
      }
      System.out.println(c);
    }*/

    c.findFinalParentChain(possibleParentChains);
    String name = c.getName();
    System.out.println(name);



  }

  

  
  

  
}
