package edu.ntnu.idi.idatt.Utils;

import java.time.LocalDate;

public class Utils {
  ////
  public static void validateName(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Unit can NOT be a null or empty value");
    }
  }

  public static void validatePrice(double pricePerUnit) {
    if (pricePerUnit <= 0) {
      throw new IllegalArgumentException("Price can NOT be a negative value");
    }
  }

  public static void validateAmount(double amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Amount must be greater than 0.");
    }
  }

  public static void validateUnit(String unit) {
    if (unit == null || unit.trim().isEmpty()) {
      throw new IllegalArgumentException("Unit cannot be null or empty.");
    }
  }

  public static void validateExpiryDate(LocalDate expiryDate) {
    if (expiryDate == null) {
      throw new IllegalArgumentException("Expiry date cannot be null.");
    }

    if (expiryDate.isBefore(LocalDate.now())) {
      throw new IllegalArgumentException("Expiry date cannot be before the current date.");
    }
  }

}


