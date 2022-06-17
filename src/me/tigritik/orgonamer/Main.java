package me.tigritik.orgonamer;

import me.tigritik.orgonamer.ui.OrgoFrame;

import javax.swing.*;
import java.io.IOException;

public class Main {

  public static void main(String[] args) throws IOException {

     JFrame f = new OrgoFrame();
    // Compound c = new Compound();
    // String name = c.getName(true);
    // System.out.println(name);

    // 1-iodo-2-nitro-2-fluoro-3-azido-4-chloro-5-bromopentane
    // 3-azido-5-bromo-4-chloro-2-fluoro-1-iodo-2-nitropentane
  }

  public static String createMolecule() {
    String name = "";
    try {
      Compound c = new Compound();
      name = c.getName(true);
    } catch (IOException e) {
      System.out.println("Missing input file input.in!");
    } catch (Throwable e) {
      System.out.println("An unknown error occurred!");
      e.printStackTrace();
    }

    return name;

  }

}
