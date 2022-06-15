package me.tigritik.orgonamer.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ElementButtons {

    public final static String[] SYMBOLS = {"F", "Cl", "Br", "I", "NO2", "N3"};

    private final JRadioButton[] buttonArray =

    {
        new JRadioButton("Carbon"),
        new JRadioButton("Fluorine"),
        new JRadioButton("Chlorine"),
        new JRadioButton("Bromine"),
        new JRadioButton("Iodine"),
        new JRadioButton("NO2"),
        new JRadioButton("N3")
    };
    private final ButtonGroup group = new ButtonGroup();
    private final List<List<Integer>> nodeListArray = new ArrayList<>(7);

    private int selectedGroup = 0;

    public ElementButtons(JPanel panel) {

        for (AbstractButton b : buttonArray) {
            group.add(b);
            b.setFocusable(false);
            b.addActionListener(new ButtonSelectListener());
            panel.add(b);
            nodeListArray.add(new ArrayList<>());
        }

        resetButtons();
    }

    private class ButtonSelectListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton source = (JRadioButton) e.getSource();
            System.out.println(source.getText());
            switch (source.getText()) {
                case "Carbon":
                    selectedGroup = 0;
                    break;
                case "Fluorine":
                    selectedGroup = 1;
                    break;
                case "Chlorine":
                    selectedGroup = 2;
                    break;
                case "Bromine":
                    selectedGroup = 3;
                    break;
                case "Iodine":
                    selectedGroup = 4;
                    break;
                case "NO2":
                    selectedGroup = 5;
                    break;
                case "N3":
                    selectedGroup = 6;
                    break;
            }
        }
    }

    public void resetButtons() {
        for (AbstractButton b : buttonArray) {
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
    }

    public int getSelectedGroup() {
        return selectedGroup;
    }

    public void addNode(int groupType, int node) {
        nodeListArray.get(groupType).add(node);
    }

    public String getFunctionalOutput() {
        StringBuilder out = new StringBuilder("FUNCTIONAL START");
        for (int i = 1; i < 7; i++) {
            for (int node : nodeListArray.get(i)) {
                out.append("\n").append(node+1).append(" ").append(SYMBOLS[i-1]);
            }
        }

        return out.toString();
    }
}
