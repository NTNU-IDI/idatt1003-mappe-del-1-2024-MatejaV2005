package edu.ntnu.idi.idatt.Interface;

import edu.ntnu.idi.idatt.Model.FoodStorage;
import edu.ntnu.idi.idatt.Model.Grocery;
import java.time.LocalDate;
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
    System.out.println("\nPlease enter a number between 1 and 9.\n");

    int menuChoice = 0;
    try {
      menuChoice = Integer.parseInt(scanner.nextLine().trim());
    } catch (NumberFormatException e) {
      System.out.println("Invalid input. Please enter a following numebr (1-9).");
    }

    return menuChoice;
  }


  public void start() {
    boolean finished = false;
    init();
    //	The	while-loop	will	run	as	long	as	the	user	has	not	selected
    //	to	quit	the	application
    while (!finished) {
      int menuChoice = this.showMenu();
      switch (menuChoice) {
        case ADD_GROCERY:
          //TODO
          try {
            //Grocery new_crocegery = new Grocery("pass");
          } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
          }

        case REMOVE_GROCERY:
          //TODO

        case GROCERIES_SORTED:
          //TODO

        case FIND_GROCERY_IN_STORAGE:
          //TODO

        case BEST_BEFORE_EXPIRY_DATE:
          //TODO

        case TOTAL_VALUE_OF_GROCERIES_IN_STORAGE:
          //TODO

        case VIEW_EXPIRED_GROCERIES:
          //TODO

        case TOTAL_VALUE_OF_EXPIRED_GROCERIES:
          //TODO

        case LIST_ALL_GROCERIES:
          System.out.println(storage);

        case EXIT:
          //TODO
      }
    }
  }

  public void init() {
    storage = new FoodStorage();

    //DUMMY VALUES----------------
    List<Grocery> groceryList = List.of(
        new Grocery("Milk", 15.0, 1.0, "l", LocalDate.of(2024, 12, 21)),
        new Grocery("Milk", 15.0, 1.0, "l", LocalDate.of(2024, 12, 21)),
        new Grocery("Milk", 15.0, 1.0, "l", LocalDate.of(2024, 12, 21)),
        new Grocery("Milk", 15.0, 2.0, "l", LocalDate.of(2024, 11, 25)),
        new Grocery("Milk", 15.0, 1.5, "l", LocalDate.of(2024, 12, 15)),

        new Grocery("Bread", 25.0, 1.0, "Stk", LocalDate.of(2024, 11, 25)),
        new Grocery("Bread", 25.0, 2.0, "Stk", LocalDate.of(2024, 11, 28)),
        new Grocery("Bread", 25.0, 1.0, "Stk", LocalDate.of(2024, 12, 1)),

        new Grocery("Eggs", 5.0, 12.0, "Stk", LocalDate.of(2024, 12, 15)),
        new Grocery("Eggs", 5.0, 6.0, "Stk", LocalDate.of(2024, 12, 5)),

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

