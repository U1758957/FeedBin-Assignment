package supervisor;

import controller.ModelFeedBin;
import recipe.ModelRecipe;

import java.util.*;

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

        // Check if any bins contain ingredients, and assign ingredients their respective bin(s)

        Map<String, List<Integer>> ingredientBinIndexMap = new HashMap<>();

        ingredientBinIndexMap.put(ingredientOneName, new ArrayList<>());
        ingredientBinIndexMap.put(ingredientTwoName, new ArrayList<>());

        for (int i = 0; i < bins.length; i++) {

            if (bins[i].getProductName().equals(ingredientOneName)) ingredientBinIndexMap.get(ingredientOneName).add(i);
            else if (bins[i].getProductName().equals(ingredientTwoName)) ingredientBinIndexMap.get(ingredientTwoName).add(i);

        }

        for (String ingredientName : ingredientBinIndexMap.keySet()) {

            if (ingredientBinIndexMap.get(ingredientName).size() == 0) {

                this.batchFailureReason = "Error! No Feed Bin contains ingredient " + ingredientName;
                return false;

            }

        }

        // Check if any bins contain ingredients, and assign ingredients their respective bin(s)

        // Check if a batch can be made up (and make up the batch by default)



        // Check if a batch can be made up (and make up the batch by default)

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
