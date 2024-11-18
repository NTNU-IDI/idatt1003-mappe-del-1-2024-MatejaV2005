package edu.ntnu.idi.idatt.Interface;

import edu.ntnu.idi.idatt.FoodStorage;
import edu.ntnu.idi.idatt.Grocery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * User interface for the Grocery application.
 * Provides a menu-driven interface for interacting with properties.
 */
public class GroceryUI {
  private static final int ADD_GROCERY = 1;
  private static final int REMOVE_GROCERY = 2;
  private static final int GROCERIES_SORTED = 3;
  private static final int FIND_GROCERY_IN_STORAGE = 4;
  private static final int BEST_BEFORE_EXPIRY_DATE = 5;
  private static final int TOTAL_VALUE_OF_GROCERIES_IN_STORAGE = 6;
  private static final int LIST_ALL_GROCERIES = 7;
  private static final int EXIT = 0;

  private final Scanner scanner = new Scanner(System.in);
  private final FoodStorage storage = new FoodStorage();

  /**
   * Presents the menu for the user and awaits input from the user.
   * The menu choice selected by the user is returned.
   *
   * @return the menu choice as a positive number starting from 1.
   *         If 0 is returned, the user has entered an invalid value.
   */
  private int showMenu() {
    System.out.println("\n***** Property Register Application v0.1 *****\n");
    System.out.println("1. Add property");
    System.out.println("2. List all properties");
    System.out.println("3. Find property by ID");
    System.out.println("4. List properties by farm number");
    System.out.println("5. Calculate average area");
    System.out.println("6. Delete property");
    System.out.println("7. Show total number of properties");
    System.out.println("9. Quit");
    System.out.println("\nPlease enter a number between 1 and 9.\n");

    int menuChoice = 0;
    try {
      menuChoice = Integer.parseInt(scanner.nextLine().trim());
    } catch (NumberFormatException e) {
      System.out.println("Invalid input. Please enter a number.");
    }

    return menuChoice;
  }


  public void start() {
    boolean running = true;
    init();

  }

  public void init() {
    //DUMMY VALUES----------------
    Grocery milk1 = new Grocery("Milk", 15.0, 1.0, "liters", LocalDate.of(2023, 11, 20));
    Grocery milk2 = new Grocery("Milk", 15.0, 2.0, "liters", LocalDate.of(2023, 12, 1));
    Grocery milk3 = new Grocery("Milk", 15.0, 1.5, "liters", LocalDate.of(2023, 12, 15));

    Grocery bread1 = new Grocery("Bread", 25.0, 1.0, "pieces", LocalDate.of(2023, 11, 18));
    Grocery bread2 = new Grocery("Bread", 25.0, 2.0, "pieces", LocalDate.of(2023, 11, 22));
    Grocery bread3 = new Grocery("Bread", 25.0, 1.0, "pieces", LocalDate.of(2023, 11, 25));

    Grocery eggs1 = new Grocery("Eggs", 5.0, 12.0, "pieces", LocalDate.of(2023, 12, 15));
    Grocery eggs2 = new Grocery("Eggs", 5.0, 6.0, "pieces", LocalDate.of(2023, 12, 5));

    Grocery chicken1 = new Grocery("Chicken", 150.0, 1.0, "kg", LocalDate.of(2023, 11, 25));
    Grocery chicken2 = new Grocery("Chicken", 150.0, 2.0, "kg", LocalDate.of(2023, 12, 10));

    Grocery rice = new Grocery("Rice", 20.0, 5.0, "kg", LocalDate.of(2025, 1, 1));

    Grocery apples1 = new Grocery("Apples", 30.0, 3.0, "kg", LocalDate.of(2023, 11, 28));
    Grocery apples2 = new Grocery("Apples", 30.0, 1.5, "kg", LocalDate.of(2023, 12, 5));

    Grocery bananas = new Grocery("Bananas", 20.0, 2.0, "kg", LocalDate.of(2023, 11, 22));

    Grocery cheese = new Grocery("Cheese", 80.0, 1.0, "kg", LocalDate.of(2024, 3, 10));

    Grocery butter = new Grocery("Butter", 50.0, 0.5, "kg", LocalDate.of(2024, 5, 20));

    Grocery yogurt1 = new Grocery("Yogurt", 10.0, 0.5, "liters", LocalDate.of(2023, 12, 1));
    Grocery yogurt2 = new Grocery("Yogurt", 10.0, 1.0, "liters", LocalDate.of(2023, 12, 10));

    Grocery potatoes = new Grocery("Potatoes", 15.0, 10.0, "kg", LocalDate.of(2024, 2, 15));

    Grocery carrots = new Grocery("Carrots", 12.0, 5.0, "kg", LocalDate.of(2024, 1, 5));

    Grocery sugar = new Grocery("Sugar", 25.0, 2.0, "kg", LocalDate.of(2025, 6, 1));

    Grocery salt = new Grocery("Salt", 10.0, 1.0, "kg", LocalDate.of(2026, 1, 1));

    Grocery flour = new Grocery("Flour", 40.0, 3.0, "kg", LocalDate.of(2024, 7, 30));

    List<Grocery> groceryList = List.of(
        milk1, milk2, milk3,
        bread1, bread2, bread3,
        eggs1, eggs2,
        chicken1, chicken2,
        rice,
        apples1, apples2,
        bananas,
        cheese,
        butter,
        yogurt1, yogurt2,
        potatoes,
        carrots,
        sugar,
        salt,
        flour
    );

    for (Grocery g : groceryList) {
      storage.registerToStorage(g);
    }
  }
}

