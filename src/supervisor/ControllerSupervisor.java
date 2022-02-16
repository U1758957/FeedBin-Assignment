package supervisor;

import controller.ModelFeedBin;
import gui.NewFeedBinGUI;

public class ControllerSupervisor implements Runnable {

    private final ModelFeedBin[] bins;

    public ControllerSupervisor(ModelFeedBin[]bins) {
        this.bins = bins;
    }

    @Override
    public void run() {

        while (NewFeedBinGUI.controllerLatch.getCount() > 0) {

            // Critical section here

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.err.println("Error : " + e.getClass().getName() + " was interrupted!");
                e.printStackTrace(); // Friendly message followed by stack trace
            }

        }

    }

}
