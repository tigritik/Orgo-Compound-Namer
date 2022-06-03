package me.tigritik.orgonamer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.StringTokenizer;


public class Input {

  private final String filePath;
  private final BufferedReader bf;
  private final StringTokenizer st;

  public Input(String path) throws IOException{
    filePath = path;
    bf = inputReader(path);
    st = new StringTokenizer(bf.readLine());
  }

  public BufferedReader getBufferedReader() {
    return bf;
  }

  public StringTokenizer getStringTokenizer() {
    return st;
  }

  public String getFilePath() {
    return filePath;
  }



  private final BufferedReader inputReader(String fileName) throws IOException {
    return new BufferedReader(new FileReader(fileName));
  }

  private final StringTokenizer inputTokenizer() throws IOException {
    return new StringTokenizer(bf.readLine());
  }

}
