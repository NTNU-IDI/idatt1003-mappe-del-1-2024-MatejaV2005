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
    Grocery grocery = new Grocery("Ost", 40.0, 10.0, "kg", expiryDate);
    grocery.increaseAmount(2.0);
    assertEquals(grocery.toString(), "Ost, 40.0kr, 12.0 kg, 31-12-2024");
  }

  @Test
  void TestingDecreaseAmount() {
    Grocery grocery = new Grocery("Ost", 40.0, 10.0, "kg", expiryDate);
    grocery.decreaseAmount(2.0);
    assertEquals(grocery.toString(), "Ost, 40.0kr, 8.0 kg, 31-12-2024");
  }

  @Test
  void TestingUnitConversion() {

  }

  //NEGATIVE TESTS--------------------------------------------------------------------------
  @Test
  void TestingInvalidNameInConstructor() {
    assertThrows(IllegalArgumentException.class, () ->
    {
      new Grocery( null, 40.0, 10.0, "kg", expiryDate);
    });

    assertThrows(IllegalArgumentException.class, () ->
    {
      new Grocery( "", 40.0, 10.0, "kg", expiryDate);
    });
  }

  @Test
  void TestingNegativePriceInConstructor() {
    assertThrows(IllegalArgumentException.class, () ->
    {
      new Grocery("Ost", -10.0, 10.0, "kg", expiryDate);
    });
  }

  @Test
  void TestingNegativeAmountInConstructor() {
    assertThrows(IllegalArgumentException.class, () ->
    {
      new Grocery("Ost", 10.0, -10.0, "kg", expiryDate);
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

    assertThrows(IllegalArgumentException.class, () ->
    {
      new Grocery("Ost", 40.0, 10.0, "l", LocalDate.of(2024, 11, 22));
    });

  }

  @Test
  void testingInvalidAmountToIncrease() {
    assertThrows(IllegalArgumentException.class, () ->
    {
      Grocery totest = new Grocery("Ost", 40.0, 10.0, "l", null);
      totest.increaseAmount(-1.0);
    });
  }

  @Test
  void testingInvalidAmountToDecrease() {
    assertThrows(IllegalArgumentException.class, () ->
    {
      Grocery totest = new Grocery("Ost", 40.0, 10.0, "l", null);
      totest.increaseAmount(-1.0);
    });
  }





}