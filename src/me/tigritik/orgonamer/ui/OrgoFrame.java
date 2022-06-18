package me.tigritik.orgonamer.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.MouseInputListener;

import javafx.util.Pair;
import me.tigritik.orgonamer.Main;

public class OrgoFrame extends JFrame {

    private final List<Pair<Integer, Integer>> nodeCoordinateList = new ArrayList<>();
    private final List<Pair<Integer, Integer>> connectionList = new ArrayList<>();
    private final JPanel buttonPanel = new JPanel();
    private final ElementButtons buttons = new ElementButtons(buttonPanel);
    private static final int RADIUS = 10;


    private int selectedNode = -1;
    private int nodeCount = 0;

    public OrgoFrame() {
        super("OrgoNamer");
        setupFrame();
    }

    private void setupFrame() {
        setSize(1920, 1000);
        //setIconImage();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addMouseListener(new ClickListener());
        addKeyListener(new KeyboardListener());
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        add(buttonPanel);
        setVisible(true);
        requestFocus();
    }

    private class ClickListener implements MouseInputListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == 1) {
                int nodeType = buttons.getSelectedGroup();
                int node = checkClick(e.getX(), e.getY());
                if (node >= 0) {
                    selectNode(nodeCoordinateList.get(node));
                    if (selectedNode >= 0) {
                        drawNode(nodeCoordinateList.get(selectedNode));
                    }
                    selectedNode = node;
                    return;
                }
                if (selectedNode >= 0) {
                    int nodeType = buttons.getSelectedGroup();
                    Pair<Integer, Integer> p = nodeCoordinateList.get(selectedNode);

                    if (nodeType == 15){
                      Graphics g = getGraphics();
                      Graphics2D g2d = (Graphics2D) g;
                      g2d.setStroke(new BasicStroke(4.0F));
                      g2d.drawLine(p.getKey(), p.getValue(), e.getX(), e.getY());
                      connectionList.add(new Pair<>(selectedNode, nodeCount)); 
                      connectionList.add(new Pair<>(selectedNode, nodeCount));
                    }
                    else if (nodeType == 16){
                      Graphics g = getGraphics();
                      Graphics2D g2d = (Graphics2D) g;
                      g2d.setStroke(new BasicStroke(10.0F));
                      g2d.drawLine(p.getKey(), p.getValue(), e.getX(), e.getY());
                      connectionList.add(new Pair<>(selectedNode, nodeCount)); 
                      connectionList.add(new Pair<>(selectedNode, nodeCount)); 
                      connectionList.add(new Pair<>(selectedNode, nodeCount)); 
                    }
                    else{
                      getGraphics().drawLine(p.getKey(), p.getValue(), e.getX(), e.getY());
                      connectionList.add(new Pair<>(selectedNode, nodeCount));
                    }

                    // System.out.println("selected node: " + selectedNode + " nnodeCoordianteListsize: " + nodeCount);
                    if (nodeType == 0 || nodeType == 15 || nodeType == 16) {
                      drawNode(nodeCoordinateList.get(selectedNode));
                    }
                    else if (nodeType <= 6){

                        Graphics g = getGraphics();
                        g.setColor(Color.red);
                        g.setFont(new Font("Times New Roman", Font.BOLD, 20));
                        g.drawString(ElementButtons.SYMBOLS[nodeType-1], e.getX(), e.getY());
                        nodeCoordinateList.add(new Pair<>(e.getX(), e.getY()));
                        buttons.addNode(nodeType, nodeCount);
                        nodeCount++;
                        //deselectNode();
                        return;
                    }
                    else if (nodeType <= 14){
                      Graphics g = getGraphics();
                      g.setColor(Color.red);
                      g.setFont(new Font("Times New Roman", Font.BOLD, 20));
                      g.drawString(ElementButtons.SYMBOLS[nodeType-1], e.getX() + 10, e.getY() - 10);
                      drawNode(nodeCoordinateList.get(selectedNode));
                      buttons.addNode(nodeType, nodeCount);
                    }

                }
                nodeCoordinateList.add(new Pair<>(e.getX(), e.getY()));
                selectedNode = nodeCoordinateList.size() - 1;
                selectNode(nodeCoordinateList.get(selectedNode));
                nodeCount++;
            }
            if (e.getButton() == 3) {
                deselectNode();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    private class KeyboardListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                try {
                  generateOut();
                  String name = Main.createMolecule();
                  Graphics g = getGraphics();      
                  Font font = new Font("Times New Roman", Font.BOLD, 30);
                  g.setFont(font);
                  FontMetrics metrics = g.getFontMetrics(font);

                  g.drawString("Name: " + name, 900 - metrics.stringWidth(name)/2, 850);

                  // drawCenteredString(g, "Name: " + name);
                  //dispose();
                }
                catch (IOException ex) {
                    System.out.println("Cannot save output file!");
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_R){
              dispose();
              JFrame f = new OrgoFrame();


            }
        }

      

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }



    private int checkClick(int x, int y) {
        int i = 0;

        for (Pair<Integer, Integer> p : nodeCoordinateList) {
            if (Math.abs(x - p.getKey()) <= RADIUS && Math.abs(y-p.getValue()) <= RADIUS) {
                return i;
            }
            i++;
        }

        return -1;
    }

    private void drawNode(Pair<Integer, Integer> node) {
        getGraphics().fillOval(node.getKey()-RADIUS, node.getValue()-RADIUS, 2*RADIUS, 2*RADIUS);
    }

    private void selectNode(Pair<Integer, Integer> node) {
        Graphics g = getGraphics();
        g.setColor(Color.YELLOW);
        g.fillOval(node.getKey()-RADIUS, node.getValue()-RADIUS, 2*RADIUS, 2*RADIUS);
        buttons.enableFunctionalGroups();
    }

    private void deselectNode() {
        if (selectedNode < 0) {
            return;
        }
        drawNode(nodeCoordinateList.get(selectedNode));
        selectedNode = -1;
        buttons.resetButtons();
    }

    private void generateOut() throws IOException {
        Writer bf = new BufferedWriter(new FileWriter("Input.in"));
        bf.write(nodeCount + "");
        for (Pair<Integer, Integer> connection : connectionList) {
            bf.write("\n" + (connection.getKey()+1) + " " + (connection.getValue()+1));
        }
        bf.write("\n" + buttons.getFunctionalOutput());
        bf.close();
    }
}