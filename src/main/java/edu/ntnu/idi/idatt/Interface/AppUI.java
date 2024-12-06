package edu.ntnu.idi.idatt.Interface;

import edu.ntnu.idi.idatt.Model.FoodStorage;
import edu.ntnu.idi.idatt.Model.Grocery;
import edu.ntnu.idi.idatt.Model.IngredientDetail;
import edu.ntnu.idi.idatt.Model.Recipe;
import edu.ntnu.idi.idatt.Model.RecipeBook;
import edu.ntnu.idi.idatt.Utils.InputValidation;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * User interface for the Grocery application.
 * Provides a menu-driven interface for interacting with properties.
 */
public class AppUI {

  private static final int ADD_GROCERY = 1;
  private static final int REMOVE_GROCERY = 2;
  private static final int GROCERIES_SORTED = 3;
  private static final int FIND_GROCERY_IN_STORAGE = 4;
  private static final int BEST_BEFORE_EXPIRY_DATE = 5;
  private static final int TOTAL_VALUE_OF_GROCERIES_IN_STORAGE = 6;
  private static final int MOVE_AND_VIEW_EXPIRED_GROCERIES = 7;
  private static final int TOTAL_VALUE_OF_EXPIRED_GROCERIES = 8;
  private static final int LIST_ALL_GROCERIES = 9;
  private static final int REGISTER_RECIPE = 10;
  private static final int CHECK_FOR_RECIPE = 11;
  private static final int CHECK_AVAILABLE_RECIPES = 12;
  private static final int EXIT = 0;

  private final Scanner scanner = new Scanner(System.in);
  private FoodStorage storage = null; // Initialize FoodStorage instance globally
  private RecipeBook recipeBook = null; // Initialize FoodStorage instance globally

  /**
   * Presents the menu for the user and awaits input from the user.
   * The menu choice selected by the user is returned.
   *
   * @return the menu choice as a positive number starting from 1.
   *         If 0 is returned, the user has entered an invalid value.
   */
  private int showMenu() {
    System.out.println("\n***** FOODWASTE APPLICTAION VERSION 0.1 *****\n");
    System.out.println("1. Add grocery");
    System.out.println("2. Remove grocery");
    System.out.println("3. Return a sorted storage (Alphabetically)");
    System.out.println("4. Find grocery in storage");
    System.out.println("5. Return groceries with best-before date before a given expiry date");
    System.out.println("6. return total value of ALL groceries in storage");
    System.out.println("7. view all expired groceries");
    System.out.println("8. return total value of ALL EXPIRED groceries in storage");
    System.out.println("9. List all groceries");
    System.out.println("10. register a new recipe to recipe-book");
    System.out.println("11. Check for a specific recipe");
    System.out.println("12. Check for recipes you can make with current storage:");
    System.out.println("0. Quit");

    int menuChoice = InputValidation.getValidInt("\nPlease enter a number between 1 and 12: ");
    return menuChoice;
  }

  public void start() {
    boolean finished = false;
    init();
    // The while-loop will run as long as the user has not selected to quit the application
    while (!finished) {
      int menuChoice = this.showMenu();
      switch (menuChoice) {
        case ADD_GROCERY -> {
          addGroceryToStorage();
        }

        case REMOVE_GROCERY -> {
          removeAmountOfGroceryFromStorage(); // MISSING EXCEPTION HANDLING REMEMBER THAT CUZ!!!!
        }

        case GROCERIES_SORTED -> {
          sortedStorage();
        }

        case FIND_GROCERY_IN_STORAGE -> {
          findGroceryInStorage();
        }

        case BEST_BEFORE_EXPIRY_DATE -> {
          bestBeforeExpiryDate();
        }

        case TOTAL_VALUE_OF_GROCERIES_IN_STORAGE -> {
          totalValueOfGroceries();
        }

        case MOVE_AND_VIEW_EXPIRED_GROCERIES -> {
          viewExpiredGroceries();
        }

        case TOTAL_VALUE_OF_EXPIRED_GROCERIES -> {
          totalValueOfExpiredGroceries();
        }

        case LIST_ALL_GROCERIES -> {
          showStorage();
        }

        case REGISTER_RECIPE -> {
          createRecipe();
        }

        case CHECK_FOR_RECIPE -> {
          checkForSpecificRecipe();
        }

        case CHECK_AVAILABLE_RECIPES -> {
          checkForAllAvailableRecipes();
        }

        case EXIT -> {
          System.out.println("Program exited succesfully");
          finished = true;
        }
      }
    }
  }

  private void addGroceryToStorage() {
    System.out.println("Enter the following: ");
    System.out.println("SIDENOTE: default display of amount is in g (grams) for dry units and l (litres) for liquid units");

    try {
      String groceryName = InputValidation.getValidString("\nPlease enter Grocery name: ");

      double groceryPrice = InputValidation.getValidDouble("Price of Grocery: ", true);

      double groceryAmount = InputValidation.getValidDouble("Amount of grocery: ", false);

      String groceryUnit = InputValidation.getValidUnit("Corresponding Unit: ");

      LocalDate groceryExpiryDate = InputValidation.getValidDate("Expiry Date: ");

      // Create and register the grocery
      Grocery registeredGrocery = new Grocery(groceryName, groceryPrice, groceryAmount, groceryUnit, groceryExpiryDate);
      storage.registerToStorage(registeredGrocery);

      // Filter and remove expired groceries from storage
      storage.filterAndGroupExpiredGroceries();
      storage.removeExpiredGroceries();

      if (groceryExpiryDate.isBefore(LocalDate.now())) {
        System.out.println("WARNING: Added expired Grocery, moved to expired storage!");
      } else {
        System.out.println("Grocery added successfully!");
      }

    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("An unexpected error occurred. Please try again.");
    }
  }

  private void removeAmountOfGroceryFromStorage() {
    String groceryToRemove = InputValidation.getValidItemToRemove("\nPlease enter Grocery name: ", storage.sortGroceries()); // use sort groceries here to return a Map
    String desiredUnit = InputValidation.getValidUnit("\nPlease enter the desired unit for removal: ");
    double amountToRemove = InputValidation.getValidDouble("\nPlease enter amount to remove: ", false);
    storage.removeAmountFromStorage(groceryToRemove, amountToRemove, desiredUnit);
  }

  private void sortedStorage() {
    System.out.println(storage.toString(true));
  }

  private void findGroceryInStorage() {
    String groceryToFind = InputValidation.getValidString("Please enter the name of the grocery:\n");
    storage.findInStorage(groceryToFind, false);
    storage.findInStorage(groceryToFind, true);
  }

  private void bestBeforeExpiryDate() {
    LocalDate bestBeforeDate = InputValidation.getValidDate("Please enter a date you want to check for (dd-mm-yyyy)");
    System.out.println(storage.bestBefore(bestBeforeDate));
  }

  private void totalValueOfGroceries() {
    System.out.println("The total monetary value of all groceries: \n" + storage.totalValueOfGroceries() + "kr");
  }

  private void viewExpiredGroceries() {
    System.out.println(storage.displayExpiredGroceries());
  }

  private void totalValueOfExpiredGroceries() {
    System.out.println("Total monetary value of expired groceries in storage: \n" + storage.totalValueOfExpiredGroceries() + "kr");
  }

  private void showStorage() {
    System.out.println(storage.toString(false));
  }

  private void createRecipe() {
    System.out.println("Create a new recipe:");

    String recipeName = InputValidation.getValidString("Enter the recipe name: ");
    String recipeDescription = InputValidation.getValidString("Enter a short description: ");
    String recipeProcess = InputValidation.getValidString("Enter the cooking process: ");

    // input for ingredients
    Map<String, IngredientDetail> recipeIngredients = new HashMap<>();
    boolean finishedAdding = false;

    while (!finishedAdding) {
      String ingredientName = InputValidation.getValidString("Insert ingredient name: ");
      double ingredientAmount = InputValidation.getValidDouble("Insert amount: ", false);
      String ingredientUnit = InputValidation.getValidUnit("Insert unit (e.g., g, kg, l): ");

      recipeIngredients.put(ingredientName, new IngredientDetail(ingredientAmount, ingredientUnit));

      // Ask the user if they want to add more ingredients
      System.out.println("Do you want to add more ingredients? (Y/N)");
      String response = scanner.nextLine().trim().toLowerCase();
      finishedAdding = response.equals("n");
    }

    Recipe newRecipe = new Recipe(recipeName, recipeDescription, recipeProcess, recipeIngredients);

    try {
      recipeBook.addRecipe(newRecipe);
      System.out.println("Recipe \"" + recipeName + "\" has been successfully added to the RecipeBook!");
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  private void checkForSpecificRecipe() {
    Recipe recipe = InputValidation.getValidRecipe("Enter the name of the recipe you want to check:", recipeBook);

    // Link the FoodStorage to the recipe
    recipe.setStorage(storage);

    if (recipe.canMakeRecipe()) {
      System.out.println("Here is the recipe you requested:");
      System.out.println(recipe);
    } else {
      System.out.println("You don't have enough ingredients to make: " + recipe.getNameOfRecipe() + "\n");
      System.out.println("Missing ingredients:");
      recipe.getMissingIngredients();
    }
  }

  private void checkForAllAvailableRecipes() {
    if (recipeBook.getAvailableRecipes(storage).isEmpty()) {
      System.out.println("You do not have enough ingredients to make any recipes in the cookbook.");
    }

    // Fetch all recipes that can be made
    List<Recipe> availableRecipes = recipeBook.getAvailableRecipes(storage);

    if (availableRecipes.isEmpty()) {
      System.out.println("You do not have enough ingredients to make any recipes in the cookbook.");
    } else {
      System.out.println("Here are the recipes you can make with your current ingredients:\n");
      for (Recipe recipe : availableRecipes) {
        System.out.println(recipe); // Print full recipe details using the overridden `toString`
        System.out.println();
      }
    }
  }


  private void init() {
    storage = new FoodStorage();
    recipeBook = new RecipeBook();

    // DUMMY VALUES----------------

    // Groceries
    List<Grocery> groceryList = List.of(
        new Grocery("Milk", 15.0, 12.0, "l", LocalDate.of(2024, 12, 21)), // 1L = 1000g
        new Grocery("Milk", 15.0, 10.0, "l", LocalDate.of(2024, 12, 21)),
        new Grocery("Milk", 15.0, 12.0, "l", LocalDate.of(2024, 12, 21)),
        new Grocery("Milk", 15.0, 8.0, "l", LocalDate.of(2024, 11, 25)), // 2L = 2000g
        new Grocery("Milk", 15.0, 5.0, "l", LocalDate.of(2024, 12, 15)), // 1.5L = 1500g

        // Bread
        new Grocery("Bread", 25.0, 1000.0, "g", LocalDate.of(2024, 11, 25)), // 1 loaf = 1000g
        new Grocery("Bread", 25.0, 2000.0, "g", LocalDate.of(2024, 11, 28)), // 2 loaves = 2000g
        new Grocery("Bread", 25.0, 1000.0, "g", LocalDate.of(2024, 12, 1)),

        // Eggs
        new Grocery("Eggs", 5.0, 12.0, "stk", LocalDate.of(2024, 12, 15)), // Quantity in number of eggs, no change needed
        new Grocery("Eggs", 5.0, 6.0, "stk", LocalDate.of(2024, 12, 5)),

        // Chicken
        new Grocery("Chicken", 150.0, 1000.0, "g", LocalDate.of(2024, 11, 30)), // 1kg = 1000g
        new Grocery("Chicken", 150.0, 2000.0, "g", LocalDate.of(2024, 12, 10)), // 2kg = 2000g

        // Rice
        new Grocery("Rice", 20.0, 5000.0, "g", LocalDate.of(2025, 1, 10)), // 5kg = 5000g

        // Apples
        new Grocery("Apples", 30.0, 3000.0, "g", LocalDate.of(2024, 11, 28)), // 3kg = 3000g
        new Grocery("Apples", 30.0, 1500.0, "g", LocalDate.of(2024, 12, 5)), // 1.5kg = 1500g

        // Bananas
        new Grocery("Bananas", 20.0, 2000.0, "g", LocalDate.of(2024, 11, 30)), // 2kg = 2000g

        // Cheese
        new Grocery("Cheese", 80.0, 1000.0, "g", LocalDate.of(2025, 3, 10)), // 1kg = 1000g

        // Butter
        new Grocery("Butter", 50.0, 500.0, "g", LocalDate.of(2025, 5, 20)), // 0.5kg = 500g

        // Yogurt
        new Grocery("Yogurt", 10.0, 500.0, "g", LocalDate.of(2024, 12, 1)), // 0.5L = 500g
        new Grocery("Yogurt", 10.0, 1000.0, "g", LocalDate.of(2024, 12, 10)), // 1L = 1000g

        // Potatoes
        new Grocery("Potatoes", 15.0, 10000.0, "g", LocalDate.of(2025, 2, 15)), // 10kg = 10000g

        // Carrots
        new Grocery("Carrots", 12.0, 5000.0, "g", LocalDate.of(2025, 1, 5)), // 5kg = 5000g

        // Sugar
        new Grocery("Sugar", 25.0, 2000.0, "g", LocalDate.of(2025, 6, 1)), // 2kg = 2000g

        // Salt
        new Grocery("Salt", 10.0, 1000.0, "g", LocalDate.of(2026, 1, 1)), // 1kg = 1000g

        // Flour
        new Grocery("Flour", 40.0, 3000.0, "g", LocalDate.of(2025, 7, 30)) // 3kg = 3000g
    );

    for (Grocery g : groceryList) {
      storage.registerToStorage(g);
    }

    // automatically filters and removes expired groceries when initiated
    storage.filterAndGroupExpiredGroceries();
    storage.removeExpiredGroceries();

    // Recipes
    Map<String, IngredientDetail> spaghettiIngredients = new HashMap<>();
    spaghettiIngredients.put("Spaghetti", new IngredientDetail(500, "g"));
    spaghettiIngredients.put("Ground Beef", new IngredientDetail(300, "g"));
    spaghettiIngredients.put("Tomato Sauce", new IngredientDetail(200, "g"));

    Recipe spaghetti = new Recipe(
        "Spaghetti Bolognese",
        "Classic Italian pasta dish with meat sauce.",
        "Boil pasta. Cook beef. Mix with sauce. Serve.",
        spaghettiIngredients
    );

    recipeBook.addRecipe(spaghetti); // Add recipe to RecipeBook

    Map<String, IngredientDetail> pancakeIngredients = new HashMap<>();
    pancakeIngredients.put("Flour", new IngredientDetail(200, "g"));
    pancakeIngredients.put("Milk", new IngredientDetail(300, "ml"));
    pancakeIngredients.put("Eggs", new IngredientDetail(2, "stk"));

    Recipe pancakes = new Recipe(
        "Pancakes",
        "Delicious breakfast pancakes.",
        "Mix ingredients. Fry in a pan. Serve hot.",
        pancakeIngredients
    );

    recipeBook.addRecipe(pancakes);
  }
}
