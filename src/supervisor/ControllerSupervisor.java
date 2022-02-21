package supervisor;

import controller.ModelFeedBin;
import gui.NewFeedBinGUI;

public class ControllerSupervisor implements Runnable {

    private final ModelFeedBin[] bins;

    private volatile String batch;
    private volatile int operation;

    public ControllerSupervisor(ModelFeedBin[]bins) {
        this.bins = bins;
        this.batch = "";
        this.operation = -1;
    }

    /*

    Operation IDs and their roles for the ControllerSupervisor:

    0 : Adding a batch
    1 : Processing the next batch
    2 : Inspecting all the bins

     */
    public void issueOrder(String batch, int operation) {
        this.batch = batch;
        this.operation = operation;
    }

    @Override
    public void run() {

        while (NewFeedBinGUI.controllerLatch.getCount() > 0) {

            if (operation > -1) {

                switch (operation) {

                    case 0:
                        break;

                    case 1:
                        break;

                    case 2:
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
