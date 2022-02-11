package gui;

import bin.FeedBin;

import javax.swing.*;
import java.awt.*;

/*
Inspect Menu Option Dialog Class
 */
class InspectDialog extends JDialog {

    private FeedBin bin;

    private Box boxInfo;
    private Box boxButton;
    private JPanel panel;
    private JLabel[] label;
    private JButton OKButton;

    /*
    InspectDialog Constructor
     */
    InspectDialog(FeedBinGUI parent, boolean modal, FeedBin binObject) {

        super(parent, "Bin Inspection", modal); // Call Superclass Constructor

        this.bin = binObject;

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        this.panel = new JPanel();
        this.panel.setLayout(new GridLayout(4, 2));

        this.label = new JLabel[8];
        this.label[0] = new JLabel("Bin Number");
        this.label[2] = new JLabel("Contains");
        this.label[4] = new JLabel("Maximum Volume");
        this.label[6] = new JLabel("Current Volume");
        this.label[1] = new JLabel(String.valueOf(bin.getBinNumber()));
        this.label[3] = new JLabel(bin.getProductName());
        this.label[5] = new JLabel(String.valueOf(bin.getMaxVolume()));
        this.label[7] = new JLabel(String.valueOf(bin.getCurrentVolume()));

        this.OKButton = new JButton("OK");
        OKButton.addActionListener(e -> dispose());

        for (JLabel idLabel : label) this.panel.add(idLabel); // Labels go into a 4x2 Grid

        this.boxInfo = new Box(BoxLayout.Y_AXIS);
        this.boxButton = new Box(BoxLayout.Y_AXIS);
        this.boxInfo.add(panel);
        this.boxButton.add(OKButton);
        getContentPane().add(boxInfo);
        getContentPane().add(boxButton);

        pack();

    } // End InspectDialog Constructor

} // End InspectDialog Class
