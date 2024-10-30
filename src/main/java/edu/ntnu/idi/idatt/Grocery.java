package edu.ntnu.idi.idatt;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Grocery {
  // Instance variables that form the framework for the Grocery class and its content
  private final String name;
  private Double amount;
  private final double price;
  private String unit;
  public LocalDate expiryDate;

  // Constructor for the Grocery class
  public Grocery(String name, double price, double amount, String unit, LocalDate expiryDate) throws IllegalArgumentException {
    if (price < 0 || amount < 0) {
      throw new IllegalArgumentException("Price/amount can NOT be a negative value");
    }
    this.name = name;
    this.price = price;
    this.amount = amount;
    this.unit = unit;
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
    return price;
  }

  public String getUnit() {
    return unit;
  }

  public LocalDate getExpiryDate() {
    return expiryDate;
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
    double totalValue = this.price * this.amount; // Total value in currency of the grocery
    return totalValue;
  }

  //REMEMBER TO ADD A METHOD FOR CALCULATING WITH DIFFERENT SI-UNITS!!!!

  // Overrides the toString method to print it in the desired format
  @Override
  public String toString() {
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    return this.name + ", " + this.price + "kr, " + this.amount + " " + this.unit + ", " + dateFormat.format(expiryDate);
  }
}
