package edu.ntnu.idi.idatt;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class GroceryTest {
  LocalDate expiryDate = LocalDate.of(2024,12,31);

  @Test
  void testingTheToStringFunction() {
    Grocery grocery = new Grocery("Kjøtt", 125.0, 10.0, "Litre", expiryDate);

    assertEquals(grocery.toString(), "Kjøtt, 125.0kr, 10.0 Litre, 31-12-2024");
  }

  @Test
  void TestingIsExpired() {
    Grocery grocery = new Grocery("Melk", 35.0, 3.0, "Litre", expiryDate);
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
  void TestingTotalValueOfGrocery() {
    Grocery grocery = new Grocery("Egg", 45.0, 10.0, "Stk", expiryDate);
    assertEquals(grocery.totalValueOfGrocery(), 450);
  }
}