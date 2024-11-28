package edu.ntnu.idi.idatt.Utils;

public class UnitConverter {

  public static double ConvertUnitAmount(double amount, String unit) {
    switch (unit.toLowerCase()) {
      case "ml": return amount / 1000;
      case "dl": return amount / 10;
      case "mg": return amount / 1000;
      case "kg": return amount * 1000;
      case "g": return amount;
      case "l": return amount;
      case "stk": return amount;
      default: throw new IllegalArgumentException("Unsupported unit: " + unit);
    }
  }
}


