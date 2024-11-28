package edu.ntnu.idi.idatt.Interface;

import edu.ntnu.idi.idatt.Model.FoodStorage;
import edu.ntnu.idi.idatt.Model.Grocery;
import edu.ntnu.idi.idatt.Utils.ExceptionHandling;
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
          RemoveGroceryFromStorage();
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

        }
      }
    }
  }

  private void AddGroceryToStorage() {
    System.out.println("Enter the following: |Name of Grocery|  |Price of Grocery|  |Amount of grocery|  |Corresponding Unit|  |Expiry Date|");

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

  private void RemoveGroceryFromStorage() {
    String groceryToRemove = InputValidation.getValidString("\nPlease enter Grocery name: ");
    Double amountToRemove = InputValidation.getValidDouble("\nPlease enter amount to remove: ", false);
    storage.removeAmountFromStorage(groceryToRemove, amountToRemove);
  }

  private void SortedStorage() {
    System.out.println(storage.sortGroceries());
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
    System.out.println(storage.moveToExpiredGroceries());
  }

  private void TotalValueOfExpiredGroceries() {
    System.out.println("Total monetary value of expired groceries in storage: \n" +storage.TotalValueOfExpiredGroceries() +"kr");
  }

  private void ShowStorage() {
    System.out.println(storage);
  }

  private void init() {
    storage = new FoodStorage();

    //DUMMY VALUES----------------
    List<Grocery> groceryList = List.of(
        new Grocery("Milk", 15.0, 1.0, "l", LocalDate.of(2024, 12, 21)),
        new Grocery("Milk", 15.0, 1.0, "l", LocalDate.of(2024, 12, 21)),
        new Grocery("Milk", 15.0, 1.0, "l", LocalDate.of(2024, 12, 21)),
        new Grocery("Milk", 15.0, 2.0, "l", LocalDate.of(2024, 11, 25)),
        new Grocery("Milk", 15.0, 1.5, "l", LocalDate.of(2024, 12, 15)),

        new Grocery("Bread", 25.0, 1.0, "stk", LocalDate.of(2024, 11, 25)),
        new Grocery("Bread", 25.0, 2.0, "stk", LocalDate.of(2024, 11, 28)),
        new Grocery("Bread", 25.0, 1.0, "stk", LocalDate.of(2024, 12, 1)),

        new Grocery("Eggs", 5.0, 12.0, "stk", LocalDate.of(2024, 12, 15)),
        new Grocery("Eggs", 5.0, 6.0, "stk", LocalDate.of(2024, 12, 5)),

        new Grocery("Chicken", 150.0, 1.0, "kg", LocalDate.of(2024, 11, 30)),
        new Grocery("Chicken", 150.0, 2.0, "kg", LocalDate.of(2024, 12, 10)),

        new Grocery("Rice", 20.0, 5.0, "kg", LocalDate.of(2025, 1, 10)),

        new Grocery("Apples", 30.0, 3.0, "kg", LocalDate.of(2024, 11, 28)),
        new Grocery("Apples", 30.0, 1.5, "kg", LocalDate.of(2024, 12, 5)),

        new Grocery("Bananas", 20.0, 2.0, "kg", LocalDate.of(2024, 11, 30)),

        new Grocery("Cheese", 80.0, 1.0, "kg", LocalDate.of(2025, 3, 10)),

        new Grocery("Butter", 50.0, 0.5, "kg", LocalDate.of(2025, 5, 20)),

        new Grocery("Yogurt", 10.0, 0.5, "l", LocalDate.of(2024, 12, 1)),
        new Grocery("Yogurt", 10.0, 1.0, "l", LocalDate.of(2024, 12, 10)),

        new Grocery("Potatoes", 15.0, 10.0, "kg", LocalDate.of(2025, 2, 15)),

        new Grocery("Carrots", 12.0, 5.0, "kg", LocalDate.of(2025, 1, 5)),

        new Grocery("Sugar", 25.0, 2.0, "kg", LocalDate.of(2025, 6, 1)),

        new Grocery("Salt", 10.0, 1.0, "kg", LocalDate.of(2026, 1, 1)),

        new Grocery("Flour", 40.0, 3.0, "kg", LocalDate.of(2025, 7, 30))
    );

    for (Grocery g : groceryList) {
      storage.registerToStorage(g);
    }
  }
}

