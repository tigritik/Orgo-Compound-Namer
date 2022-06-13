package me.tigritik.orgonamer;

import me.tigritik.orgonamer.ui.OrgoFrame;

import javax.swing.*;
import java.io.IOException;


public class Main {
  

  public static void main(String[] args) {

    JFrame f = new OrgoFrame();



  }

  public static void createMolecule() {
    try {
      Compound c = new Compound();
      String name = c.getName(true);
      System.out.println(name);
    }
    catch (IOException e) {
      System.out.println("Missing input file input.in!");
    }
    catch (Throwable e) {
      System.out.println("An unknown error occurred!");
    }
  }

}
