package edu.ntnu.idi.idatt;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Grocery {
  // objektvariabler som danner rammeverket for Grocery-klassen og dens innhold
  private final String name;
  private Double amount;
  private final int price;
  private String unit;
  public Date expiryDate;

  // Klassekonstruktør for Grocery-klassen
  public Grocery (String name, Double amount, int price, String unit, Date expiryDate) throws IllegalArgumentException{
    if (price < 0 || amount < 0) {
      throw new IllegalArgumentException("kan ikke ha negativ pris/mengde, sett inn andre verdier");
    }

    this.name = name;
    this.amount = amount;
    this.price = price;
    this.unit = unit;
    this.expiryDate = expiryDate;
  }

  // GET-metoder -------------------------
  public String getName() {
    return name;
  }

  public Double getAmount() {
    return amount;
  }

  public int getPrice() {
    return price;
  }

  public String getUnit() {
    return unit;
  }

  public Date getExpiryDate() {
    return expiryDate;
  }


  //Overrider tostring metoden for å skrive den ut på ønsket måte
  @Override
  public String toString() {
    SimpleDateFormat dateFormat = new SimpleDateFormat();
    return this.name + ", " + this.price + ", " + this.unit + ", " + dateFormat.format(expiryDate);
  }





}
