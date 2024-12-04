package edu.ntnu.idi.idatt.Model;

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
    if (recipe == null) {
      throw new IllegalArgumentException("Recipe cannot be null.");
    }
    if (recipes.stream().anyMatch(r -> r.getNameOfRecipe().equalsIgnoreCase(recipe.getNameOfRecipe()))) {
      throw new IllegalArgumentException("Recipe with the same name already exists in the cookbook.");
    }
    recipes.add(recipe);
    System.out.println("Recipe \"" + recipe.getNameOfRecipe() + "\" added to the cookbook.");
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
}

