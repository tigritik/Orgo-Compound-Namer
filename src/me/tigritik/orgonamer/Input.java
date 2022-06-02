package me.tigritik.orgonamer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import me.tigritik.orgonamer.nodes.CarbonNode;
import me.tigritik.orgonamer.nodes.Node;

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



  private static final BufferedReader inputReader(String fileName) throws IOException {
    return new BufferedReader(new FileReader(fileName));
  }

  private static final StringTokenizer inputTokenizer() {
    return new StringTokenizer(bf.readLine());
  }

