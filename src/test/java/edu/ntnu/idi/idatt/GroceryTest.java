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

    // Initialize Grocery objects
    milk = new Grocery("Melk", 35.0, 3.0, "l", expiryDate);
    cheese = new Grocery("Ost", 40.0, 10.0, "g", expiryDate);
    meat = new Grocery("Kjøtt", 125.0, 10.0, "l", expiryDate);
  }

  // POSITIVE TESTS -----------------------------------------------------------------

  @Test
  void testingTheToStringFunction() {
    assertEquals("Kjøtt, 125.0kr, 10.0 l, 31-12-2024", meat.toString());
  }

  @Test
  void testingIsExpired() {
    assertFalse(milk.isExpired());
  }

  @Test
  void testingIncreaseAmount() {
    cheese.increaseAmount(2.0);
    assertEquals("Ost, 40.0kr, 12.0 g, 31-12-2024", cheese.toString());
  }

  @Test
  void testingDecreaseAmount() {
    cheese.decreaseAmount(2.0);
    assertEquals("Ost, 40.0kr, 8.0 g, 31-12-2024", cheese.toString());
  }

  @Test
  void testingUnitConversion() {
    // For liquid units (l)
    Grocery grocery1 = new Grocery("Melk", 40, 1000, "ml", expiryDate);
    Grocery grocery2 = new Grocery("Melk", 40, 10, "dl", expiryDate);
    Grocery grocery3 = new Grocery("Melk", 40, 1, "l", expiryDate);

    assertEquals(1.0, grocery1.getAmount());
    assertEquals(1.0, grocery2.getAmount());
    assertEquals(1.0, grocery3.getAmount());

    // For dry units (kg)
    Grocery grocery4 = new Grocery("Kjøtt", 40, 1000, "mg", expiryDate);
    Grocery grocery5 = new Grocery("Kjøtt", 40, 1, "g", expiryDate);
    Grocery grocery6 = new Grocery("Kjøtt", 40, 0.001, "kg", expiryDate);

    assertEquals(1.0, grocery4.getAmount());
    assertEquals(1.0, grocery5.getAmount());
    assertEquals(1.0, grocery6.getAmount());
  }

  // NEGATIVE TESTS -----------------------------------------------------------------

  @Test
  void testingInvalidNameInConstructor() {
    assertThrows(IllegalArgumentException.class, () ->
        new Grocery(null, 40.0, 10.0, "g", expiryDate));

    assertThrows(IllegalArgumentException.class, () ->
        new Grocery("", 40.0, 10.0, "g", expiryDate));
  }

  @Test
  void testingNegativePriceInConstructor() {
    assertThrows(IllegalArgumentException.class, () ->
        new Grocery("Ost", -10.0, 10.0, "g", expiryDate));
  }

  @Test
  void testingNegativeAmountInConstructor() {
    assertThrows(IllegalArgumentException.class, () ->
        new Grocery("Ost", 10.0, -10.0, "g", expiryDate));
  }

  @Test
  void testingInvalidUnitInConstructor() {
    assertThrows(IllegalArgumentException.class, () ->
        new Grocery("Melk", 40.0, 10.0, "ounces", expiryDate));

    assertThrows(IllegalArgumentException.class, () ->
        new Grocery("Melk", 40.0, 10.0, "", expiryDate));

    assertThrows(IllegalArgumentException.class, () ->
        new Grocery("Melk", 40.0, 10.0, null, expiryDate));
  }

  @Test
  void testingInvalidDateInConstructor() {
    assertThrows(IllegalArgumentException.class, () ->
        new Grocery("Ost", 40.0, 10.0, "l", null));
  }

  @Test
  void testingInvalidAmountForSetAmount() {
    assertThrows(IllegalArgumentException.class, () ->
        cheese.validateAndSetAmount(-5.0, cheese.getUnit()));
  }

  @Test
  void testingInvalidPriceForSetPrice() {
    assertThrows(IllegalArgumentException.class, () ->
        cheese.validateAndSetPrice(-15.0));
  }

  @Test
  void testingZeroPriceForSetPrice() {
    assertThrows(IllegalArgumentException.class, () ->
        cheese.validateAndSetPrice(0.0));
  }

  @Test
  void testingInvalidAmountToIncrease() {
    assertThrows(IllegalArgumentException.class, () ->
        cheese.increaseAmount(-1.0));
  }

  @Test
  void testingInvalidAmountToDecrease() {
    assertThrows(IllegalArgumentException.class, () ->
        cheese.decreaseAmount(20.0));
  }

  @Test
  void testingDecreaseAmountToNegative() {
    assertThrows(IllegalArgumentException.class, () ->
        cheese.decreaseAmount(15.0));
  }
}
