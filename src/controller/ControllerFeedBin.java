package controller;

import gui.NewFeedBinGUI;

public class ControllerFeedBin implements Runnable {

    private final ModelFeedBin[] bins;

    public ControllerFeedBin(ModelFeedBin[] bins) {
        this.bins = bins;
    }

    @Override
    public void run() {

        while (NewFeedBinGUI.controllerLatch.getCount() > 0) {

            // Critical section here

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
