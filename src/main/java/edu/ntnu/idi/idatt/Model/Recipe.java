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
  private Map<String, Double> ingredients;
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
  public Recipe(String nameOfRecipe, String description, String process, Map<String, Double> ingredients) {
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
  public Map<String, Double> getIngredients() {
    return ingredients;
  }

  // Setters ------------------------------------------
  /**
   * Sets the list of ingredients for the recipe.
   *
   * @param ingredients a map of ingredients, where the key is the ingredient name, and the value is the required amount
   * @return the validated ingredients map
   */
  private Map<String, Double> setIngredients(Map<String, Double> ingredients) {
    return ingredients;
  }

  // Methods ------------------------------------------

  /**
   * Checks if there are enough ingredients available in the {@link FoodStorage} to prepare the recipe.
   * It prints the result, listing any insufficient ingredients by name.
   *
   * @param ingredients the map of required ingredients, where the key is the ingredient name,
   *                    and the value is the required amount
   */
  public void enoughIngredientsInStorage(Map<String, Double> ingredients) {
    // Filter out ingredients that are insufficient
    List<String> insufficientIngredients = ingredients.entrySet().stream()
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

    if (insufficientIngredients.isEmpty()) {
      System.out.println("You have enough ingredients to make: " + nameOfRecipe);
    } else {
      System.out.println("You do not have enough of the following ingredients:");
      insufficientIngredients.forEach(System.out::println); // Print each insufficient ingredient
    }
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
    for (Map.Entry<String, Double> entry : ingredients.entrySet()) {
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
