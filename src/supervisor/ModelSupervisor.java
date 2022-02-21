package supervisor;

import controller.ModelFeedBin;
import recipe.ModelRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelSupervisor {

    private final ModelFeedBin[] bins;

    public ModelSupervisor(ModelFeedBin[] bins) {
        this.bins = bins;
    }

    public boolean addBatch(String recipe, double amount) {

        String[] recipeIngredients = ModelRecipe.getRecipeIngredients(recipe);

        double ingredientOnePercentage = Double.parseDouble(recipeIngredients[0]) * 0.01d; // E.g., 40% -> 0.4
        String ingredientOneName = recipeIngredients[2];

        double ingredientTwoPercentage = Double.parseDouble(recipeIngredients[1]) * 0.01d;
        String ingredientTwoName = recipeIngredients[3];

        double ingredientOneAmount = amount * ingredientOnePercentage;
        double ingredientTwoAmount = amount * ingredientTwoPercentage;

        System.out.println("Amount: " + amount);
        System.out.println(ingredientOneName + " -> " + ingredientOneAmount);
        System.out.println(ingredientTwoName + " -> " + ingredientTwoAmount);

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
