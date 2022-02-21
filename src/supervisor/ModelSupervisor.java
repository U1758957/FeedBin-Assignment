package supervisor;

import controller.ModelFeedBin;

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

        return null;

    }

}
