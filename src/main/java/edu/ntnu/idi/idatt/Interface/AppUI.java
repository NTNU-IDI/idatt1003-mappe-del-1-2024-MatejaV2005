package edu.ntnu.idi.idatt.Interface;

import edu.ntnu.idi.idatt.Model.FoodStorage;
import edu.ntnu.idi.idatt.Model.Grocery;
import edu.ntnu.idi.idatt.Utils.InputValidation;
import java.time.LocalDate;
import java.util.List;
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
  private static final int VIEW_EXPIRED_GROCERIES = 7;
  private static final int TOTAL_VALUE_OF_EXPIRED_GROCERIES = 8;
  private static final int LIST_ALL_GROCERIES = 9;
  private static final int EXIT = 0;

  private final Scanner scanner = new Scanner(System.in);
  private FoodStorage storage = null;


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
    System.out.println("7. View all expired groceries");
    System.out.println("8. return total value of ALL EXPIRED groceries in storage");
    System.out.println("9. List all groceries");
    System.out.println("0. Quit");

    int menuChoice = InputValidation.getValidInt("\nPlease enter a number between 1 and 9: ");
    return menuChoice;
  }


  public void start() {
    boolean finished = false;
    init();
    //The	while-loop will run as long as the user has not selected to quit the application
    while (!finished) {
      int menuChoice = this.showMenu();
      switch (menuChoice) {
        case ADD_GROCERY -> {
          AddGroceryToStorage();
        }

        case REMOVE_GROCERY -> {
          RemoveAmountOfGroceryFromStorage();
        }


        case GROCERIES_SORTED -> {
          SortedStorage();
        }


        case FIND_GROCERY_IN_STORAGE -> {
          FindGroceryInStorage();
        }


        case BEST_BEFORE_EXPIRY_DATE -> {
          BestBeforeExpiryDate();
        }

        case TOTAL_VALUE_OF_GROCERIES_IN_STORAGE -> {
          TotalValueOfGroceries();
        }


        case VIEW_EXPIRED_GROCERIES -> {
          ViewExpiredGroceries();
        }

        case TOTAL_VALUE_OF_EXPIRED_GROCERIES -> {
          TotalValueOfExpiredGroceries();
        }

        case LIST_ALL_GROCERIES -> {
          ShowStorage();
        }


        case EXIT -> {
          finished = true;
        }
      }
    }
  }

  private void AddGroceryToStorage() {
    System.out.println("Enter the following: |Name of Grocery|  |Price of Grocery|  |Amount of grocery|  |Corresponding Unit|  |Expiry Date|");
    System.out.println("SIDENOTE: default display of amount is in grams for dry units");

    try {
      String groceryName = InputValidation.getValidString("\nPlease enter Grocery name: ");

      double groceryPrice = InputValidation.getValidDouble("Price of Grocery: ", true);

      double groceryAmount = InputValidation.getValidDouble("Amount of grocery: ", false);

      String groceryUnit = InputValidation.getValidUnit("Corresponding Unit: ");

      LocalDate groceryExpiryDate = InputValidation.getValidDate("Expiry Date: ");

      // Create and register the grocery
      Grocery registeredGrocery = new Grocery(groceryName, groceryPrice, groceryAmount, groceryUnit, groceryExpiryDate);
      storage.registerToStorage(registeredGrocery);
      System.out.println("Grocery added successfully!");

    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("An unexpected error occurred. Please try again.");
    }
  }

  private void RemoveAmountOfGroceryFromStorage() {
    String groceryToRemove = InputValidation.getValidString("\nPlease enter Grocery name: ");
    String desiredUnit = InputValidation.getValidString("\nPlease enter the desired unit for removal: ");
    double amountToRemove = InputValidation.getValidDouble("\nPlease enter amount to remove: ", false);
    storage.removeAmountFromStorage(groceryToRemove, amountToRemove, desiredUnit);
  }

  private void SortedStorage() {
    System.out.println(storage.toString(true));
  }

  private void FindGroceryInStorage() {
    String groceryToFind = InputValidation.getValidString("Please enter the name of the grocery:\n");
    storage.inStorage(groceryToFind);
  }

  private void BestBeforeExpiryDate() {
    LocalDate bestBeforeDate = InputValidation.getValidDate("Please enter a date you want to check for (dd-mm-yyyy)");
    System.out.println(storage.bestBefore(bestBeforeDate));
  }

  private void TotalValueOfGroceries() {
    System.out.println("The total monetary value of all groceries: \n" +storage.TotalValueOfGroceries() + "kr");
  }

  private void ViewExpiredGroceries() {
    System.out.println(storage.DisplayExpiredGroceries());
    storage.removeExpiredGroceries();
  }

  private void TotalValueOfExpiredGroceries() {
    System.out.println("Total monetary value of expired groceries in storage: \n" +storage.TotalValueOfExpiredGroceries() +"kr");
  }

  private void ShowStorage() {
    System.out.println(storage.toString(false));
  }

  private void init() {
    storage = new FoodStorage();
    //TODO: ADD METHOD SO THAT LARGER QUANTITIES USE "KG", SMALLER USE "g"
    //DUMMY VALUES----------------
    List<Grocery> groceryList = List.of(
        new Grocery("Milk", 15.0, 12.0, "l", LocalDate.of(2024, 12, 21)),   // 1L = 1000g
        new Grocery("Milk", 15.0, 10.0, "l", LocalDate.of(2024, 12, 21)),
        new Grocery("Milk", 15.0, 12.0, "l", LocalDate.of(2024, 12, 21)),
        new Grocery("Milk", 15.0, 8.0, "l", LocalDate.of(2024, 11, 25)),   // 2L = 2000g
        new Grocery("Milk", 15.0, 5.0, "l", LocalDate.of(2024, 12, 15)),   // 1.5L = 1500g

        // Bread
        new Grocery("Bread", 25.0, 1000.0, "g", LocalDate.of(2024, 11, 25)),   // 1 loaf = 1000g
        new Grocery("Bread", 25.0, 2000.0, "g", LocalDate.of(2024, 11, 28)),   // 2 loaves = 2000g
        new Grocery("Bread", 25.0, 1000.0, "g", LocalDate.of(2024, 12, 1)),

        // Eggs
        new Grocery("Eggs", 5.0, 12.0, "stk", LocalDate.of(2024, 12, 15)),    // Quantity in number of eggs, no change needed
        new Grocery("Eggs", 5.0, 6.0, "stk", LocalDate.of(2024, 12, 5)),

        // Chicken
        new Grocery("Chicken", 150.0, 1000.0, "g", LocalDate.of(2024, 11, 30)), // 1kg = 1000g
        new Grocery("Chicken", 150.0, 2000.0, "g", LocalDate.of(2024, 12, 10)), // 2kg = 2000g

        // Rice
        new Grocery("Rice", 20.0, 5000.0, "g", LocalDate.of(2025, 1, 10)),  // 5kg = 5000g

        // Apples
        new Grocery("Apples", 30.0, 3000.0, "g", LocalDate.of(2024, 11, 28)), // 3kg = 3000g
        new Grocery("Apples", 30.0, 1500.0, "g", LocalDate.of(2024, 12, 5)),  // 1.5kg = 1500g

        // Bananas
        new Grocery("Bananas", 20.0, 2000.0, "g", LocalDate.of(2024, 11, 30)), // 2kg = 2000g

        // Cheese
        new Grocery("Cheese", 80.0, 1000.0, "g", LocalDate.of(2025, 3, 10)),  // 1kg = 1000g

        // Butter
        new Grocery("Butter", 50.0, 500.0, "g", LocalDate.of(2025, 5, 20)),  // 0.5kg = 500g

        // Yogurt
        new Grocery("Yogurt", 10.0, 500.0, "g", LocalDate.of(2024, 12, 1)),  // 0.5L = 500g
        new Grocery("Yogurt", 10.0, 1000.0, "g", LocalDate.of(2024, 12, 10)), // 1L = 1000g

        // Potatoes
        new Grocery("Potatoes", 15.0, 10000.0, "g", LocalDate.of(2025, 2, 15)), // 10kg = 10000g

        // Carrots
        new Grocery("Carrots", 12.0, 5000.0, "g", LocalDate.of(2025, 1, 5)),  // 5kg = 5000g

        // Sugar
        new Grocery("Sugar", 25.0, 2000.0, "g", LocalDate.of(2025, 6, 1)),  // 2kg = 2000g

        // Salt
        new Grocery("Salt", 10.0, 1000.0, "g", LocalDate.of(2026, 1, 1)),  // 1kg = 1000g

        // Flour
        new Grocery("Flour", 40.0, 3000.0, "g", LocalDate.of(2025, 7, 30))  // 3kg = 3000g
    );

    for (Grocery g : groceryList) {
      storage.registerToStorage(g);
    }
  }
}

