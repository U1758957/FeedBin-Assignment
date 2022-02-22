package supervisor;

import controller.ModelFeedBin;
import recipe.ModelRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelSupervisor {

    private final ModelFeedBin[] bins;

    private volatile String currentBatchOrder;

    public ModelSupervisor(ModelFeedBin[] bins) {
        this.bins = bins;
        this.currentBatchOrder = "";
    }

    public String getCurrentBatchOrder() {
        return currentBatchOrder;
    }

    public void addBatch(String recipe, double amount) {

        String[] recipeIngredients = ModelRecipe.getRecipeIngredients(recipe);

        double ingredientOnePercentage = Double.parseDouble(recipeIngredients[0]) * 0.01d; // E.g., 40% -> 0.4
        String ingredientOneName = recipeIngredients[2];

        double ingredientTwoPercentage = Double.parseDouble(recipeIngredients[1]) * 0.01d;
        String ingredientTwoName = recipeIngredients[3];

        double ingredientOneAmount = amount * ingredientOnePercentage;
        double ingredientTwoAmount = amount * ingredientTwoPercentage;

        this.currentBatchOrder = // E.g., WeetyMeal (20.0) -> Weety Bits-8.0_Bitty Weets-9.0

                recipe + " " + "(" + amount + ")" + " -> "
                +
                ingredientOneName + "-" + ingredientOneAmount
                + "_" +
                ingredientTwoName + "-" + ingredientTwoAmount;

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
