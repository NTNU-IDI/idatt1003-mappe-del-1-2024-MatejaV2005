package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.utils.ExceptionHandling;
import edu.ntnu.idi.idatt.utils.UnitConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a grocery item with its name, price, amount, unit, and expiry date.
 * Provides methods to manipulate and retrieve the details of the grocery item.
 * Includes functionality to check if the grocery is expired and to format the item as a string.
 */

public class Grocery {

  private final String name;
  private double amount;
  private final double price;
  private final String unit;
  private final LocalDate expiryDate;

  /**
   * Constructs a new Grocery object with the specified details.
   *
   * @param name       the name of the grocery item (e.g., "Milk", "Bread")
   * @param price      the price of the grocery item (e.g., 10.50)
   * @param amount     the amount of the grocery item (e.g., 1000 for 1000 grams)
   * @param unit       the unit of measurement for the amount (e.g., "g", "kg", "stk")
   * @param expiryDate the expiry date of the grocery item
   * @throws IllegalArgumentException if any of the parameters are invalid
   */

  public Grocery(String name, double price, double amount, String unit, LocalDate expiryDate) {
    this.name = validateAndSetName(name);
    this.price = validateAndSetPrice(price);
    this.unit = validateAndSetUnit(unit);
    validateAndSetAmount(amount, unit);
    this.expiryDate = validateAndSetExpiryDate(expiryDate);
  }

  // Getters -----------------------------------------

  public String getName() {
    return name;
  }

  public double getAmount() {
    return amount;
  }

  public double getPrice() {
    return price;
  }

  public String getUnit() {
    return unit;
  }

  public LocalDate getExpiryDate() {
    return expiryDate;
  }

  // Private Validation and Setting Methods -----------------------------------------

  /**
   * Validates and sets the name of the grocery.
   *
   * @param name the name of the grocery to validate and set
   * @return the validated name
   * @throws IllegalArgumentException if the name is invalid
   */

  private String validateAndSetName(String name) {
    ExceptionHandling.validateName(name); // Validates name
    return name.toLowerCase(); // Returns validated name
  }

  /**
   * Validates and sets the price of the grocery.
   *
   * @param price the price of the grocery to validate and set
   * @return the validated price
   * @throws IllegalArgumentException if the price is invalid
   */

  public double validateAndSetPrice(double price) {
    ExceptionHandling.validatePrice(price); // Validates price
    return price; // Returns validated price
  }

  /**
   * Validates and sets the unit of the grocery.
   *
   * @param unit the unit of the grocery to validate and set
   * @return the standardized unit after validation
   * @throws IllegalArgumentException if the unit is invalid
   */

  private String validateAndSetUnit(String unit) {
    ExceptionHandling.validateUnit(unit); // Validates unit
    return UnitConverter.getStandardUnit(unit); // Converts to a standard unit
  }

  /**
   * Validates and sets the amount of the grocery.
   * The amount is converted to its standardized unit (e.g., grams or liters).
   *
   * @param amount the amount of the grocery to validate and set
   * @param unit the unit of the amount to assist in conversion
   * @throws IllegalArgumentException if the amount is invalid
   */

  public void validateAndSetAmount(double amount, String unit) {
    // Validates amount
    ExceptionHandling.validateAmount(amount);

    // Converts to a standardized amount
    this.amount = UnitConverter.convertToStandardUnit(amount, unit);
  }

  /**
   * Validates and sets the expiry date of the grocery.
   *
   * @param expiryDate the expiry date to validate and set
   * @return the validated expiry date
   * @throws IllegalArgumentException if the expiry date is invalid
   */

  private LocalDate validateAndSetExpiryDate(LocalDate expiryDate) {
    ExceptionHandling.validateExpiryDate(expiryDate); // Validates expiry date
    return expiryDate; // Returns validated expiry date
  }

  //---------------------------------------------------------------------------

  /**
   * Checks if the grocery item is expired based on the current date and the expiry date.
   *
   * @return {@code true} if the grocery item is expired, otherwise {@code false}
   */

  public boolean isExpired() {
    LocalDate currentDate = LocalDate.now(); // Gets the current date
    return currentDate.isAfter(expiryDate); // Returns true if expired
  }

  /**
   * Increases the amount of the grocery item.
   *
   * @param amountToIncrease the amount to increase the grocery item's amount by
   * @throws IllegalArgumentException if the increase amount is invalid
   */

  public void increaseAmount(double amountToIncrease) {
    ExceptionHandling.validateAmountIncrease(amountToIncrease);
    this.amount += amountToIncrease; // Adds to the new amount
  }

  /**
   * Decreases the amount of the grocery item.
   *
   * @param amountToDecrease the amount to decrease the grocery item's amount by
   * @throws IllegalArgumentException if the decrease amount is invalid
   */

  public void decreaseAmount(double amountToDecrease) {
    ExceptionHandling.validateAmountDecrease(this, amountToDecrease);
    this.amount -= amountToDecrease;
  }

  /**
   * Returns a string representation of the grocery item,
   * including its name, price, amount, unit, and expiry date.
   *
   * @return a formatted string representation of the grocery item
   */

  @Override
  public String toString() {
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    return this.name + ", " + this.price + "kr, "
        + this.amount + " " + this.unit + ", " + dateFormat.format(expiryDate);
  }
}
