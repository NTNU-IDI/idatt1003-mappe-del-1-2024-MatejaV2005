package edu.ntnu.idi.idatt.Model;

import edu.ntnu.idi.idatt.Utils.UnitConverter;
import edu.ntnu.idi.idatt.Utils.ExceptionHandling;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Grocery {
  private final String name;
  private double amount;
  private double price;
  private final String unit;
  public final LocalDate expiryDate;

  /**
   * objectvariables of the grocery constructor with following parametres
   * @param name
   * @param price
   * @param amount
   * @param unit
   * @param expiryDate
   * */

  // Constructor for the Grocery class
  public Grocery(String name, double price, double amount, String unit, LocalDate expiryDate) {
    ExceptionHandling.validateName(name);
    ExceptionHandling.validatePrice(price);
    ExceptionHandling.validateAmount(amount);
    ExceptionHandling.validateUnit(unit);
    ExceptionHandling.validateExpiryDate(expiryDate);

    //ADD MORE EXPCETION HANDLING
    //NOT SUPPOSED TO HAVE A THROW AS A INPUTVALIDATOR BUT RATHER JUST FOR TESTING IN THIS CASE
    this.name = name;
    setPrice(price);
    this.unit = unit;
    //HUSK Å BRUKE SET METODER HER!!!!!! IFØLGE VURDERINGSKRITERENE (SENSORVEILEDNING)
    setAmount(amount);
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

  //SET-METHODS----------------------------------------------------- (REMEMBER TO TEST???)
  public void setAmount(double amount) {
    ExceptionHandling.validateAmount(amount);
    this.amount = UnitConverter.convertToStandardUnit(amount, unit);;
  }

  public void setPrice(double price) {
    ExceptionHandling.validatePrice(price);
    this.price = price;
  }


  public Boolean isExpired() {
    LocalDate currentDate = LocalDate.now(); // Gets the current date
    return currentDate.isAfter(expiryDate); // Returns true if expired
  }

  // STILL UNSURE IF IM GOING TO KEEP IT LIKE THIS!!! BUT REMEMBER EVENTUALLY IF YOURE GOING TO KEEP IT LIKE THIS TO ARGUMENT WHY, USE THIS INSTEAD OF SET METHODS!!!
  public void increaseAmount(double amountToIncrease) {
    ExceptionHandling.validateAmountIncrease(amountToIncrease);
    this.amount += amountToIncrease; // Adds to the new amount
  }

  public void decreaseAmount(double amountToDecrease) {
    ExceptionHandling.validateAmountDecrease(this, amountToDecrease);
    this.amount -= amountToDecrease;
  }


  // Overrides the toString method to print it in the desired format
  @Override
  public String toString() {
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    return this.name + ", " + this.price + "kr, " + this.amount + " " + this.unit + ", " + dateFormat.format(expiryDate);
  }
}
