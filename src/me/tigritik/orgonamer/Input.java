import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import me.tigritik.orgonamer.nodes.CarbonNode;
import me.tigritik.orgonamer.nodes.Node;
import me.tigritik.orgonamer.Main;
import me.tigritik.orgonamer.Compound;

public class Input {

  private final String filePath;
  private final BufferedReader bf;
  private final StringTokenizer st;

  public Input(String path) {
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

  public void fillAdjacencyList(Compound m) throws IOException {

    m.setN(Integer.parseInt(st.nextToken()));
    int N = m.getN();
    m.setAdjList(new ArrayList<>(N));
    m.setNodeList(new Node[N + 1]);

    for (int i = 0; i < N + 1; i++) {
      m.getAdjList.add(new ArrayList<>());
      nodeList[i] = new CarbonNode(i);
    }

    while (bf.ready()) {
      st = new StringTokenizer(bf.readLine());
      int a = Integer.parseInt(st.nextToken()), b = Integer.parseInt(st.nextToken());

      adjList.get(a).add(b);
      adjList.get(b).add(a);
      nodeList[a].addConnection(nodeList[b]);
      nodeList[b].addConnection(nodeList[a]);
    }

    
  }

  private static final BufferedReader inputReader(String fileName) throws IOException {
    return new BufferedReader(new FileReader(fileName));
  }

  private static final StringTokenizer inputTokenizer() {
    return new StringTokenizer(bf.readLine());
  }

}