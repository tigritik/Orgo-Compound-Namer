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

    private double weight;
    private String iupacName;
    private String name;
    private boolean inParentChain;
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
        
    }

    public FunctionalNode(String iupacName) {
        
        this.iupacName = iupacName;
        inParentChain = false;
        weight = 0;
    }

    public FunctionalNode(String iupacName, boolean inParentChain) {
        //name = "carboxylic acid" or "bromine" or "chlorine"
        //iupacName = "oic - acid" or "bromo" or "chloro"
        
        
        this.iupacName = iupacName;
        this.inParentChain = inParentChain;
        weight = 0;
    }

    public FunctionalNode(String iupacName, boolean inParentChain, double weight) {
        
        this.iupacName = iupacName;
        this.inParentChain = inParentChain;
        this.weight = weight;
    }

    public FunctionalNode(String iupacName, boolean inParentChain, double weight, boolean hasPriority) {
        //boolean hasPriority: carboxylic acid is greater than everything so true, if carboxylic acid exists and ketone exists
        //ketone has lower priority so false. 
        
        this.iupacName = iupacName;
        this.inParentChain = inParentChain;
        this.weight = weight;
        this.hasPriority = hasPriority;
    }

    public String getIUPACName() {
        return iupacName;
    }

    public FunctionalNode returnFunctionalGroupName(String functionalGroup) {
    switch (functionalGroup) {
      case "F":
        return new FunctionalNode("fluoro");

      case "NO2":
        return new FunctionalNode("nitro");

      case "Br":
        return new FunctionalNode("bromo");

      case "N3":
        return new FunctionalNode("azido");

      case "I":
        return new FunctionalNode("iodo");
      // i added smth to get rid of error idk waht u want to set it as
      default:
        return new FunctionalNode("other");
    }
  }

  


}