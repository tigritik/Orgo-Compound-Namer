package me.tigritik.orgonamer.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ElementButtons {

    public final static String[] SYMBOLS = {"F", "Cl", "Br", "I", "NO2", "N3", "COOH", "CONH2", "CN", "COH", "O", "OH", "NH2", "ETH"};

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
    private final JRadioButton[] higherGroupButtons =
    {
        new JRadioButton("Carboxylic Acid"),
        new JRadioButton("Amide"),
        new JRadioButton("Nitrile"),
        new JRadioButton("Aldehyde"),
        new JRadioButton("Ketone"),
        new JRadioButton("Alcohol"),
        new JRadioButton("Amine"),
        new JRadioButton("Ether")
    };

    private final JRadioButton[] multiBonds = {
      new JRadioButton("Double Bond"),
      new JRadioButton("Triple Bond")
    };

    private final ButtonGroup group = new ButtonGroup();
    private final List<List<Integer>> nodeListArray = new ArrayList<>(17);

    private int selectedGroup = 0;

    public ElementButtons(JPanel panel) {

        JPanel row1 = new JPanel(new FlowLayout());
        row1.setMaximumSize(new Dimension(2000, 100));
        JPanel row2 = new JPanel(new FlowLayout());
        row2.setMaximumSize(new Dimension(2000, 200));
        JPanel row3 = new JPanel(new FlowLayout());


        for (AbstractButton b : buttonArray) {
            group.add(b);
            b.setFocusable(false);
            b.addActionListener(new ButtonSelectListener());
            row1.add(b);
            nodeListArray.add(new ArrayList<>());
        }

        for (AbstractButton b : higherGroupButtons) {
            group.add(b);
            b.setFocusable(false);
            b.addActionListener(new ButtonSelectListener());
            row2.add(b);
            nodeListArray.add(new ArrayList<>());
        }
        for (AbstractButton b : multiBonds){
            group.add(b);
            b.setFocusable(false);
            b.addActionListener(new ButtonSelectListener());
            row3.add(b);
            nodeListArray.add(new ArrayList<>());
        }

        panel.add(row1);
        panel.add(row2);
        panel.add(row3);


        resetButtons();
    }

    private class ButtonSelectListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton source = (JRadioButton) e.getSource();
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
                case "Carboxylic Acid":
                    selectedGroup = 7;
                    break;
                case "Amide":
                    selectedGroup = 8;
                    break;
                case "Nitrile":
                    selectedGroup = 9;
                    break;
                case "Aldehyde":
                    selectedGroup = 10;
                    break;
                case "Ketone":
                    selectedGroup = 11;
                    break;
                case "Alcohol":
                    selectedGroup = 12;
                    break;
                case "Amine":
                    selectedGroup = 13;
                    break;
                case "Ether":
                    selectedGroup = 14;
                    break;
                case "Double Bond":
                    selectedGroup = 15;
                    break;
                case "Triple Bond":
                    selectedGroup = 16;
                    break;

            }
        }
    }

    public void resetButtons() {
        for (AbstractButton b : buttonArray) {
            b.setSelected(false);
            b.setEnabled(false);
        }
        for (AbstractButton b : higherGroupButtons) {
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
        for (AbstractButton b : higherGroupButtons) {
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
        for (int i = 1; i < 15; i++) {

            for (int node : nodeListArray.get(i)) {
                out.append("\n").append(node+1).append(" ").append(SYMBOLS[i-1]);
            }
        }

        return out.toString();
    }
}
