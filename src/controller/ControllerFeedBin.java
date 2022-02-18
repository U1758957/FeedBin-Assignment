package controller;

import gui.NewFeedBinGUI;

public class ControllerFeedBin implements Runnable {

    private final ModelFeedBin[] bins;

    private volatile int binNumber;
    private volatile int operation;
    private volatile String value;

    public ControllerFeedBin(ModelFeedBin[] bins) {
        this.bins = bins;
        this.binNumber = 0;
        this.operation = -1;
    }

    private boolean setProductName(int binNumber, String newProductName) {
        return this.bins[binNumber].setProductName(newProductName);
    }

    private void flush(int binNumber) {
        this.bins[binNumber].flush();
    }

    private boolean addProduct(int binNumber, double volume) {
        return this.bins[binNumber].addProduct(volume);
    }

    private double removeProduct(int binNumber, double volume) {
        return this.bins[binNumber].removeProduct(volume);
    }

    private String[] inspectBin(int binNumber) {

        String binNo = String.valueOf(this.bins[binNumber].getBinNumber()); // Kind of moot, but doing it for sake of completeness
        String productName = this.bins[binNumber].getProductName();
        String maxVolume = String.valueOf(this.bins[binNumber].getMaxVolume());
        String currentVolume = String.valueOf(this.bins[binNumber].getCurrentVolume());

        return new String[] {binNo, productName, maxVolume, currentVolume};

    }

    public void issueOrder(int binNumber, int operation, String value) {
        this.binNumber = binNumber;
        this.operation = operation;
        this.value = value;
    }

    @Override
    public void run() {

        while (NewFeedBinGUI.controllerLatch.getCount() > 0) {

            if (operation > -1) {

                switch (operation) {

                    case 0:

                        boolean isProductNameSet = setProductName(binNumber, value);

                        if (! isProductNameSet); // TODO: 17/02/2022 Notify client that name cannot be changed

                        break;

                    case 1:

                        flush(binNumber);
                        break;

                    case 2:

                        boolean isProductAdded = addProduct(binNumber, Double.parseDouble(value));
                        // Double parse-ability is guaranteed by the front-end

                        if (! isProductAdded); // TODO: 17/02/2022 Notify client that amount cannot be added

                        break;

                    case 3:

                        String[] binInformation = inspectBin(binNumber);

                        // TODO: 17/02/2022 Show dialogue informing the client of the bin stats

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
