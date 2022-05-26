package me.tigritik.orgonamer.nodes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FunctionalNode extends Node {

    

    private Node n1;
    private Node n2;
    private Node n3;
    private Node n4;

    private String iupacName;
    private String name;
    private boolean isPartOfParentChain;
    private boolean hasPriority;

    public List<Node> getConnections() {
        
        Set<Node> s = new HashSet<>(4);
        s.add(n1);
        s.add(n2);
        s.add(n3);
        s.add(n4);
        return new ArrayList<>(s);
    }


    public FunctionalNode() {

        super(4);
    }

    
    public FunctionalNode(String iupacName) {
      super(4);
      this.iupacName = iupacName;
    }

    public FunctionalNode(String iupacName, boolean isPartOfParentChain) {
        //name = "carboxylic acid" or "bromine" or "chlorine"
        //iupacName = "oic - acid" or "bromo" or "chloro"


        super(4);
        this.iupacName = returnFunctionalGroupName(iupacName);
        this.isPartOfParentChain = isPartOfParentChain;
        
    }

    public FunctionalNode(boolean isPartOfParentChain) {
      super(4);
      this.isPartOfParentChain = isPartOfParentChain;
    }

    public String getIUPACName(){
      return iupacName;
    }
    
    public boolean getIsPartOfParentChain() {
      return isPartOfParentChain;
    }

    public void setIsPartOfParentChain(boolean bn) {
      isPartOfParentChain = bn; 
    }

@Override
    public void addConnection(Node n) {
        if (n1 == null) {
            n1 = n;
            return;
        }
        if (n2 == null) {
            n2 = n;
            return;

        }
        if (n3 == null) {
            n3 = n;
            return;
        }
        if (n4 == null) {
            n4 = n;
            return;
        }
       
    }
    

    

    

    public String returnFunctionalGroupName(String functionalGroup) {
    switch (functionalGroup) {
      case "F":
        return "fluoro";

      case "NO2":
        return "nitro";

      case "Br":
        return "bromo";

      case "N3":
        return "azido";

      case "I":
        return "iodo";
      // i added smth to get rid of error idk waht u want to set it as
      default:
        return "";
    }
  }

  


}