package gui;

import javax.swing.*;
import java.awt.*;

public class NewFeedBinGUI extends JFrame {

    public NewFeedBinGUI() {



    }

    public static void main(String[] args) {

        NewFeedBinGUI demo = new NewFeedBinGUI();
        demo.setTitle("Feed Bin Demo");
        demo.setDefaultCloseOperation(EXIT_ON_CLOSE);
        demo.setSize(new Dimension(400, 200));
        demo.setLocationRelativeTo(null); // Spawns GUI in the center of your screen
        demo.setVisible(true);

    }

}
