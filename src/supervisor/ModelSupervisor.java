package supervisor;

import controller.ModelFeedBin;

import java.util.List;

public class ModelSupervisor {

    private final ModelFeedBin[] bins;

    public ModelSupervisor(ModelFeedBin[] bins) {
        this.bins = bins;
    }

    private boolean addBatch(String recipe, int amount) {

        return false;

    }

    private boolean processBatch(String batch) {

        return false;

    }

    private List<String[]> inspectAllBins() {

        return null;

    }

}
