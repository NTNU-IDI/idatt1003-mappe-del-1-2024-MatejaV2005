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

  public static String getStandarUnit(String unit) {
    switch (unit.toLowerCase()) {
      case "ml": return "l";
      case "dl": return "l";
      case "mg": return "g";
      case "kg": return "g";
      case "g": return "g";
      case "l": return "l";
      case "stk": return "stk";
      default: throw new IllegalArgumentException("Unsupported unit: " + unit);
    }
  }
}


