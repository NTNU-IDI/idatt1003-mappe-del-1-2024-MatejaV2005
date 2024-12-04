package edu.ntnu.idi.idatt.Model;

import edu.ntnu.idi.idatt.Utils.ExceptionHandling;
import java.util.List;
import java.util.Map;

/**
 * Represents a recipe for a dish or meal.
 * A recipe contains a name, description, cooking process, and a list of ingredients with their required amounts.
 * It also interacts with a {@link FoodStorage} to check if enough ingredients are available to prepare the dish.
 */
public class Recipe {

  private final String nameOfRecipe; // The name of the recipe
  private final String description; // A brief description of the recipe
  private final String process; // Step-by-step instructions for the recipe
  private Map<String, IngredientDetail> ingredients; // Map of ingredient names to their details
  public FoodStorage storage; // Storage instance for checking available groceries

  /**
   * Constructs a Recipe object with the specified details.
   *
   * @param nameOfRecipe the name of the recipe
   * @param description  a brief description of the recipe
   * @param process      the step-by-step cooking process for the recipe
   * @param ingredients  a map of ingredients required for the recipe, where the key is the ingredient name,
   *                     and the value is the required amount
   * @throws IllegalArgumentException if the name, description, or process are invalid
   */
  public Recipe(String nameOfRecipe, String description, String process, Map<String, IngredientDetail> ingredients) {
    ExceptionHandling.validateName(nameOfRecipe);
    ExceptionHandling.validateName(description);
    ExceptionHandling.validateName(process);

    this.nameOfRecipe = nameOfRecipe;
    this.description = description;
    this.process = process;
    this.ingredients = ingredients;
    this.storage = new FoodStorage();
  }

  /**
   * Retrieves the name of the recipe.
   *
   * @return the name of the recipe
   */
  public String getNameOfRecipe() {
    return nameOfRecipe;
  }

  /**
   * Retrieves the description of the recipe.
   *
   * @return a brief description of the recipe
   */
  public String getDescription() {
    return description;
  }

  /**
   * Retrieves the cooking process or instructions for the recipe.
   *
   * @return the step-by-step cooking process
   */
  public String getProcess() {
    return process;
  }

  /**
   * Retrieves the list of ingredients for the recipe.
   *
   * @return a map of ingredients, where the key is the ingredient name, and the value is the required amount
   */
  public Map<String, IngredientDetail> getIngredients() {
    return ingredients;
  }

  /**
   * Sets the storage instance for the recipe.
   * This allows the recipe to check available ingredients against the storage.
   *
   * @param storage the {@link FoodStorage} instance to set
   * @return the set storage instance
   */
  public FoodStorage setStorage(FoodStorage storage) {
    this.storage = storage;
    return storage;
  }

  /**
   * Prints the missing ingredients required to make the recipe.
   * If an ingredient is missing or insufficient, its name, missing amount, and unit are displayed.
   */
  public void getMissingIngredients() {
    ingredients.forEach((ingredientName, requiredDetail) -> {
      double availableAmount = storage.getGroceriesByName(ingredientName).stream()
          .mapToDouble(Grocery::getAmount)
          .sum();

      if (availableAmount < requiredDetail.getAmount()) {
        double missingAmount = requiredDetail.getAmount() - availableAmount;
        System.out.printf("- %s: Missing %.2f %s%n", ingredientName, missingAmount, requiredDetail.getUnit());
      }
    });
  }

  /**
   * Checks if the recipe can be made with the available ingredients in storage.
   *
   * @return {@code true} if all required ingredients are available in sufficient quantities, {@code false} otherwise
   */
  public boolean canMakeRecipe() {
    for (Map.Entry<String, IngredientDetail> entry : ingredients.entrySet()) {
      String ingredientName = entry.getKey();
      IngredientDetail requiredDetail = entry.getValue();

      double availableAmount = storage.getGroceriesByName(ingredientName).stream()
          .mapToDouble(Grocery::getAmount)
          .sum();

      // Check if available amount is less than the required amount
      if (availableAmount < requiredDetail.getAmount()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns a formatted string representation of the Recipe object.
   * The output includes the recipe's name, description, process,
   * and a table displaying the ingredients and their respective amounts.
   *
   * <p>Example output:</p>
   * <pre>
   * Recipe: Spaghetti Bolognese
   * Description: Classic Italian pasta dish with meat sauce.
   * Process: Cook pasta, prepare sauce, mix and serve.
   * Ingredients:
   * Ingredient           | Amount
   * ---------------------------------
   * Spaghetti            | 200.00 g
   * Ground Beef          | 300.00 g
   * Tomato Sauce         | 150.00 ml
   * Onion                | 50.00 g
   * </pre>
   *
   * @return A formatted string representation of the Recipe object
   */
  @Override
  public String toString() {
    StringBuilder ingredientsTable = new StringBuilder();
    ingredientsTable.append(String.format("%-20s | %-10s%n", "Ingredient", "Amount"));
    ingredientsTable.append(String.format("%s%n", "-".repeat(33)));

    // Loop through the ingredients and format them correctly
    for (Map.Entry<String, IngredientDetail> entry : ingredients.entrySet()) {
      String ingredientName = entry.getKey();
      IngredientDetail detail = entry.getValue();
      ingredientsTable.append(String.format("%-20s | %-10.2f %s%n",
          ingredientName,
          detail.getAmount(),
          detail.getUnit())); // Include both amount and unit
    }

    // Combine recipe details with the formatted ingredient list
    return String.format(
        "Recipe: %s%n" +
            "Description: %s%n" +
            "Process: %s%n" +
            "Ingredients:%n%s",
        nameOfRecipe,
        description,
        process,
        ingredientsTable
    );
  }
}
