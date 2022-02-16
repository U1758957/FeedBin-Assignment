package supervisor;

import controller.ModelFeedBin;

public class ControllerSupervisor implements Runnable {

    private final ModelFeedBin[] bins;

    public ControllerSupervisor(ModelFeedBin[]bins) {
        this.bins = bins;
    }

    @Override
    public void run() {

    }

}
