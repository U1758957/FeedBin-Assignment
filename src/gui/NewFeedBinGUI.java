package gui;

import controller.ControllerFeedBin;
import controller.ModelFeedBin;
import recipe.ModelRecipe;
import supervisor.ControllerSupervisor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NewFeedBinGUI extends JFrame {

    private JPanel panelMain;
    private JButton buttonBinController;
    private JButton buttonSupervisor;
    private JButton buttonExit;
    private JPanel panelBinController;
    private JPanel panelSupervisor;
    private JComboBox<String> comboBoxBinSelection;
    private JLabel labelComboBoxBinSelection;
    private JTextField textFieldAddProduct;
    private JButton buttonAddProduct;
    private JButton buttonFlushBin;
    private JButton buttonChangeProductName;
    private JComboBox<String> comboBoxChangeProductName;
    private JButton buttonInspectBin;
    private JTextArea textAreaInspectionResult;
    private JComboBox<String> comboBoxRecipeList;
    private JTextField textFieldBatchAmount;
    private JButton buttonAddBatch;
    private JButton buttonInspectAllBins;
    private JTextArea textAreaBatchList;
    private JButton buttonProcessNextBatch;
    private JTextArea textAreaAllBinsInspection;

    private ModelFeedBin[] bins;

    private ControllerFeedBin controller;
    private ControllerSupervisor supervisor;

    private ExecutorService controllerService;
    public static CountDownLatch controllerLatch; // Used to tell the controllers to shut down
    public static CountDownLatch exitLatch; // Used to tell the GUI that the controllers have shut down
    public static CountDownLatch guiLatch; // Used to make sure operations succeed before accessing a controller again

    private final Dimension[] guiDimensions = new Dimension[] {

            new Dimension(448, 352), // Bin Controller / Default
            new Dimension(640, 480) // Supervisor Controller

    }; // The GUI changes size between the Bin Controller and Supervisor Controller interfaces, so store the sizes here

    public NewFeedBinGUI() {

        initNonGUIComponents();
        initGUIComponents();

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

        buttonBinController.addActionListener(e -> {

            this.panelSupervisor.setVisible(false);
            this.setSize(guiDimensions[0]);
            this.panelBinController.setVisible(true);

        });

        buttonSupervisor.addActionListener(e -> {

            this.panelBinController.setVisible(false);
            this.setSize(guiDimensions[1]);
            this.panelSupervisor.setVisible(true);

        });

        // ControllerFeedBin GUI elements

        buttonAddProduct.addActionListener(e -> {

            try {

                double volume = Double.parseDouble(textFieldAddProduct.getText());

                guiLatch = new CountDownLatch(1);
                this.controller.issueOrder(comboBoxBinSelection.getSelectedIndex(), 2, String.valueOf(volume));
                guiLatch.await();

                JOptionPane.showMessageDialog(
                        this,
                        controller.isOrderFulFilled() ? "Successfully added product!" : "Failed to add product!",
                        "Notification",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Adding product input must be a number!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);

            } catch (InterruptedException ex) {

                System.err.println("Error : GUI interrupted whilst awaiting controller operation!");
                System.exit(-1);

            }

        });

        buttonFlushBin.addActionListener(e -> {

            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you would like to flush " + comboBoxBinSelection.getSelectedItem() + "?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {

                try {

                    guiLatch = new CountDownLatch(1);
                    this.controller.issueOrder(comboBoxBinSelection.getSelectedIndex(), 1, null);
                    guiLatch.await();

                } catch (InterruptedException ex) {

                    System.err.println("Error : GUI interrupted whilst awaiting controller operation!");
                    System.exit(-1);

                }

            }

        });

        buttonChangeProductName.addActionListener(e -> {

            try {

                guiLatch = new CountDownLatch(1);
                this.controller.issueOrder(comboBoxBinSelection.getSelectedIndex(), 0, (String) comboBoxChangeProductName.getSelectedItem());
                guiLatch.await();

                JOptionPane.showMessageDialog(
                        this,
                        controller.isOrderFulFilled() ? "Successfully changed product name!" : "Failed to change product name!",
                        "Notification",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (InterruptedException ex) {

                System.err.println("Error : GUI interrupted whilst awaiting controller operation!");
                System.exit(-1);

            }

        });

        buttonInspectBin.addActionListener(e -> {

            try {

                guiLatch = new CountDownLatch(1);
                this.controller.issueOrder(comboBoxBinSelection.getSelectedIndex(), 3, null);
                guiLatch.await();

                String[] binInspectionResult = controller.getInspectionResult();

                textAreaInspectionResult.setText(""); // Clear the previous inspection

                textAreaInspectionResult.append("Bin Number: " + binInspectionResult[0] + System.lineSeparator());
                textAreaInspectionResult.append("Product Name: " + binInspectionResult[1] + System.lineSeparator());
                textAreaInspectionResult.append("Max Volume: " + binInspectionResult[2] + System.lineSeparator());
                textAreaInspectionResult.append("Current Volume: " + binInspectionResult[3]);

            } catch (InterruptedException ex) {

                System.err.println("Error : GUI interrupted whilst awaiting controller operation!");
                System.exit(-1);

            }

        });

        // ControllerFeedBin GUI elements

        // SupervisorController GUI elements

        buttonAddBatch.addActionListener(e -> {

            try {

                double amount = Double.parseDouble(textFieldBatchAmount.getText());

                guiLatch = new CountDownLatch(1);
                this.supervisor.issueOrder((String) comboBoxRecipeList.getSelectedItem(), "", amount, 0);
                guiLatch.await();

                boolean wasOrderFulfilled = supervisor.isOrderFulfilled();

                JOptionPane.showMessageDialog(
                        this,
                        wasOrderFulfilled ? "Batch successfully added!" : "Failed to add batch!",
                        "Notification",
                        JOptionPane.INFORMATION_MESSAGE
                );

                if (wasOrderFulfilled)
                    this.textAreaBatchList.append(supervisor.getCurrentBatchOrder() + System.lineSeparator());

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Batch amount must be a number!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);

            } catch (InterruptedException ex) {

                System.err.println("Error : GUI interrupted whilst awaiting supervisor operation!");
                System.exit(-1);

            }

        });

        buttonProcessNextBatch.addActionListener(e -> {

            String[] batchList = textAreaBatchList.getText().split(System.lineSeparator());
            String currentBatch = batchList[batchList.length - 1]; // Take the current batch from the bottom of the "stack"

            // A jTextArea is technically just a String, with the convenience of it looking like a list with new-lines.
            // This does mean that to treat it as a list, you need to do String splitting and rebuilding, but the
            // performance cost is negligible and not critical, so the convenience for the user is preferred.

            this.textAreaBatchList.setText("");

            for (int i = 0; i < batchList.length - 1; i++) // Rebuild the text area without the last element
                this.textAreaBatchList.append(batchList[i] + System.lineSeparator());


            try {

                guiLatch = new CountDownLatch(1);
                this.supervisor.issueOrder("", currentBatch, 0, 1);
                guiLatch.await();

                // TODO: 22/02/2022 React to boolean result

            } catch (InterruptedException ex) {

                System.err.println("Error : GUI interrupted whilst awaiting supervisor operation!");
                System.exit(-1);

            }

        });

        buttonInspectAllBins.addActionListener(e -> {

            try {

                guiLatch = new CountDownLatch(1);
                this.supervisor.issueOrder("", "", 0, 2);
                guiLatch.await();

                List<String[]> binInspectionResults = supervisor.getInspectionResults();

                textAreaAllBinsInspection.setText(""); // Clear the previous inspections

                for (String[] currentBinInspection : binInspectionResults) {

                    textAreaAllBinsInspection.append("Bin Number: " + currentBinInspection[0] + System.lineSeparator());
                    textAreaAllBinsInspection.append("Product Name: " + currentBinInspection[1] + System.lineSeparator());
                    textAreaAllBinsInspection.append("Max Volume: " + currentBinInspection[2] + System.lineSeparator());
                    textAreaAllBinsInspection.append("Current Volume: " + currentBinInspection[3] + System.lineSeparator());

                   textAreaAllBinsInspection.append(System.lineSeparator());

                }

            } catch (InterruptedException ex) {

                System.err.println("Error : GUI interrupted whilst awaiting supervisor operation!");
                System.exit(-1);

            }

        });

        // SupervisorController GUI elements

    }

    private void initGUIComponents() {

        this.buttonBinController.setText("Bin Controller");
        this.buttonSupervisor.setText("Supervisor");
        this.buttonExit.setText("Exit");

        // Bin Controller Interface Init

        this.labelComboBoxBinSelection.setText("Bin Selection");
        for (int i = 0; i < bins.length; i++) this.comboBoxBinSelection.addItem("Feed Bin #" + (i + 1));

        this.buttonAddProduct.setText("Add Product");

        this.buttonFlushBin.setText("Flush Bin");

        this.buttonChangeProductName.setText("Change Product Name");
        for (String productName : ModelRecipe.getProductList()) comboBoxChangeProductName.addItem(productName);

        this.buttonInspectBin.setText("Inspect Bin");

        // Bin Controller Interface Init

        // Supervisor Controller Interface Init

        for (String recipeName : ModelRecipe.getRecipeList()) comboBoxRecipeList.addItem(recipeName);
        this.buttonAddBatch.setText("Add Batch");

        this.buttonInspectAllBins.setText("Inspect All Bins");

        this.buttonProcessNextBatch.setText("Process Next Batch");

        // Supervisor Controller Interface Init

        this.setContentPane(panelMain);
        this.setTitle("Feed Bin Demo");
        this.setPreferredSize(guiDimensions[0]);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null); // Spawns GUI at the center of the screen
        this.panelSupervisor.setVisible(false);
        this.panelBinController.setVisible(true);
        this.pack();
        this.setVisible(true);

    }

    private void initNonGUIComponents() {

        this.bins = new ModelFeedBin[3]; // 3 bins as defined in the specification

        this.bins[0] = new ModelFeedBin(0, "Cornmeal"); // Manually declaring starting bins for demo
        this.bins[1] = new ModelFeedBin(1, "Crushed Flakes");
        this.bins[2] = new ModelFeedBin(2, "Crushed Flakes");

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
