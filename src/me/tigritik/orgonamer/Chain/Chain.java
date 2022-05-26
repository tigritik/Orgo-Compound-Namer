package me.tigritik.orgonamer.chain;

import java.util.ArrayList;
import java.util.Collection;

import me.tigritik.orgonamer.Util;
import me.tigritik.orgonamer.nodes.Node;


public class Chain {

    private final int length;
    private final Node[] nodes;
    private String name; 


    public Chain(int length) {
      this.length = length;
      nodes = new Node[length+1];
    }

    public Chain(Collection<Node> nodes) {
        this(nodes.size(), nodes);
    }

    public Chain(int length, Collection<Node> nodes) {
        this(length);
        int i = 1;
        for (Node n: nodes) {
            this.nodes[i] = n;
            i++;
        }
    }
    
    public int getChainLength(){
      return length;
    }

    public Node[] getNodes(){
      return nodes; 
    }

    public boolean branchAt(int index) {
        return (nodes[index].getConnections().size() == 4);
    }

    public String toString(){
      return "prefixes-" + Util.PREFIX[length] + "ane";
    }

    public int compareTo(Chain b){

      ArrayList<Integer> branchPointsThis = new ArrayList<Integer>(); // contains pooints in which it braches off
      ArrayList<Integer> branchPointsB = new ArrayList<Integer>();
      for (int i = 1; i < this.getNodes().length; i++) {
        if (this.branchAt(i)) {
          branchPointsThis.add(i);
        }
      }
      for (int i = 1; i < b.getNodes().length; i++) {
        if (this.branchAt(i)) {
          branchPointsB.add(i);
        }
      }

      int index = 0;

      if(branchPointsThis.size() < branchPointsB.size())
      while (index < branchPointsThis.size() && index < branchPointsB.size()){
        if (branchPointsThis.get(index) < branchPointsB.get(index)){
          return 1;
        }
        else if (branchPointsThis.get(index) > branchPointsB.get(index)){
          return -1;
        }
        else {
          index++;
        }
      }

      if (branchPointsThis.size() == branchPointsB.size()){ // branching points are identical
        index = 0;
        String branchA = "", branchB = "";
        while (index < branchPointsThis.size()){
          for (Node next : nodes[branchPointsThis.get(index)].getConnections()){
            if (next != null && next.getIsPartOfParentChain() == false){
              branchA = nameBranch(next, nodes[branchPointsThis.get(index)]);
            }
          }
          for (Node next : b.getNodes()[branchPointsB.get(index)].getConnections()){
            if (next != null && next.getIsPartOfParentChain() == false){
              branchB = nameBranch(next, nodes[branchPointsB.get(index)]);
            }
          }

          // comparetobranch
          if (compareName(branchA, branchB) > 0){
            return 1;
          }
          else if (compareName(branchA, branchB) < 0){
            return -1;
          }
          else{
            index++;
          }
        }
        // branhc name are also identical
        return 1;
      }
      else{
        if (branchPointsThis.size() > branchPointsB.size()){
          return 1;
        }
        else{
          return -1;
        }
      }



    }

    public String nameBranch(Node start, Node parent) {
      String name = "";

      return name;
      

    }

    public int compareName(String A, String B) {
      
      return 0;
      
    }

}