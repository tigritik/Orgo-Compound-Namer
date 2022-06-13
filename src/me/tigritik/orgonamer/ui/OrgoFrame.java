package me.tigritik.orgonamer.ui;

import javafx.util.Pair;
import me.tigritik.orgonamer.Main;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrgoFrame extends JFrame {

    private final List<Pair<Integer, Integer>> nodeCoordinateList = new ArrayList<>();
    private final List<Pair<Integer, Integer>> connectionList = new ArrayList<>();
    private final ButtonGroup elementSelector = new ButtonGroup();
    private static final int RADIUS = 5;

    private int selectedNode = -1;
    private int nodeCount = 0;

    private final JRadioButton carbon = new JRadioButton("Carbon");
    private final JRadioButton bond1 = new JRadioButton("Single Bond");

    public OrgoFrame() {
        super("OrgoNamer");
        setupFrame();
    }

    private void setupFrame() {
        setSize(1280, 720);
        //setIconImage();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addMouseListener(new ClickListener());
        addKeyListener(new KeyboardListener());
        setVisible(true);
        requestFocus();
        elementSelector.add(carbon);
        elementSelector.add(bond1);
        add(carbon);
        add(bond1);

    }

    private class ClickListener implements MouseInputListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == 1) {
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
                    Pair<Integer, Integer> p = nodeCoordinateList.get(selectedNode);
                    getGraphics().drawLine(p.getKey(), p.getValue(), e.getX(), e.getY());
                    drawNode(nodeCoordinateList.get(selectedNode));
                    connectionList.add(new Pair<>(selectedNode, nodeCoordinateList.size()));
                }
                nodeCoordinateList.add(new Pair<>(e.getX(), e.getY()));
                selectedNode = nodeCoordinateList.size() - 1;
                selectNode(nodeCoordinateList.get(selectedNode));
                nodeCount++;
            }
            if (e.getButton() == 3) {
                if (selectedNode < 0) {
                    return;
                }
                drawNode(nodeCoordinateList.get(selectedNode));
                selectedNode = -1;
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
                    Main.createMolecule();
                    dispose();
                }
                catch (IOException ex) {
                    System.out.println("Cannot save output file!");
                }
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
    }

    private void generateOut() throws IOException {
        BufferedWriter bf = new BufferedWriter(new FileWriter("Input.in"));
        bf.write(nodeCount + "");
        for (Pair<Integer, Integer> connection : connectionList) {
            bf.write("\n" + (connection.getKey()+1) + " " + (connection.getValue()+1));
        }
        bf.close();
    }
}