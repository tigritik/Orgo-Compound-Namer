package me.tigritik.orgonamer;

import me.tigritik.orgonamer.nodes.FunctionalNode;
import me.tigritik.orgonamer.nodes.Node;

class Branch {
  private static final String[] rootPrefix = { "", "meth", "eth", "prop", "but", "pent", "hex", "hept", "oct", "non",
      "dec", "undec", "dodec" };

  private int parentChainLength;
  private boolean inParentChain;
  private String iupacName;
  

  Branch(int parentChainLength, boolean inParentChain) {
    this.inParentChain=inParentChain;
    this.parentChainLength=parentChainLength;
    iupacName=returnBaseName(parentChainLength,inParentChain);

  }

  Branch(String functionalGroup) {
    iupacName = returnFunctionalGroupName(functionalGroup);
  }

  public String returnBaseName(int chainLength, boolean inParentChain) {
    // i.e. methyl, methane, propane, propyl, etc
    if (inParentChain) {
      return rootPrefix[chainLength] + "ane";
    } else {
      return rootPrefix[chainLength] + "yl";
    }

  }

  public Node returnFunctionalGroupName(String functionalGroup) {
    switch (functionalGroup) {
      case "F":
        return new FunctionalNode(functionalGroup, "fluoro");

      case "NO2":
        return new FunctionalNode(functionalGroup, "nitro");

      case "Br":
        return new FunctionalNode(functionalGroup, "bromo");

      case "N3":
        return new FunctionalNode(functionalGroup, "azido");

      case "I":
        return new FunctionalNode(functionalGroup, "iodo");
      // i added smth to get rid of error idk waht u want to set it as
      default:
        return new FunctionalNode(functionalGroup, "other");
    }
  }
}