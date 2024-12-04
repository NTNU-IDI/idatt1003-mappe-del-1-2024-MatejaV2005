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

  private final String nameOfRecipe;
  private final String description;
  private final String process;
  private Map<String, IngredientDetail> ingredients;
  public FoodStorage storage;

  /**
   * Constructs a Recipe object with the specified details.
   *
   * @param nameOfRecipe the name of the recipe
   * @param description  a brief description of the recipe
   * @param process      the step-by-step cooking process for the recipe
   * @param ingredients  a map of ingredients required for the recipe, where the key is the ingredient name,
   *                     and the value is the required amount
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

  // Getters ------------------------------------------

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

  // Setters ------------------------------------------
  /**
   * Sets the list of ingredients for the recipe.
   *
   * @param ingredients a map of ingredients, where the key is the ingredient name, and the value is the required amount
   * @return the validated ingredients map
   */
  private Map<String, IngredientDetail> setIngredients(Map<String, IngredientDetail> ingredients) {
    return ingredients;
  }

  public FoodStorage setStorage(FoodStorage storage) {
    this.storage = storage;
    return storage;
  }

  // Methods ------------------------------------------

  /**
   * Retrieves a list of ingredients that are insufficient in the storage.
   *
   * @param ingredients the map of required ingredients with their amounts
   * @return a list of ingredient names that are missing or insufficient
   */
  public List<String> getMissingIngredients(Map<String, Double> ingredients) {
    return ingredients.entrySet().stream()
        .filter(entry -> {
          String name = entry.getKey();
          Double requiredAmount = entry.getValue();

          // Check if the storage does not have enough of the ingredient
          return storage.getGroceriesByName(name).stream()
              .mapToDouble(Grocery::getAmount)
              .sum() < requiredAmount;
        })
        .map(Map.Entry::getKey) // Extract the name of the ingredient
        .toList(); // Collect as a list
  }

  public boolean canMakeRecipe() {
    // Iterate over all ingredients in the recipe
    for (Map.Entry<String, IngredientDetail> entry : ingredients.entrySet()) {
      String ingredientName = entry.getKey();
      IngredientDetail requiredDetail = entry.getValue();

      // Sum up the total amount of this ingredient in storage
      double availableAmount = storage.getGroceriesByName(ingredientName).stream()
          .mapToDouble(Grocery::getAmount)
          .sum();

      // Check if available amount is less than the required amount
      if (availableAmount < requiredDetail.getAmount()) {
        return false; // Not enough of this ingredient
      }
    }
    return true; // All ingredients are sufficient
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
   * Spaghetti            | 200.00
   * Ground Beef          | 300.00
   * Tomato Sauce         | 150.00
   * Onion                | 50.00
   * </pre>
   *
   * @return A formatted string representation of the Recipe object.
   */
  @Override
  public String toString() {
    StringBuilder ingredientsTable = new StringBuilder();
    ingredientsTable.append(String.format("%-20s | %-10s%n", "Ingredient", "Amount"));
    ingredientsTable.append(String.format("%s%n", "-".repeat(33)));

    // Legg til ingredienser i tabellformat
    for (Map.Entry<String, IngredientDetail> entry : ingredients.entrySet()) {
      ingredientsTable.append(String.format("%-20s | %-10.2f%n", entry.getKey(), entry.getValue()));
    }

    // Kombiner oppskriftens detaljer med ingredienslisten
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
