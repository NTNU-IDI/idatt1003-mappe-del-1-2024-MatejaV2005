package edu.ntnu.idi.idatt;

import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.idi.idatt.Model.Grocery;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroceryTest {

  private Grocery milk;
  private Grocery cheese;
  private Grocery meat;
  private LocalDate expiryDate;

  @BeforeEach
  void setUp() {
    expiryDate = LocalDate.of(2024, 12, 31);

    // Initialiser vanlige Grocery-objekter
    milk = new Grocery("Melk", 35.0, 3.0, "l", expiryDate);
    cheese = new Grocery("Ost", 40.0, 10.0, "g", expiryDate);
    meat = new Grocery("Kjøtt", 125.0, 10.0, "l", expiryDate);
  }

  // POSITIVE TESTS -----------------------------------------------------------------

  @Test
  void testingTheToStringFunction() {
    assertEquals(meat.toString(), "Kjøtt, 125.0kr, 10.0 l, 31-12-2024");
  }

  @Test
  void TestingIsExpired() {
    assertFalse(milk.isExpired());
  }

  @Test
  void TestingIncreaseAmount() {
    cheese.increaseAmount(2.0);
    assertEquals(cheese.toString(), "Ost, 40.0kr, 12.0 g, 31-12-2024");
  }

  @Test
  void TestingDecreaseAmount() {
    cheese.decreaseAmount(2.0);
    assertEquals(cheese.toString(), "Ost, 40.0kr, 8.0 g, 31-12-2024");
  }

  @Test
  void TestingUnitConversion() {
    // for liquid units (l)
    Grocery grocery1 = new Grocery("Melk", 40, 1000, "ml", expiryDate);
    Grocery grocery2 = new Grocery("Melk", 40, 10, "dl", expiryDate);
    Grocery grocery3 = new Grocery("Melk", 40, 1, "l", expiryDate);

    assertEquals(1.0, grocery1.getAmount());
    assertEquals(1.0, grocery2.getAmount());
    assertEquals(1.0, grocery3.getAmount());

    // for dry units (kg)
    Grocery grocery4 = new Grocery("Kjøtt", 40, 1000, "mg", expiryDate);
    Grocery grocery5 = new Grocery("Kjøtt", 40, 1, "g", expiryDate);
    Grocery grocery6 = new Grocery("Kjøtt", 40, 0.001, "kg", expiryDate);

    assertEquals(1.0, grocery4.getAmount());
    assertEquals(1.0, grocery5.getAmount());
    assertEquals(1.0, grocery6.getAmount());
  }

  // NEGATIVE TESTS --------------------------------------------------------------------------

  @Test
  void TestingInvalidNameInConstructor() {
    assertThrows(IllegalArgumentException.class, () ->
        new Grocery(null, 40.0, 10.0, "g", expiryDate));

    assertThrows(IllegalArgumentException.class, () ->
        new Grocery("", 40.0, 10.0, "g", expiryDate));
  }

  @Test
  void TestingNegativePriceInConstructor() {
    assertThrows(IllegalArgumentException.class, () ->
        new Grocery("Ost", -10.0, 10.0, "g", expiryDate));
  }

  @Test
  void TestingNegativeAmountInConstructor() {
    assertThrows(IllegalArgumentException.class, () ->
        new Grocery("Ost", 10.0, -10.0, "g", expiryDate));
  }

  @Test
  void TestingInvalidUnitInConstructor() {
    assertThrows(IllegalArgumentException.class, () ->
        new Grocery("Melk", 40.0, 10.0, "ounces", expiryDate));

    assertThrows(IllegalArgumentException.class, () ->
        new Grocery("Melk", 40.0, 10.0, "", expiryDate));

    assertThrows(IllegalArgumentException.class, () ->
        new Grocery("Melk", 40.0, 10.0, null, expiryDate));
  }

  @Test
  void TestingInvalidDateInConstructor() {
    assertThrows(IllegalArgumentException.class, () ->
        new Grocery("Ost", 40.0, 10.0, "l", null));
  }

  @Test
  void TestingInvalidAmountForSetAmount() {
    assertThrows(IllegalArgumentException.class, () -> cheese.setAmount(-5.0));
  }

  @Test
  void TestingInvalidPriceForSetPrice() {
    assertThrows(IllegalArgumentException.class, () -> cheese.setPrice(-15.0));
  }

  @Test
  void TestingZeroPriceForSetPrice() {
    assertThrows(IllegalArgumentException.class, () -> cheese.setPrice(0.0));
  }

  @Test
  void TestingInvalidAmountToIncrease() {
    assertThrows(IllegalArgumentException.class, () -> cheese.increaseAmount(-1.0));
  }

  @Test
  void TestingInvalidAmountToDecrease() {
    assertThrows(IllegalArgumentException.class, () -> cheese.decreaseAmount(20.0));
  }

  @Test
  void TestingDecreaseAmountToNegative() {
    assertThrows(IllegalArgumentException.class, () -> cheese.decreaseAmount(15.0));
  }
}
