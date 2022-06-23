package me.tigritik.orgonamer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {

  private static List<String> testCases = new ArrayList<>();
  private static List<String> correctCases = new ArrayList<>();
  private static final boolean PRINT=true;
  private static final int numTestCases = 1;
  private static final boolean checkMatch = true;
  
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader("Inputs/correctTestCaseName.txt"));
    while(br.ready()) {
      correctCases.add(br.readLine());
    }
    br.close();

    // JFrame f = new OrgoFrame();
   
    for (int i = 0; i < numTestCases; i++) {
      testCase("Input/TestCase" + i + ".in", checkMatch, PRINT);
    }


    Compound c = new Compound("Inputs/Inputmain.in");
    String name = c.getName(true);
    System.out.println(name);

    // 1-iodo-2-nitro-2-fluoro-3-azido-4-chloro-5-bromopentane
    // 3-azido-5-bromo-4-chloro-2-fluoro-1-iodo-2-nitropentane
  }

  public static String createMolecule(String fileName) {
    String name = "";
    try {
      Compound c = new Compound(fileName);
      name = c.getName(true);
    } catch (IOException e) {
      System.out.println("Missing input file input.in!");
    } catch (Throwable e) {
      System.out.println("An unknown error occurred!");
      e.printStackTrace();
    }

    return name;

  }

  public static void testCase(String fileName, boolean checkMatch, boolean printTrue) throws IOException{
      Compound temp = new Compound(fileName);
      String s = temp.getName(true);

      if(printTrue) {
        System.out.println(s);
      }

      if(checkMatch) {
        System.out.println("yes");
      }

      testCases.add(s);

      
  }

}
