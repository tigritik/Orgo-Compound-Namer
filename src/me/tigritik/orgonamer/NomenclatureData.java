package me.tigritik.orgonamer;

class NomenclatureData {
  private final String[] rootPrefix = { "", "meth", "eth", "prop", "but", "pent", "hex", "hept", "oct", "non", "dec",
      "undec", "dodec" };
  private int parentChainLength;
  private String parentChainName;
  

  NomenclatureData(int parentChainLength) {
    parentChainLength = this.parentChainLength;
    parentChainName = returnPrefix(parentChainLength);
  }

  public String returnPrefix(int parentChainLength){
      return rootPrefix[parentChainLength];
  }
}