package edu.ntnu.idi.idatt.Model;

import java.util.List;
import java.util.Map;

public class Recipe {

  private final String nameOfRecipe;
  private final String description;
  private final String process;
  private Map<String, Double> ingredients;
  public FoodStorage storage = new FoodStorage();

  /**
   * Constructor for creating a Recipe object.
   *
   * @param name        the name of the recipe
   * @param description a short description of the recipe
   * @param process     the process or instructions for the recipe
   * @param ingredients the list of ingredients required for the recipe
   */
  public Recipe(String name, String description, String process, Map<String, Double> ingredients) {
    this.nameOfRecipe = setName(name);
    this.description = setDescription(description);
    this.process = setProcess(process);
    this.ingredients = setIngredients(ingredients);
  }

  // Getters ------------------------------------------

  /**
   * Gets the name of the recipe.
   *
   * @return the name of the recipe
   */
  public String getNameOfRecipe() {
    return nameOfRecipe;
  }

  /**
   * Gets the description of the recipe.
   *
   * @return the description of the recipe
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets the cooking process or instructions of the recipe.
   *
   * @return the process of the recipe
   */
  public String getProcess() {
    return process;
  }

  /**
   * Gets the list of ingredients for the recipe.
   *
   * @return the list of ingredients
   */
  public Map<String, Double> getIngredients() {
    return ingredients;
  }

//SET-METHODS________________________________________________________________

  private String setName(String name) {
    return name;
  }

  private String setDescription(String description) {
    return description;
  }

  private String setProcess(String process) {
    return process;
  }

  private Map<String, Double> setIngredients(Map<String, Double> ingredients) {
    return ingredients;
  }

  //METHODS_______________________________________________________________

  //COME BACK TO THIS!!!!!!!!!!!!!!!!!!!!!!!!!!!

  public void enoughIngredientsInStorage(Map<String, Double> ingredients) {
    // Filter out ingredients that are insufficient
    List<String> insufficientIngredients = ingredients.entrySet().stream()
        .filter(entry -> {
          String name = entry.getKey();
          Double requiredAmount = entry.getValue();

          // Check if the storage does not have enough of the ingredient
          return storage.getGroceriesByName(name).stream()
              .mapToDouble(g -> g.getAmount())
              .sum() < requiredAmount;
        })
        .map(Map.Entry::getKey) // Extract the name of the ingredient
        .toList(); // Collect as a list

    if (insufficientIngredients.isEmpty()) {
      System.out.println("Du har nok ingredienser til å lage: " + nameOfRecipe);
    } else {
      System.out.println("You do not have enough of the following ingredients:");
      insufficientIngredients.forEach(System.out::println); // Print each insufficient ingredient
    }
  }

}


