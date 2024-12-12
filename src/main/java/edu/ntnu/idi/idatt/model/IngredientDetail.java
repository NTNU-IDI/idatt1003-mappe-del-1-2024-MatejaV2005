package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.utils.ExceptionHandling;
import edu.ntnu.idi.idatt.utils.UnitConverter;

/**
 * Represents detailed information about an ingredient, including its amount and unit.
 * Automatically converts the amount and unit to standard forms (grams or liters).
 */
public class IngredientDetail {

  private double amount; // The amount of the ingredient in its standard unit
  private String unit;   // The standard unit of the ingredient (e.g., "g", "l")

  /**
   * Constructs an IngredientDetail with a specified amount and unit.
   * Automatically converts the unit and amount to standard forms.
   *
   * @param amount the amount of the ingredient
   * @param unit   the unit of the ingredient
   */
  public IngredientDetail(double amount, String unit) {
    this.setAmountAndUnit(amount, unit); // Automatically validate and convert
  }


  public double getAmount() {
    return amount;
  }

  public String getUnit() {
    return unit;
  }


  /**
   * Sets the amount and unit, converting both to the standard format.
   *
   * @param amount the new amount
   * @param unit   the new unit
   */
  public void setAmountAndUnit(double amount, String unit) {
    ExceptionHandling.validateAmount(amount);
    ExceptionHandling.validateUnit(unit);

    // Convert and standardize the unit and amount
    this.unit = UnitConverter.getStandardUnit(unit);
    this.amount = UnitConverter.convertToStandardUnit(amount, unit);
  }

  @Override
  public String toString() {
    return String.format("%.2f %s", amount, unit);
  }
}
