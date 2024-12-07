package edu.ntnu.idi.idatt.Model;

import edu.ntnu.idi.idatt.Utils.ExceptionHandling;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cookbook that stores recipes and provides functionality
 * to manage and search for recipes.
 */
public class RecipeBook {

  private final List<Recipe> recipes;

  /**
   * Constructs an empty cookbook.
   */
  public RecipeBook() {
    this.recipes = new ArrayList<>();
  }

  /**
   * Adds a recipe to the cookbook.
   *
   * @param recipe the recipe to add
   * @throws IllegalArgumentException if the recipe is null or already exists in the cookbook
   */
  public void addRecipe(Recipe recipe) {
    ExceptionHandling.validateRecipeInRecipeBook(recipe, recipes);

    recipes.add(recipe);
  }

  /**
   * Retrieves a recipe by name.
   *
   * @param recipeName the name of the recipe to retrieve
   * @return the recipe with the specified name, or null if not found
   */
  public Recipe getRecipe(String recipeName) {
    return recipes.stream()
        .filter(recipe -> recipe.getNameOfRecipe().equalsIgnoreCase(recipeName))
        .findFirst()
        .orElse(null);
  }

  /**
   * Returns a list of recipes that can be made with the current storage.
   *
   * @param storage the storage to check against
   * @return a list of recipes that can be made
   */
  public List<Recipe> getAvailableRecipes(FoodStorage storage) {
    List<Recipe> availableRecipes = new ArrayList<>();

    for (Recipe recipe : recipes) {
      recipe.setStorage(storage); // Attach the storage to the recipe
      if (recipe.canMakeRecipe()) {
        availableRecipes.add(recipe);
      }
    }

    return availableRecipes;
  }
}
