package gui;

import bin.FeedBin;

import javax.swing.*;
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
