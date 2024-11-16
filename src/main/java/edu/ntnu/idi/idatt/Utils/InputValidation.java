package edu.ntnu.idi.idatt.Utils;

import java.util.Scanner;

public class InputValidation {
  Scanner sc = new Scanner(System.in);

  public int getValidInt(String prompt) {
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
}
