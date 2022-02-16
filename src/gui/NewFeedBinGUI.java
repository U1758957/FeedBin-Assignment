package gui;

import controller.ControllerFeedBin;
import controller.ModelFeedBin;
import supervisor.ControllerSupervisor;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewFeedBinGUI extends JFrame {

    private final ModelFeedBin[] bins;

    private final ControllerFeedBin controller;
    private final ControllerSupervisor supervisor;

    private final ExecutorService controllerService;
    public static CountDownLatch controllerLatch;

    public NewFeedBinGUI() {

        this.bins = new ModelFeedBin[3]; // 3 bins as defined in the specification

        this.controller = new ControllerFeedBin(bins);
        this.supervisor = new ControllerSupervisor(bins);

        this.controllerService = Executors.newFixedThreadPool(2); // 2 controllers, so 2 threads.
        controllerLatch = new CountDownLatch(1); // Used to tell threads to shut down.

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
