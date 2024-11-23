package edu.ntnu.idi.idatt.Model;

import edu.ntnu.idi.idatt.Utils.UnitConverter;
import edu.ntnu.idi.idatt.Utils.ExceptionHandling;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Grocery {
  private final String name;
  private double amount;
  private double pricePerUnit;
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
    ExceptionHandling.validateName(name);
    ExceptionHandling.validatePrice(pricePerUnit);
    ExceptionHandling.validateAmount(amount);
    ExceptionHandling.validateUnit(unit);
    ExceptionHandling.validateExpiryDate(expiryDate);

    //ADD MORE EXPCETION HANDLING
    //NOT SUPPOSED TO HAVE A THROW AS A INPUTVALIDATOR BUT RATHER JUST FOR TESTING IN THIS CASE
    this.name = name;
    this.pricePerUnit = pricePerUnit;
    this.unit = unit;
    //HUSK Å BRUKE SET METODER HER!!!!!! IFØLGE VURDERINGSKRITERENE (SENSORVEILEDNING)
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
    ExceptionHandling.validateAmount(amount);
    this.amount = amount;
  }

  public void setPrice(double pricePerUnit) {
    ExceptionHandling.validatePrice(pricePerUnit);
    this.pricePerUnit = pricePerUnit;
  }


  public Boolean isExpired() {
    LocalDate currentDate = LocalDate.now(); // Gets the current date
    return currentDate.isAfter(expiryDate); // Returns true if expired
  }

  // STILL UNSURE IF IM GOING TO KEEP IT LIKE THIS!!! BUT REMEMBER EVENTUALLY IF YOURE GOING TO KEEP IT LIKE THIS TO ARGUMENT WHY, USE THIS INSTEAD OF SET METHODS!!!
  public void increaseAmount(double amount) {
    ExceptionHandling.validateAmountIncrease(amount);
    this.amount += amount; // Adds to the new amount
  }

  public void decreaseAmount(double amount) {
    ExceptionHandling.validateAmountDecrease(this, amount);
    this.amount -= amount;
  }


  // Overrides the toString method to print it in the desired format
  @Override
  public String toString() {
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    return this.name + ", " + this.pricePerUnit + "kr, " + this.amount + " " + this.unit + ", " + dateFormat.format(expiryDate);
  }
}
