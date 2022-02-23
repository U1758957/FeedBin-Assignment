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

        if (batch.equals("")) {
            this.batchFailureReason = "Error : No batch to make up!";
            return false;
        }

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

        Map<String, Double> ingredientAmountMap = new HashMap<>();

        ingredientAmountMap.put(ingredientOneName, ingredientOneAmount);
        ingredientAmountMap.put(ingredientTwoName, ingredientTwoAmount);

        for (String currentIngredient : ingredientBinIndexMap.keySet()) {

            double binVolumeSum = 0.0d;

            for (int binIndex : ingredientBinIndexMap.get(currentIngredient))
                binVolumeSum += bins[binIndex].getCurrentVolume();

            if (binVolumeSum < ingredientAmountMap.get(currentIngredient)) {

                this.batchFailureReason =
                        "Error : Bins don't have enough ingredients to satisfy need of ingredient "
                        + currentIngredient
                        + System.lineSeparator()
                        + "Need: "
                        + ingredientAmountMap.get(currentIngredient) + "cm^3" + " | "
                        + "Bins Contain: " + binVolumeSum + "cm^3";

                return false;

            }

        }

        for (String currentIngredient : ingredientBinIndexMap.keySet()) {

            double remainingAmount = ingredientAmountMap.get(currentIngredient);

            for (int binIndex : ingredientBinIndexMap.get(currentIngredient)) {

                double removedAmount = bins[binIndex].removeProduct(remainingAmount);

                if (removedAmount != remainingAmount) {
                    // E.g., if a recipe wants 10.0 of x but a bin only has 8.0 of x, then you will need to take 2.0
                    // of x from another bin, so update the variables to reflect the difference.
                    remainingAmount -= removedAmount;
                    ingredientAmountMap.replace(currentIngredient, remainingAmount);
                } else {
                    // If any bin can satisfy the remaining (or technically full) amount, e.g., you want 10.0 of x, and
                    // you received 10.0 of x, then removedAmount will be equal to remainingAmount, so you have thus
                    // satisfied the production and can break the loop for this ingredient.
                    break;
                }

            }

        }

        // Check if a batch can be made up (and make up the batch by default)

        return true; // If you get all the way to here, then the batch was made up successfully, so return true.

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
