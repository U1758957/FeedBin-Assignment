package gui;

import bin.FeedBin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * FeedBinGUI.java
 *
 * @author Anne Grundy
 *
 * Modified by Gary Allen
 * Modified by Zac King (U1758957)
 *
 * Latest Update February 2022
 *
 * @version 1.2
 *
 */
public class FeedBinGUI extends JFrame {

    private JPanel panelMain;

    private JMenuBar jmbTop;
    private JMenu binMenu;
    private JMenuItem inspect;
    private JMenuItem fill;
    private JMenuItem flush;
    private JMenuItem exit;

    private FeedBin bin; // Here's the system object behind the interface

    public FeedBinGUI() {

        this.bin = new FeedBin(34, "Weety Bits"); // Create a Feed Bin

        // Create the Menu Components

        this.binMenu = new JMenu("Bin");

        this.inspect = new JMenuItem("Inspect the Bin...");
        this.fill = new JMenuItem("Add More Product...");
        this.flush = new JMenuItem("Flush the Bin...");
        this.exit = new JMenuItem("Exit");

        this.binMenu.add(inspect);
        this.binMenu.add(fill);
        this.binMenu.add(flush);
        this.binMenu.add(new JSeparator());
        this.binMenu.add(exit);

        this.jmbTop.add(binMenu);
        setJMenuBar(jmbTop);

        exit.addActionListener(e -> System.exit(0));

        inspect.addActionListener(e -> {

            InspectDialog id = new InspectDialog(FeedBinGUI.this, true, bin);
            id.setVisible(true);

        });

        fill.addActionListener(e -> {

            FillDialog fd = new FillDialog(FeedBinGUI.this, true, bin);
            fd.setVisible(true);

        });

        flush.addActionListener(e -> {

            bin.flush();

            JOptionPane.showMessageDialog(
                    FeedBinGUI.this,
                    "The bin is now empty",
                    "Flush Confirmation",
                    JOptionPane.INFORMATION_MESSAGE,
                    null);

        });

        setTitle("Feed Bin Controller");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 200);

    }

    public static void main(String[] args) {

        FeedBinGUI demo = new FeedBinGUI();
        demo.setLocation(400, 400);
        demo.setVisible(true);

    }

} // End Class FeedBinGUI

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

class FillDialog extends JDialog implements ActionListener {

    private FeedBin bin;
    private JTextField input;
    private JPanel panel1;
    private JPanel panel2;
    private JLabel label;
    private JButton applyButton;
    private JButton clearButton;
    private JButton cancelButton;

    /*
    FillDialog Constructor
     */
    FillDialog(FeedBinGUI parent, boolean modal, FeedBin binObject) {

        super(parent, "Bin Filling", modal); // Call Superclass Constructor

        this.bin = binObject;

        this.panel1 = new JPanel();
        this.panel2 = new JPanel();

        this.label = new JLabel("How much is to be added?");
        this.input = new JTextField(10);

        this.applyButton = new JButton("Apply");
        this.clearButton = new JButton("Clear");
        this.cancelButton = new JButton("Cancel");

        this.applyButton.addActionListener(this);
        this.clearButton.addActionListener(this);
        this.cancelButton.addActionListener(this);

        this.panel1.add(label);
        this.panel2.add(input);

        getContentPane().add(panel1, "North");

        this.panel2.add(applyButton);
        this.panel2.add(clearButton);
        this.panel2.add(cancelButton);

        getContentPane().add(panel2, "South");

        pack();

    }

    /*
    Button Event Handler
     */
    @Override
    public void actionPerformed(ActionEvent event) {

        double volume;
        String outcome;
        String butLabel = event.getActionCommand();

        if (butLabel.equals("Apply")) {

            try {

                volume = Double.parseDouble(input.getText());

                if (volume < 0.0d) throw new NumberFormatException();

                if (! bin.addProduct(volume)) throw new FillException(); // Interact with the Bin Object here

                JOptionPane.showMessageDialog(
                        this,
                        "Addition Confirmed",
                        "Fill Report",
                        JOptionPane.INFORMATION_MESSAGE,
                        null);

                dispose();

            } catch (NumberFormatException e) { // May be thrown by parseDouble()

                JOptionPane.showMessageDialog(
                        this,
                        "Not a valid number",
                        "Fill Report",
                        JOptionPane.WARNING_MESSAGE,
                        null
                );

            } catch (FillException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Not enough room in the bin",
                        "Fill Report",
                        JOptionPane.WARNING_MESSAGE,
                        null
                );

            } // End of Apply Button Handling

        } else if (butLabel.equals("Clear")) {

            this.input.setText("");

        } else dispose(); // Must have pressed the cancel button

    }

} // Class FillDialog

class FillException extends RuntimeException {}