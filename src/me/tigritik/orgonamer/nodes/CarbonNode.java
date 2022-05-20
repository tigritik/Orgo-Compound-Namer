package me.tigritik.orgonamer.nodes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CarbonNode extends FunctionalNode {

    private Node n1;
    private Node n2;
    private Node n3;
    private Node n4;

    private String baseName;

    private static final String[] rootPrefix = { "", "meth", "eth", "prop", "but", "pent", "hex", "hept", "oct", "non",
      "dec", "undec", "dodec" };

    public List<Node> getConnections() {
        Set<Node> s = new HashSet<>(4);
        s.add(n1);
        s.add(n2);
        s.add(n3);
        s.add(n4);
        return new ArrayList<>(s);
    }

    public CarbonNode(int numCarbons) {
        
        super(rootPrefix[numCarbons]);
        setBaseName(numCarbons,false);
        
    }



    

    public void setBaseName(int chainLength, boolean inParentChain) {
    // i.e. methyl, methane, propane, propyl, etc
    if (inParentChain) {
      this.baseName = rootPrefix[chainLength] + "ane";
    } else {
      this.baseName = rootPrefix[chainLength] + "yl";
    }

  }

    
}
