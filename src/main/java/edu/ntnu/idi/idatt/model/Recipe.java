package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.utils.ExceptionHandling;
import java.util.Map;

/**
 * Represents a recipe for a dish or meal.
 * A recipe contains a name, description, cooking process,
 * and a list of ingredients with their required amounts.
 * It also interacts with a {@link FoodStorage}
 * to check if enough ingredients are available to prepare the dish.
 */
public class Recipe {

  private final String nameOfRecipe;
  private final String description;
  private final String process;
  private final Map<String, IngredientDetail> ingredients;
  public FoodStorage storage;

  /**
   * Constructs a Recipe object with the specified details.
   *
   * @param nameOfRecipe the name of the recipe
   * @param description  a brief description of the recipe
   * @param process      the step-by-step cooking process for the recipe
   * @param ingredients  a map of ingredients required for the recipe,
   *                     where the key is the ingredient name,
   *                     and the value is the required amount
   *
   * @throws IllegalArgumentException if the name, description, or process are invalid
   */

  // Suppressed line length check as breaking the method definition reduces readability
  @SuppressWarnings("checkstyle:LineLength")
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

  // Set-methods ---------------------------------------------------------

  /**
   * validates and sets the instance for recipe
   * This allows the recipe to check available ingredients against
   * the specified {@link FoodStorage}.
   *
   * @param storage the {@link FoodStorage} instance to associate with the recipe.
   * @throws IllegalArgumentException if the provided storage is null
   */
  public void setStorage(FoodStorage storage) {
    ExceptionHandling.nullStorage(storage);
    this.storage = storage;
  }

  /**
   * Validates and sets the name of the recipe.
   *
   * @param name the name of the recipe to validate
   * @return the validated name
   * @throws IllegalArgumentException if the name is null, empty, or invalid
   */
  private String validateAndSetName(String name) {
    ExceptionHandling.validateName(name);
    return name;
  }

  /**
   * Validates and sets the description of the recipe.
   *
   * @param description the description of the recipe to validate
   * @return the validated description
   * @throws IllegalArgumentException if the description is null, empty, or invalid
   */
  private String validateAndSetDescription(String description) {
    ExceptionHandling.validateName(description);
    return description;
  }

  /**
   * Validates and sets the process (cooking instructions) of the recipe.
   *
   * @param process the cooking process to validate
   * @return the validated process
   * @throws IllegalArgumentException if the process is null, empty, or invalid
   */
  private String validateAndSetProcess(String process) {
    ExceptionHandling.validateName(process);
    return process;
  }

  /**
   * Validates and sets the ingredients of the recipe.
   *
   * @param ingredients a map of ingredient names to their respective details
   * @return the validated map of ingredients
   * @throws IllegalArgumentException if the ingredients map is null, empty,
   *         or contains invalid data
   *
   */

  // Suppressed line length check as breaking the method definition reduces readability
  @SuppressWarnings("checkstyle:LineLength")
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
      double availableAmount = storage.findGroceriesByName(ingredientName).stream()
          .mapToDouble(Grocery::getAmount)
          .sum();

      if (availableAmount < requiredDetail.getAmount()) {
        double missingAmount = requiredDetail.getAmount() - availableAmount;
        System.out.printf("- %s: Missing %.2f %s%n",
            ingredientName,
            missingAmount,
            requiredDetail.getUnit());
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

      double availableAmount = storage.findGroceriesByName(ingredientName).stream()
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
