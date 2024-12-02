package edu.ntnu.idi.idatt;

import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.idi.idatt.Model.Grocery;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class GroceryTest {
  LocalDate expiryDate = LocalDate.of(2024,12,31);

  //POSITIVE TESTS-----------------------------------------------------------------
  @Test
  void testingTheToStringFunction() {
    Grocery grocery = new Grocery("Kjøtt", 125.0, 10.0, "l", expiryDate);

    assertEquals(grocery.toString(), "Kjøtt, 125.0kr, 10.0 l, 31-12-2024");
  }

  @Test
  void TestingIsExpired() {
    Grocery grocery = new Grocery("Melk", 35.0, 3.0, "l", expiryDate);
    assertEquals(false, grocery.isExpired());
  }

  @Test
  void TestingIncreaseAmount() {
    Grocery grocery = new Grocery("Ost", 40.0, 10.0, "g", expiryDate);
    grocery.increaseAmount(2.0);
    assertEquals(grocery.toString(), "Ost, 40.0kr, 12.0 g, 31-12-2024");
  }

  @Test
  void TestingDecreaseAmount() {
    Grocery grocery = new Grocery("Ost", 40.0, 10.0, "g", expiryDate);
    grocery.decreaseAmount(2.0);
    assertEquals(grocery.toString(), "Ost, 40.0kr, 8.0 g, 31-12-2024");
  }


  @Test
  void TestingUnitConversion() {
    //for liquid units (l)
    Grocery grocery1 = new Grocery("Melk", 40, 1000, "ml", expiryDate);
    Grocery grocery2 = new Grocery("Melk", 40, 10, "dl", expiryDate);
    Grocery grocery3 = new Grocery("Melk", 40, 1, "l", expiryDate);

    assertEquals(1.0, grocery1.getAmount());
    assertEquals(1.0, grocery2.getAmount());
    assertEquals(1.0, grocery3.getAmount());

    //for dry units (kg)
    Grocery grocery4 = new Grocery("Kjøtt", 40, 1000, "mg", expiryDate);
    Grocery grocery5 = new Grocery("Kjøtt", 40, 1, "g", expiryDate);
    Grocery grocery6 = new Grocery("Kjøtt", 40, 0.001, "kg", expiryDate);

    assertEquals(1.0, grocery4.getAmount());
    assertEquals(1.0, grocery5.getAmount());
    assertEquals(1.0, grocery6.getAmount());


  }

  //NEGATIVE TESTS--------------------------------------------------------------------------
  @Test
  void TestingInvalidNameInConstructor() {
    assertThrows(IllegalArgumentException.class, () ->
    {
      new Grocery( null, 40.0, 10.0, "g", expiryDate);
    });

    assertThrows(IllegalArgumentException.class, () ->
    {
      new Grocery( "", 40.0, 10.0, "g", expiryDate);
    });
  }

  @Test
  void TestingNegativePriceInConstructor() {
    assertThrows(IllegalArgumentException.class, () ->
    {
      new Grocery("Ost", -10.0, 10.0, "g", expiryDate);
    });
  }

  @Test
  void TestingNegativeAmountInConstructor() {
    assertThrows(IllegalArgumentException.class, () ->
    {
      new Grocery("Ost", 10.0, -10.0, "g", expiryDate);
    });
  }

  @Test
  void TestingInvalidUnitInConstructor() {
    assertThrows(IllegalArgumentException.class, () ->
    {
      new Grocery( "melk", 40.0, 10.0, "ounces", expiryDate);
    });

    assertThrows(IllegalArgumentException.class, () ->
    {
      new Grocery( "melk", 40.0, 10.0, "", expiryDate);
    });

    assertThrows(IllegalArgumentException.class, () ->
    {
      new Grocery( "melk", 40.0, 10.0, null, expiryDate);
    });
  }

  @Test
  void TestingInvalidDateInConstructor() {
    assertThrows(IllegalArgumentException.class, () ->
    {
      new Grocery("Ost", 40.0, 10.0, "l", null);
    });
  }

  @Test
  void TestingInvalidAmountForSetAmount() {
    Grocery grocery = new Grocery("Ost", 40.0, 10.0, "g", expiryDate);
    assertThrows(IllegalArgumentException.class, () -> grocery.setAmount(-5.0));
  }

  @Test
  void TestingInvalidPriceForSetPrice() {
    Grocery grocery = new Grocery("Ost", 40.0, 10.0, "g", expiryDate);
    assertThrows(IllegalArgumentException.class, () -> grocery.setPrice(-15.0));
  }

  @Test
  void TestingZeroPriceForSetPrice() {
    Grocery grocery = new Grocery("Ost", 40.0, 10.0, "g", expiryDate);
    assertThrows(IllegalArgumentException.class, () -> grocery.setPrice(0.0));
  }

  @Test
  void TestingInvalidAmountToIncrease() {
    Grocery grocery = new Grocery("Ost", 40.0, 10.0, "g", expiryDate);
    assertThrows(IllegalArgumentException.class, () -> grocery.increaseAmount(-1.0));
  }

  @Test
  void TestingInvalidAmountToDecrease() {
    Grocery grocery = new Grocery("Ost", 40.0, 10.0, "g", expiryDate);
    assertThrows(IllegalArgumentException.class, () -> grocery.decreaseAmount(20.0));
  }

  @Test
  void TestingDecreaseAmountToNegative() {
    Grocery grocery = new Grocery("Ost", 40.0, 5.0, "g", expiryDate);
    assertThrows(IllegalArgumentException.class, () -> grocery.decreaseAmount(10.0));
  }
}