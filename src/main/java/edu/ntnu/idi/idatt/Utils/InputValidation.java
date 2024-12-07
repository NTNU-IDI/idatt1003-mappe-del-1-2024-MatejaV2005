package edu.ntnu.idi.idatt.Utils;

import edu.ntnu.idi.idatt.Model.FoodStorage;
import edu.ntnu.idi.idatt.Model.Grocery;
import edu.ntnu.idi.idatt.Model.Recipe;
import edu.ntnu.idi.idatt.Model.RecipeBook;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class InputValidation {
  static Scanner sc = new Scanner(System.in);

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

  public static String getValidUnit(String prompt) {
    System.out.println(prompt);
    String input = sc.nextLine().trim();
    String output;

    try {
      output = input;
      ExceptionHandling.validateUnit(output);
      UnitConverter.ConvertUnitAmount(1, output);
      return output;
    } catch (IllegalArgumentException e) {
      System.out.println("error: " + e.getMessage());
      return getValidUnit(prompt);
    }
  }

  public static LocalDate getValidDate(String prompt) {
    System.out.println(prompt);
    String dateString = sc.nextLine();
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy"); //DateTimeFormatter class that instantiates a new DateTimeFormatter instance and sets it to the desired form of our date
    LocalDate output;

    try {
      output = LocalDate.parse(dateString, dateFormat); // compares here if the user-input of the date (dateString) format equals the dateFormat we assigned by parsing through it. If valid, we assign it to expiryDate and the value is converted to a Date datatype
      ExceptionHandling.validateExpiryDate(output);
      return output;
    } catch (DateTimeParseException e) { // Throw exception if date format is incorrect
      System.out.println("Invalid date format.");
      return getValidDate(prompt);
    }
  }

  public static String getValidItemToRemove(String prompt, Map<String, List<Grocery>> storage) {
    System.out.println(prompt);
    String input = sc.nextLine().trim();

    try {
      ExceptionHandling.validateStorageContainsItem(storage, input);
      return input;
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
      return getValidItemToRemove(prompt, storage); // Retry if validation fails
    }
  }

  public static String getValidCompatibleUnit(String prompt, String groceryName, Map<String, List<Grocery>> storage) {
    System.out.println(prompt);
    String inputUnit = sc.nextLine().trim();

    try {
      // Validate that the unit is compatible with the grocery in storage
      ExceptionHandling.validateUnitCompatibility(inputUnit, groceryName, storage);
      return inputUnit; // Return the valid unit if validation passes
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
      return getValidCompatibleUnit(prompt, groceryName, storage); // Retry if validation fails
    }
  }

  public static double getValidAmountToRemove(String prompt, Map<String, List<Grocery>> storage, String itemName, String unit) {
    System.out.println(prompt);
    String input = sc.nextLine().trim();

    try {
      double amount = Double.parseDouble(input);
      double standardizedAmount = UnitConverter.ConvertUnitAmount(amount, unit);
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



  public static Recipe getValidRecipe(String prompt, RecipeBook recipebook) {
    System.out.println(prompt);
    String recipeName = sc.nextLine();

    try {
      Recipe toCheck = recipebook.getRecipe(recipeName);
      ExceptionHandling.validateRecipe(toCheck);
      return toCheck;
    } catch (IllegalArgumentException e) {
      System.out.println("error: Recipe not found in recipe book");
      return getValidRecipe(prompt, recipebook);
    }
  }

  public static String getValidAnswer(String input) {
    if (input.equals("y") || input.equals("n")) {
      return input;
    } else {
      throw new IllegalArgumentException("Please type Y or N.");
    }
  }
}
