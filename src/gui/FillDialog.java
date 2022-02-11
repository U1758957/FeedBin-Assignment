package gui;

import bin.FeedBin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

} // Class AddDialog

class FillException extends RuntimeException {}
