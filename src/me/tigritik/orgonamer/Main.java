package me.tigritik.orgonamer;

import java.io.IOException;


public class Main {
  

  public static void main(String[] args) throws IOException {

    Compound c = new Compound();

    String name = c.getName(true);
    System.out.println(name);
    

  }

}
