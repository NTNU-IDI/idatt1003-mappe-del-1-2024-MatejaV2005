package edu.ntnu.idi.idatt.Model;

import edu.ntnu.idi.idatt.Utils.ExceptionHandling;
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
  private final Map<String, IngredientDetail> ingredients; // Map of ingredient names to their details
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
    this.nameOfRecipe = validateAndSetName(nameOfRecipe);
    this.description = validateAndSetDescription(description);
    this.process = validateAndSetProcess(process);
    this.ingredients = validateAndSetIngredients(ingredients);
    this.storage = new FoodStorage();
  }

  public String getNameOfRecipe() {
    return nameOfRecipe;
  }

  public String getDescription() {
    return description;
  }

  public String getProcess() {
    return process;
  }

  public Map<String, IngredientDetail> getIngredients() {
    return ingredients;
  }

  public FoodStorage getStorage() {
    return storage;
  }

  public void setStorage(FoodStorage storage) {
    ExceptionHandling.nullStorage(storage);
    this.storage = storage;
  }

  // Private validation and setting methods
  private String validateAndSetName(String name) {
    ExceptionHandling.validateName(name);
    return name;
  }

  private String validateAndSetDescription(String description) {
    ExceptionHandling.validateName(description);
    return description;
  }

  private String validateAndSetProcess(String process) {
    ExceptionHandling.validateName(process);
    return process;
  }

  private Map<String, IngredientDetail> validateAndSetIngredients(Map<String, IngredientDetail> ingredients) {
    ExceptionHandling.validateIngredients(ingredients);
    return ingredients;
  }


  /**
   * Prints ingredients that are missing or insufficient to make the recipe.
   *
   * <p>For each ingredient, the method:
   * <ul>
   *   <li>Calculates the total available amount in {@link FoodStorage}.</li>
   *   <li>Determines the missing quantity if the available amount is insufficient.</li>
   *   <li>Prints the ingredient's name, missing amount, and unit in the format:
   *       {@code - IngredientName: Missing X.XX unit}.</li>
   * </ul>
   * <b>Example:</b>
   * If 200g of Spaghetti and 150ml of Sauce are required, but storage has only 100g and 100ml:
   * <pre>
   * - Spaghetti: Missing 100.00 g
   * - Tomato Sauce: Missing 50.00 ml
   * </pre>
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
   * Determines if the recipe can be made with the available ingredients in {@link FoodStorage}.
   *
   * <p>For each ingredient in the recipe:
   * <ul>
   *   <li>Calculates the total available amount from storage.</li>
   *   <li>Checks if the available amount is less than the required amount.</li>
   *   <li>Returns {@code false} if any ingredient is insufficient.</li>
   * </ul>
   * <b>Returns:</b> {@code true} if all ingredients are sufficient; {@code false} otherwise.
   */
  public boolean canMakeRecipe() {
    // Use Map.entry for control flow mechanisms (e.g. return true/false)
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
        "Recipe: %s%n" + "Description: %s%n" + "Process: %s%n" + "Ingredients:%n%s",
        nameOfRecipe,
        description,
        process,
        ingredientsTable
    );
  }
}
