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

  public static String getStandardUnit(String unit) {
    switch (unit.toLowerCase()) {
      case "ml": return "l";
      case "dl": return "l";
      case "mg": return "g";
      case "kg": return "g";
      case "g": return "g";  // Standardize weight to grams
      case "l": return "l";  // Standardize volume to liters
      case "stk": return "stk";  // Stykk Items stay as they are
      default: throw new IllegalArgumentException("Unsupported unit: " + unit);
    }
  }

  // Converts the amount to its standard unit (g for weight, l for volume)
  public static double convertToStandardUnit(double amount, String unit) {
    return ConvertUnitAmount(amount, unit);
  }
}