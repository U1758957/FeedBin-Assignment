package supervisor;

import controller.ModelFeedBin;
import gui.NewFeedBinGUI;

import java.util.List;

public class ControllerSupervisor implements Runnable {

    private final ModelFeedBin[] bins;
    private final ModelSupervisor supervisor;

    private volatile String recipe;
    private volatile String batch;
    private volatile double amount;
    private volatile int operation;

    private volatile String currentBatchOrder;

    private volatile boolean orderFulfillment;
    private volatile List<String[]> inspectionResults;

    public ControllerSupervisor(ModelFeedBin[] bins) {
        this.bins = bins;
        this.supervisor = new ModelSupervisor(bins);
        this.recipe = "";
        this.batch = "";
        this.amount = 0.0d;
        this.operation = -1;
    }

    private boolean addBatch(String recipe, double amount) {
         return this.supervisor.addBatch(recipe, amount);
    }

    private boolean processBatch(String batch) {
        return this.supervisor.processBatch(batch);
    }

    private List<String[]> inspectAllBins() {
        return supervisor.inspectAllBins();
    }

    public String getCurrentBatchOrder() {
        return currentBatchOrder;
    }

    /*

        Operation IDs and their roles for the ControllerSupervisor:

        0 : Adding a batch (from a recipe)
        1 : Processing the next batch
        2 : Inspecting all the bins

         */
    public void issueOrder(String recipe, String batch, double amount, int operation) {
        this.recipe = recipe;
        this.batch = batch;
        this.amount = amount;
        this.operation = operation;
    }

    public boolean isOrderFulfilled() {
        return orderFulfillment;
    }

    public List<String[]> getInspectionResults() {
        return inspectionResults;
    }

    @Override
    public void run() {

        while (NewFeedBinGUI.controllerLatch.getCount() > 0) {

            if (operation > -1) {

                switch (operation) {

                    case 0:

                        this.orderFulfillment = addBatch(recipe, amount);
                        this.currentBatchOrder = supervisor.getCurrentBatchOrder();
                        break;

                    case 1:

                        this.orderFulfillment = processBatch(batch);
                        break;

                    case 2:

                        this.inspectionResults = inspectAllBins();
                        break;

                }

                this.operation = -1;
                NewFeedBinGUI.guiLatch.countDown();

            }

            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {
                System.err.println("Error : " + getClass().getName() + " was interrupted!");
                e.printStackTrace(); // Friendly message followed by stack trace
            }

        }

        NewFeedBinGUI.exitLatch.countDown();

    }

}
