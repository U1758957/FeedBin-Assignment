package recipe;

import java.util.*;

public class ModelRecipe {

    private static final String[] productsArr;

    private static final Map<String, String> recipesMap;
    private static final String[] recipesArr;

    static {

         productsArr = new String[] {"Cornmeal", "Crushed Flakes", "Weety Bits", "Bitty Weets"};

         recipesMap = new HashMap<>();
         recipesMap.put("FlakyMeal", "30_70_Cornmeal_Crushed Flakes");
         recipesMap.put("WeetyMeal", "40_60_Weety Bits_Bitty Weets");

         recipesArr = recipesMap.keySet().toArray(String[]::new);

    }

    public static String[] getProductList() {
        return productsArr;
    }

    public static String[] getRecipeList() {
        return recipesArr;
    }

    public static String[] getRecipeIngredients(String recipe) {

        return recipesMap.get(recipe).split("_"); // Will return e.g., ["30", "70", "Cornmeal", "Crushed Flakes"]
        // so Product 1 will be even indexes, and Product 2 will be odd indexes

    }

}
