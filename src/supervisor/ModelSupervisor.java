package supervisor;

import controller.ModelFeedBin;
import recipe.ModelRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelSupervisor {

    private final ModelFeedBin[] bins;

    private volatile String currentBatchOrder;

    private volatile String batchFailureReason;

    public ModelSupervisor(ModelFeedBin[] bins) {
        this.bins = bins;
        this.currentBatchOrder = "";
        this.batchFailureReason = "";
    }

    public String getCurrentBatchOrder() {
        return currentBatchOrder;
    }

    public String getBatchFailureReason() {
        return batchFailureReason;
    }

    public boolean addBatch(String recipe, double amount) {

        if (amount <= 0) return false;

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

        return true;

    }

    public boolean processBatch(String batch) {

        String[] batchSplit = batch.split(" -> ")[1].split("_");

        String[] ingredientOneSplit = batchSplit[0].split("-");
        String[] ingredientTwoSplit = batchSplit[1].split("-");

        String ingredientOneName = ingredientOneSplit[0];
        double ingredientOneAmount = Double.parseDouble(ingredientOneSplit[1]);

        String ingredientTwoName = ingredientTwoSplit[0];
        double ingredientTwoAmount = Double.parseDouble(ingredientTwoSplit[1]);

        boolean[] binContainsIngredients = new boolean[] {false, false};

        for (ModelFeedBin bin : bins) {

            if (bin.getProductName().equals(ingredientOneName)) binContainsIngredients[0] = true;
            if (bin.getProductName().equals(ingredientTwoName)) binContainsIngredients[1] = true;

            if (binContainsIngredients[0] & binContainsIngredients[1]) break;

        }

        if (! binContainsIngredients[0]) {
            this.batchFailureReason = "Error! No Feed Bin contains ingredient " + ingredientOneName;
            return false;
        }

        if (! binContainsIngredients[1]) {
            this.batchFailureReason = "Error! No Feed Bin contains ingredient " + ingredientTwoName;
            return false;
        }

        return true;

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
