package me.tigritik.orgonamer.chain;

import java.io.IOException;



import me.tigritik.orgonamer.Util;


public class CarbonChain extends Chain {

private boolean isPartOfParentChain;
private int numCarbons;
private String name;


  public CarbonChain(int length, boolean isPartOfParentChain) throws IOException {
    super(length);
    numCarbons = length;
    this.isPartOfParentChain = isPartOfParentChain;
    name = getName();
  }

  public String getName() {
    if (isPartOfParentChain) {
        return Util.PREFIX[numCarbons] + "ane";
    }

    else {
        return Util.PREFIX[numCarbons] + "yl";
    }
  }
}