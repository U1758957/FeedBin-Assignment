package controller;

import gui.NewFeedBinGUI;

public class ControllerFeedBin implements Runnable {

    private final ModelFeedBin[] bins;

    public ControllerFeedBin(ModelFeedBin[] bins) {
        this.bins = bins;
    }

    private void setProductName(int binNumber, String newProductName) {

        boolean isNameChanged = this.bins[binNumber].setProductName(newProductName);

        if (! isNameChanged); // TODO: 17/02/2022 Notify the client that they could not change the name

    }

    private void flush(int binNumber) {
        this.bins[binNumber].flush();
    }

    private void addProduct(int binNumber, double volume) {

        boolean isProductAdded = this.bins[binNumber].addProduct(volume);

        if (! isProductAdded); // TODO: 17/02/2022 Notify the client that they could not add the product

    }

    private void removeProduct(int binNumber, double volume) {

        double removedProduct = this.bins[binNumber].removeProduct(volume);

        // TODO: 17/02/2022 Place the removed volume into the buffer when it's made

    }

    private void inspectBin(int binNumber) {

        int binNo = this.bins[binNumber].getBinNumber(); // Kind of moot, but doing it for sake of completeness
        String productName = this.bins[binNumber].getProductName();
        double maxVolume = this.bins[binNumber].getMaxVolume();
        double currentVolume = this.bins[binNumber].getCurrentVolume();

        // TODO: 17/02/2022 Create dialogue box showing the stats for the bin for the client

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
