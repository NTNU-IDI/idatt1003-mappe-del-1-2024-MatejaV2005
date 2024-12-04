package edu.ntnu.idi.idatt.Model;

import edu.ntnu.idi.idatt.Utils.ExceptionHandling;
import edu.ntnu.idi.idatt.Utils.UnitConverter;

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

  // -------------------------------------------------------------------
  // Getters
  // -------------------------------------------------------------------

  public double getAmount() {
    return amount;
  }

  public String getUnit() {
    return unit;
  }

  // -------------------------------------------------------------------
  // Combined Setter for Amount and Unit
  // -------------------------------------------------------------------

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

  // -------------------------------------------------------------------
  // Amount Manipulation
  // -------------------------------------------------------------------

  /**
   * Increases the amount of the ingredient.
   *
   * @param amountToAdd the amount to add
   */
  public void increaseAmount(double amountToAdd) {
    ExceptionHandling.validateAmount(amountToAdd);
    this.amount += amountToAdd; // Add directly in the standardized unit
  }

  /**
   * Decreases the amount of the ingredient.
   *
   * @param amountToSubtract the amount to subtract
   */
  public void decreaseAmount(double amountToSubtract) {
    this.amount -= amountToSubtract; // Subtract directly in the standardized unit
  }

  // -------------------------------------------------------------------
  // Override Methods
  // -------------------------------------------------------------------

  @Override
  public String toString() {
    return String.format("%.2f %s", amount, unit);
  }
}
