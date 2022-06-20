package me.tigritik.orgonamer.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElementButtons {

    public final static String[] SYMBOLS = {"O", "N", "F", "Cl", "Br", "I", "NO2", "N3"};
    public static String[] groupNames = {"Carbon", "Oxygen", "Nitrogen", "Fluorine", "Chlorine", "Bromine", "Iodine", "NO2", "N3",
       "Double Bond C", "Triple Bond C", "Double Bond O", "Triple Bond N", "H"};

    private final JRadioButton[] buttonArray =
    {
        new JRadioButton("Carbon"),
        new JRadioButton("Oxygen"),
        new JRadioButton("Nitrogen"),
        new JRadioButton("Fluorine"),
        new JRadioButton("Chlorine"),
        new JRadioButton("Bromine"),
        new JRadioButton("Iodine"),
        new JRadioButton("NO2"),
        new JRadioButton("N3"),
        new JRadioButton("H")
    };
    private final JRadioButton[] multiBonds = {
      new JRadioButton("Double Bond C"),
      new JRadioButton("Triple Bond C"), 
      new JRadioButton("Double Bond O"), 
      new JRadioButton("Triple Bond N")
    };

    private final ButtonGroup group = new ButtonGroup();
    private final List<List<Integer>> nodeListArray = new ArrayList<>(13);

    private int selectedGroup = 0;

    public ElementButtons(JPanel panel) {

        JPanel row1 = new JPanel(new FlowLayout());
        row1.setMaximumSize(new Dimension(2000, 100));
        JPanel row2 = new JPanel(new FlowLayout());


        for (AbstractButton b : buttonArray) {
            group.add(b);
            b.setFocusable(false);
            b.addActionListener(new ButtonSelectListener());
            row1.add(b);
            nodeListArray.add(new ArrayList<>());
        }
        for (AbstractButton b : multiBonds){
            group.add(b);
            b.setFocusable(false);
            b.addActionListener(new ButtonSelectListener());
            row2.add(b);
            nodeListArray.add(new ArrayList<>());
        }

        panel.add(row1);
        panel.add(row2);

        resetButtons();
    }

    private class ButtonSelectListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton source = (JRadioButton) e.getSource();
            selectedGroup = Arrays.asList(groupNames).indexOf(source.getText());
            
        }
    }

    public void resetButtons() {
        for (AbstractButton b : buttonArray) {
            b.setSelected(false);
            b.setEnabled(false);
        }
        for (AbstractButton b : multiBonds){
            b.setSelected(false);
            b.setEnabled(false);
        }

        buttonArray[0].setEnabled(true);
        buttonArray[0].setSelected(true);
        selectedGroup = 0;
    }

    public void enableFunctionalGroups() {
        for (AbstractButton b : buttonArray) {
            b.setEnabled(true);
        }

        for (AbstractButton b: multiBonds){
            b.setEnabled(true);
        }

    }

    public int getSelectedGroup() {
        return selectedGroup;
    }

    public void addNode(int groupType, int node) {
        nodeListArray.get(groupType).add(node);
    }

    public String getFunctionalOutput() {
        StringBuilder out = new StringBuilder("FUNCTIONAL START");
        for (int i = 1; i < 9; i++) {

            for (int node : nodeListArray.get(i)) {
                out.append("\n").append(node+1).append(" ").append(SYMBOLS[i-1]);
            }
        }

        return out.toString();
    }
}
