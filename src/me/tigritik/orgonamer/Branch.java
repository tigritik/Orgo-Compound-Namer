package me.tigritik.orgonamer;


class Branch {
  

  private int parentChainLength;
  private boolean inParentChain;
  private String iupacName;
  

  Branch(int parentChainLength, boolean inParentChain) {
    this.inParentChain=inParentChain;
    this.parentChainLength=parentChainLength;
    

  }

  

  
  
  
}