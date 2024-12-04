package edu.ntnu.idi.idatt.Model;

import edu.ntnu.idi.idatt.Utils.UnitConverter;
import edu.ntnu.idi.idatt.Utils.ExceptionHandling;
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
  private double price;
  private final String unit;
  private final LocalDate expiryDate; // Immutable field to ensure safety

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
    this.amount = validateAndSetAmount(amount, unit);
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

  private String validateAndSetName(String name) {
    ExceptionHandling.validateName(name); // Validates name
    return name; // Returns validated name
  }

  private double validateAndSetPrice(double price) {
    ExceptionHandling.validatePrice(price); // Validates price
    return price; // Returns validated price
  }

  private String validateAndSetUnit(String unit) {
    ExceptionHandling.validateUnit(unit); // Validates unit
    return UnitConverter.getStandardUnit(unit); // Converts to a standard unit
  }

  private double validateAndSetAmount(double amount, String unit) {
    ExceptionHandling.validateAmount(amount); // Validates amount
    return UnitConverter.convertToStandardUnit(amount, unit); // Converts to a standardized amount
  }

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
   * Returns a string representation of the grocery item, including its name, price, amount, unit, and expiry date.
   *
   * @return a formatted string representation of the grocery item
   */
  @Override
  public String toString() {
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    return this.name + ", " + this.price + "kr, " + this.amount + " " + this.unit + ", " + dateFormat.format(expiryDate);
  }
}
