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

  public static double getValidDouble(String prompt) {
    System.out.println(prompt);
    String input = sc.nextLine();
    double output;

    try {
      output = Double.parseDouble(input);
      ExceptionHandling.validatePrice(output);
      ExceptionHandling.validateAmount(output);
      return output;
    } catch (NumberFormatException e) {
      System.out.println("error: Input must be a valid number");
      return getValidDouble(prompt);
    } catch (IllegalArgumentException e) {
      System.out.println("error:" + e.getMessage());
      return getValidDouble(prompt);
    }
  }

  public static String getValidString(String prompt) {
    return "JNAFIIA";
  }

  public static LocalDate getDateFromUser(Scanner scan) {
    String dateString = scan.nextLine();
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy"); //DateTimeFormatter class that instantiates a new DateTimeFormatter instance and sets it to the desired form of our date
    System.out.println(dateString);
    try {
      LocalDate expiryDate = LocalDate.parse(dateString, dateFormat); // compares here if the user-input of the date (dateString) format equals the dateFormat we assigned by parsing through it. If valid, we assign it to expiryDate and the value is converted to a Date datatype
      //dateFormat.parse has the built-in function to, if correct format, convert the String to Date datatype. parse-method is from SimpleDateFormat
      return expiryDate;
    } catch (DateTimeParseException e) { //Throw exception if date format is incorrect
      System.out.println("Invalid date format." + e.getMessage());
      return getDateFromUser(scan);
    }
  }
}
