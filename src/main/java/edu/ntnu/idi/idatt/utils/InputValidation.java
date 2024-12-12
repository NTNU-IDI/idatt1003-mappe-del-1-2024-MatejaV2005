package edu.ntnu.idi.idatt.utils;

import edu.ntnu.idi.idatt.model.Grocery;
import edu.ntnu.idi.idatt.model.Recipe;
import edu.ntnu.idi.idatt.model.RecipeBook;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Utility class for validating user input in a text-based user interface.
 *
 * <p>Provides static methods to validate and parse various input types, such as
 * strings, numbers, dates, and application-specific objects like groceries and recipes.
 * Ensures input meets required formats and constraints, with retry mechanisms for invalid input.
 *
 * <p>Key features include:
 * <ul>
 *   <li>Validation of numerical input and dates.</li>
 *   <li>Compatibility checks for units and storage items.</li>
 *   <li>Validation of groceries, recipes, and recipe books.</li>
 * </ul>
 *
 * <p>Enhances application robustness by ensuring all user inputs are valid and consistent.
 */

public class InputValidation {
  static Scanner sc = new Scanner(System.in);

  /**
   * Prompts the user to input an integer and validates the input.
   *
   * <p>Displays the provided prompt to the user and ensures the input is a valid integer.
   * If the input is invalid, the user is prompted again until a valid integer is provided.
   *
   * @param prompt the message displayed to the user
   * @return the valid integer input from the user
   */

  public static int getValidInt(String prompt) {
    System.out.println(prompt);
    String input = sc.nextLine();
    int output;

    try {
      output = Integer.parseInt(input);
      return output;
    } catch (NumberFormatException e) {
      System.out.println("Input must be an integer");
      return getValidInt(prompt);
    } catch (Exception e) {
      System.out.println("Unknown error " + e.getMessage());
      return getValidInt(prompt);
    }
  }

  /**
   * Prompts the user to input a double value and validates the input.
   *
   * <p>Displays the prompt to the user and checks if the value is valid.
   * If the input represents a price, it is validated using price constraints.
   * Otherwise, it is validated as a general amount.
   *
   * <p>If the input is invalid, the user is prompted again until a valid value is provided.
   *
   * @param prompt the message displayed to the user
   * @param isPrice whether the value represents a price or a general amount
   * @return the valid double input from the user
   */

  public static double getValidDouble(String prompt, boolean isPrice) {
    System.out.println(prompt);
    String input = sc.nextLine();
    double output;

    try {
      output = Double.parseDouble(input);
      if (isPrice) {
        ExceptionHandling.validatePrice(output);
      } else {
        ExceptionHandling.validateAmount(output);

      }
      return output;
    } catch (NumberFormatException e) {
      System.out.println("error: Input must be a valid number");
      return getValidDouble(prompt, isPrice);
    } catch (IllegalArgumentException e) {
      System.out.println("error: " + e.getMessage());
      return getValidDouble(prompt, isPrice);
    }
  }

  /**
   * Prompts the user to input a valid string and validates it.
   *
   * <p>Displays the provided prompt to the user and ensures the string meets
   * specified requirements, such as not being null, empty, or numerical.
   * If invalid, the user is prompted again until a valid string is provided.
   *
   * @param prompt the message displayed to the user
   * @return the valid string input from the user
   */

  public static String getValidString(String prompt) {
    System.out.println(prompt);
    String input = sc.nextLine().trim();
    String output;

    try {
      output = input;
      ExceptionHandling.validateName(output);
      return output;
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      return getValidString(prompt);
    }
  }

  /**
   * Prompts the user to input a valid unit and validates the input.
   *
   * <p>Displays the prompt and checks if the unit is recognized and valid.
   * If invalid, the user is prompted again until a valid unit is provided.
   *
   * @param prompt the message displayed to the user
   * @return the valid unit as a string
   */

  public static String getValidUnit(String prompt) {
    System.out.println(prompt);
    String input = sc.nextLine().trim();
    String output;

    try {
      output = input;
      ExceptionHandling.validateUnit(output);
      UnitConverter.convertUnitAmount(1, output);
      return output;
    } catch (IllegalArgumentException e) {
      System.out.println("error: " + e.getMessage());
      return getValidUnit(prompt);
    }
  }

  /**
   * Prompts the user to input a date in the format "dd-MM-yyyy" and validates it.
   *
   * <p>If the input is not a valid date or does not match the required format,
   * the user is prompted again until a valid date is provided.
   *
   * @param prompt the message displayed to the user
   * @return the valid date as a {@code LocalDate} object
   */

  public static LocalDate getValidDate(String prompt) {
    System.out.println(prompt);
    String dateString = sc.nextLine();
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    LocalDate output;

    try {
      output = LocalDate.parse(dateString, dateFormat);
      ExceptionHandling.validateExpiryDate(output);
      return output;
    } catch (DateTimeParseException e) {
      System.out.println("Invalid date format.");
      return getValidDate(prompt);
    }
  }

  /**
   * Prompts the user to input the name of a grocery item and validates its existence in storage.
   *
   * <p>If the item does not exist, the user is prompted again until a valid item name is provided.
   *
   * @param prompt the message displayed to the user
   * @param storage the map of storage containing grocery items
   * @return the name of a valid grocery item
   */

  public static String getValidItemToRemove(String prompt, Map<String, List<Grocery>> storage) {
    System.out.println(prompt);
    String input = sc.nextLine().trim();

    try {
      ExceptionHandling.validateStorageContainsItem(storage, input);
      return input;
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
      return getValidItemToRemove(prompt, storage);
    }
  }

  /**
   * Prompts the user to input a compatible unit for a specific grocery item and validates it.
   *
   * @param prompt the message displayed to the user
   * @param groceryName the name of the grocery item
   * @param storage the map of storage containing grocery items
   * @return the valid compatible unit as a string
   */

  // Suppressed line length check as breaking the method definition reduces readability
  @SuppressWarnings("checkstyle:LineLength")
  public static String getValidCompatibleUnit(String prompt, String groceryName, Map<String, List<Grocery>> storage) {
    System.out.println(prompt);
    String inputUnit = sc.nextLine().trim();

    try {
      ExceptionHandling.validateUnitCompatibility(inputUnit, groceryName, storage);
      return inputUnit;
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
      return getValidCompatibleUnit(prompt, groceryName, storage);
    }
  }

  /**
   * Prompts the user to input a valid amount to remove for a specific grocery item.
   *
   * @param prompt the message displayed to the user
   * @param storage the map of storage containing grocery items
   * @param itemName the name of the grocery item
   * @param unit the unit of the amount to remove
   * @return the valid amount to remove as a double
   */

  // Suppressed line length check as breaking the method definition reduces readability
  @SuppressWarnings("checkstyle:LineLength")
  public static double getValidAmountToRemove(String prompt, Map<String, List<Grocery>> storage, String itemName, String unit) {
    System.out.println(prompt);
    String input = sc.nextLine().trim();

    try {
      double amount = Double.parseDouble(input);
      double standardizedAmount = UnitConverter.convertUnitAmount(amount, unit);
      ExceptionHandling.validateAmountToRemove(storage, standardizedAmount, itemName);
      ExceptionHandling.validateAmount(standardizedAmount);
      return amount;

    } catch (NumberFormatException e) {
      System.out.println("Error: Amount must be a valid number.");
      return getValidAmountToRemove(prompt, storage, itemName, unit);
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
      return getValidAmountToRemove(prompt, storage, itemName, unit);
    }
  }

  /**
   * Prompts the user to input the name of a recipe and validates its existence in the recipe book.
   *
   * @param prompt the message displayed to the user
   * @param recipebook the recipe book to check
   * @return the valid recipe, or {@code null} if the recipe does not exist
   */

  public static Recipe getValidRecipe(String prompt, RecipeBook recipebook) {
    System.out.println(prompt);
    String recipeName = sc.nextLine();

    Recipe toCheck = recipebook.getRecipe(recipeName);
    if (toCheck == null) {
      System.out.println("error: This recipe doesnt exist in your recipe-book.");
      return null;
    }
    return toCheck;
  }

  /**
   * Validates a user's input for a yes/no question.
   *
   * @param input the input to validate
   * @return the valid input, either "y" or "n"
   * @throws IllegalArgumentException if the input is not "y" or "n"
   */
  public static String getValidAnswer(String input) {
    if (input.equals("y") || input.equals("n")) {
      return input;
    } else {
      throw new IllegalArgumentException("Please type Y or N.");
    }
  }
}
