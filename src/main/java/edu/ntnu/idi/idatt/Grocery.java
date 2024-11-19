package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.Utils.UnitConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Grocery {
  private final String name;
  private Double amount;
  private final double pricePerUnit;
  private final String unit;
  public final LocalDate expiryDate;

  /**
   * objectvariables of the grocery constructor with following parametres
   * @param name
   * @param pricePerUnit
   * @param amount
   * @param unit
   * @param expiryDate
   * */

  // Constructor for the Grocery class
  public Grocery(String name, double pricePerUnit, double amount, String unit, LocalDate expiryDate) throws IllegalArgumentException {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Price/amount can NOT be a negative value");
    }
    if (pricePerUnit <= 0 || amount < 0) {
      throw new IllegalArgumentException("Price/amount can NOT be a negative value");
    }
    //ADD MORE EXPCETION HANDLING
    //NOT SUPPOSED TO HAVE A THROW AS A INPUTVALIDATOR BUT RATHER JUST FOR TESTING IN THIS CASE
    this.name = name;
    this.pricePerUnit = pricePerUnit;
    this.unit = unit;
    this.amount = UnitConverter.convertToStandardUnit(amount, unit);
    this.expiryDate = expiryDate;
  }

  // GET methods -------------------------
  public String getName() {
    return name;
  }

  public Double getAmount() {
    return amount;
  }

  public double getPrice() {
    return pricePerUnit;
  }

  public String getUnit() {
    return unit;
  }

  public LocalDate getExpiryDate() {
    return expiryDate;
  }

  //SET-METHODS-----------------------------------------------------
  public void setAmount(double amount) {
    this.amount = amount;
  }


  public Boolean isExpired() {
    LocalDate currentDate = LocalDate.now(); // Gets the current date
    return currentDate.isAfter(expiryDate); // Returns true if expired
  }

  // Handles increasing and decreasing the amount, unsure if ill keep it like this, possibly better solution
  public void increaseAmount(double amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Please enter a valid number (not negative)"); // Invalid input for negative increase
    }
    this.amount += amount; // Adds to the new amount
  }

  public void decreaseAmount(double amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Please enter a valid number (not negative)"); // Invalid input for positive decrease
    }
    this.amount -= amount; // Decreases the amount
  }

  public double totalValueOfGrocery() {
    double totalValue = this.pricePerUnit * this.amount; // Total value in currency of the grocery
    return totalValue;
  }

  // Overrides the toString method to print it in the desired format
  @Override
  public String toString() {
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    return this.name + ", " + this.pricePerUnit + "kr, " + this.amount + " " + this.unit + ", " + dateFormat.format(expiryDate);
  }
}
