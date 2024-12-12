package edu.ntnu.idi.idatt.utils;

/**
 * Utility class for handling unit conversions in a grocery or recipe context.
 *
 * <p>This class provides methods to:
 * <ul>
 *   <li>Convert amounts between different units to standard units (e.g., grams, liters).</li>
 *   <li>Determine the standard unit for a given input unit.</li>
 * </ul>
 *
 * <p>Supported units include:
 * <ul>
 *   <li>Weight: "g", "kg".</li>
 *   <li>Volume: "l", "ml", "dl".</li>
 *   <li>Count: "stk" (for discrete items).</li>
 * </ul>
 *
 * <p>Unsupported or invalid units will result in an {@link IllegalArgumentException}.
 */

public class UnitConverter {

  /**
   * Converts a given amount from a specified unit to its base unit.
   *
   * <p>For example:
   * <ul>
   *   <li>"ml" is converted to liters.</li>
   *   <li>"kg" is converted to grams.</li>
   *   <li>"g", "l", and "stk" remain unchanged.</li>
   * </ul>
   *
   * @param amount the amount to convert
   * @param unit the unit of the amount
   * @return the converted amount in the base unit
   * @throws IllegalArgumentException if the unit is unsupported
   */

  public static double convertUnitAmount(double amount, String unit) {
    return switch (unit.toLowerCase()) {
      case "ml" -> amount / 1000;
      case "dl" -> amount / 10;
      case "kg" -> amount * 1000;
      case "g", "l", "stk" -> amount;
      default -> throw new IllegalArgumentException("Unsupported unit: " + unit);
    };
  }

  /**
   * Returns the standard unit corresponding to the given unit.
   *
   * <p>For example:
   * <ul>
   *   <li>"ml" and "dl" standardize to "l".</li>
   *   <li>"kg" and "mg" standardize to "g".</li>
   *   <li>"g", "l", and "stk" remain unchanged.</li>
   * </ul>
   *
   * @param unit the unit to standardize
   * @return the standard unit as a string
   * @throws IllegalArgumentException if the unit is unsupported
   */

  public static String getStandardUnit(String unit) {
    return switch (unit.toLowerCase()) {
      case "ml", "dl" -> "l";
      case "kg" -> "g";
      case "g" -> "g";  // Standardize weight to grams
      case "l" -> "l";  // Standardize volume to liters
      case "stk" -> "stk";  // Stock Items stay as they are
      default -> throw new IllegalArgumentException("Unsupported unit: " + unit);
    };
  }


  /**
   * Converts a given amount to its standard unit.
   *
   * <p>This method delegates to {@link #convertUnitAmount(double, String)}
   * to perform the conversion.
   *
   * @param amount the amount to convert
   * @param unit the unit of the amount
   * @return the converted amount in the standard unit
   * @throws IllegalArgumentException if the unit is unsupported
   */

  public static double convertToStandardUnit(double amount, String unit) {
    // Converts the amount to its standard unit (g for weight, l for volume)
    return convertUnitAmount(amount, unit);
  }
}