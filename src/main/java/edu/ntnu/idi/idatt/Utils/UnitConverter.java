package edu.ntnu.idi.idatt.Utils;

public class UnitConverter {

  public static double convertToStandardUnit(double amount, String unit) {
    switch (unit.toLowerCase()) {
      case "ml": return amount / 1000;
      case "dl": return amount / 10;
      case "mg": return amount / 1_000_000;
      case "g": return amount / 1000;
      case "l":
      case "stk":
      case "kg": return amount;
      default: throw new IllegalArgumentException("Unsupported unit: " + unit);
    }
  }
}


