package gui;

import controller.ControllerFeedBin;
import controller.ModelFeedBin;
import supervisor.ControllerSupervisor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NewFeedBinGUI extends JFrame {

    private ModelFeedBin[] bins;

    private ControllerFeedBin controller;
    private ControllerSupervisor supervisor;

    private ExecutorService controllerService;
    public static CountDownLatch controllerLatch;
    public static CountDownLatch exitLatch;
    private JPanel panelMain;
    private JPanel panelMenu;
    private JButton buttonBinController;
    private JButton buttonSupervisor;
    private JButton buttonExit;

    public NewFeedBinGUI() {

        initGUIComponents();
        initNonGUIComponents();

        buttonExit.addActionListener(e -> {

            boolean shutdown = false;

            try {
                controllerLatch.countDown();
                shutdown = exitLatch.await(5L, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {}

            if (! shutdown) System.err.println("Error : Could not stop threads gracefully!");

            System.exit(shutdown ? 0 : -1); // Shutdown regardless of error, as JVM should be able to terminate process

        });

        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                buttonExit.doClick(); // Overriding the close button to try and force graceful shutdown
            }

        });

    }

    private void initGUIComponents() {

        this.buttonBinController.setText("Bin Controller");
        this.buttonSupervisor.setText("Supervisor");
        this.buttonExit.setText("Exit");

        this.setContentPane(panelMain);
        this.setTitle("Feed Bin Demo");
        this.setPreferredSize(new Dimension(400, 200));
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null); // Spawns GUI at the center of the screen
        this.pack();
        this.setVisible(true);

    }

    private void initNonGUIComponents() {

        this.bins = new ModelFeedBin[3]; // 3 bins as defined in the specification

        this.controller = new ControllerFeedBin(bins);
        this.supervisor = new ControllerSupervisor(bins);

        this.controllerService = Executors.newFixedThreadPool(2); // 2 controllers, so 2 threads.
        controllerLatch = new CountDownLatch(1); // Used to tell threads to shut down.

        exitLatch = new CountDownLatch(2); // The two threads will trigger this when the program exits.

        this.controllerService.submit(controller);
        this.controllerService.submit(supervisor);

        this.controllerService.shutdown(); // Just tells the JVM that the service will not be accepting new submits

    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Warning: No system look-and-feel found, using default Swing look-and-feel");
        }

        SwingUtilities.invokeLater(NewFeedBinGUI::new);

    }

}
