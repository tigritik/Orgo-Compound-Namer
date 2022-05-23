<<<<<<< Updated upstream
package me.tigritik.orgonamer.Chain;

import java.util.Collection;

import me.tigritik.orgonamer.nodes.Node;

=======
package me.tigritik.orgonamer.chain;

import java.util.ArrayList;
import java.util.Collection;

import me.tigritik.orgonamer.Util;
import me.tigritik.orgonamer.nodes.Node;

>>>>>>> Stashed changes
public class Chain {

    private final int length;
    //private final Node[] nodes;
    private final int[] nodes;

    public Chain(int length) {
      this.length = length;
      nodes = new int[length];
    }

    public Chain(Collection<Integer> nodes) {
        this(nodes.size(), nodes);
    }

    public Chain(int length, Collection<Integer> nodes) {
        this(length);
        int i = 0;
        for (int n: nodes) {
            this.nodes[i] = n;
            i++;
        }
    }
    
    public int getChainLength(Chain c){
      return c.length; 
    }

    public int[] getNode(){
      return nodes; 
    }

    public String toString(){
<<<<<<< Updated upstream
      return "";
=======
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
        //return name(this).compareTo(name(b));
        // name first branch and find alphabetcally first one

        //diethyl 
        //chloroethylmethyl
      }
      else{
        if (branchPointsThis.size() > branchPointsB.size()){
          return 1;
        }
        else{
          return -1;
        }
      }


>>>>>>> Stashed changes

    }
    
    
    

    

}