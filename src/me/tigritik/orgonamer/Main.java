package me.tigritik.orgonamer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import me.tigritik.orgonamer.ui.OrgoFrame;

public class Main {
 
  // TESTCASES : do you want testcases to test?

  private static List<String> correctCases = new ArrayList<>();
  private static List<String> testCaseNames = new ArrayList<>();
  private static List<String> testCases = new ArrayList<>(correctCases.size());

  // numTestCases : curr number of test cases
  // checkMatch : does the output match the correct output?
  // print : print the name of the organic molecule
   private static final int numTestCases = 24;
  private static final boolean checkMatch = true;
  private static final boolean PRINT = false;
  private static final int CASENUMBER = -1;


  private static final boolean TESTCASES = true;

  public static void main(String[] args) throws IOException {

    JFrame f = new OrgoFrame();

    // if (TESTCASES) {
    //   BufferedReader br = new BufferedReader(new FileReader("tcas/TestCaseCheck.txt"));
    //   BufferedReader nameReader = new BufferedReader(new FileReader("tcas/TestCaseName.txt"));
    //   while (br.ready()) {
    //     correctCases.add(br.readLine());
    //   }
    //   br.close();
     
    //   while (nameReader.ready()) {
    //     testCaseNames.add(nameReader.readLine());
    //   }
    //   nameReader.close();
    // }
    // if (CASENUMBER >= 0) {
    //   testCase("tcas/TCase" + CASENUMBER + ".in", checkMatch, PRINT);
    // }

    // if (CASENUMBER < 0) {
    //   for (int i = 0; i <= numTestCases; i++) {

    //     testCase("tcas/TCase" + i + ".in", checkMatch, PRINT);
    //     System.out.println("\n");
    //   }
    // }

    // Compound c = new Compound("Inputs/InputMain.in", false);
    // System.out.println(c.getName(true));

  }

  public static String createMolecule(String fileName) {
    String name = "";
    try {
      Compound c = new Compound(fileName, true);
      name = c.getName(true);
    } catch (IOException e) {
      System.out.println("Missing input file input.in!");
    } catch (Throwable e) {
      System.out.println("An unknown error occurred!");
      e.printStackTrace();
    }

    return name;

  }

  public static void testCase(String fileName, boolean checkMatch, boolean printTrue) throws IOException {
    Compound temp = new Compound(fileName, false);
    String s = temp.getName(true);

    if (printTrue) {
      System.out.println(s);
    }

    if (checkMatch) {
      int indexEnd = fileName.indexOf('.');
      int indexStart = fileName.indexOf('e');
      int listIndex = Integer.parseInt(fileName.substring(indexStart + 1, indexEnd));
      testCases.add(s);
      if (testCases.get(listIndex).equals(correctCases.get(listIndex))) {
        System.out.println("Test case " + listIndex + "(" + testCaseNames.get(listIndex) + ")" + " successful.");
      } else {
        System.out.println("Test case " + listIndex + "(" + testCaseNames.get(listIndex) + ")" + " was unsuccessful");
        System.out.println("Your input returned : " + testCases.get(listIndex));
        System.out.println("The correct output is: " + correctCases.get(listIndex));
      }
    }

    if (!(testCases.contains(s))) {
      testCases.add(s);
    }
  }

}
