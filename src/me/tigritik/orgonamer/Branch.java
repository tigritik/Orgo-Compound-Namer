package me.tigritik.orgonamer;

import me.tigritik.orgonamer.nodes.FunctionalNode;
import me.tigritik.orgonamer.nodes.Node;

class Branch {
  

  private int parentChainLength;
  private boolean inParentChain;
  private String iupacName;
  

  Branch(int parentChainLength, boolean inParentChain) {
    this.inParentChain=inParentChain;
    this.parentChainLength=parentChainLength;
    

  }

  

  
  
  
}