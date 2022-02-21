package supervisor;

import controller.ModelFeedBin;

import java.util.ArrayList;
import java.util.List;

public class ModelSupervisor {

    private final ModelFeedBin[] bins;

    public ModelSupervisor(ModelFeedBin[] bins) {
        this.bins = bins;
    }

    public boolean addBatch(String recipe, int amount) {

        return false;

    }

    public boolean processBatch(String batch) {

        return false;

    }

    public List<String[]> inspectAllBins() {

        List<String[]> binInspectionResults = new ArrayList<>();

        for (ModelFeedBin bin : bins) {

            String binNo = String.valueOf(bin.getBinNumber());
            String productName = bin.getProductName();
            String maxVolume = String.valueOf(bin.getMaxVolume());
            String currentVolume = String.valueOf(bin.getCurrentVolume());

            binInspectionResults.add(new String[] {binNo, productName, maxVolume, currentVolume} );

        }

        return binInspectionResults;

    }

}
