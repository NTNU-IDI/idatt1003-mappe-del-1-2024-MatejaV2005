package edu.ntnu.idi.idatt.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    }
    catch (IllegalArgumentException e) {
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
    } catch (DateTimeParseException e) { //Throw exception if date format is incorrect
      System.out.println("Invalid date format.");
      return getValidDate(prompt);
    }
  }
}
