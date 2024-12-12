package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.model.IngredientDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IngredientDetailTest {
  private IngredientDetail ingredient;

  @BeforeEach
  void setUp() {
    ingredient = new IngredientDetail(2.0, "kg"); // Initialize with 2 kg
  }

  @Test
  void testConstructorValidInputs() {
    assertEquals(2000.0, ingredient.getAmount(), "The amount should be converted to 2000 grams.");
    assertEquals("g", ingredient.getUnit(), "The unit should be standardized to 'g'.");
  }

  @Test
  void testConstructorInvalidUnit() {
    assertThrows(IllegalArgumentException.class, () -> new IngredientDetail(2.0, "invalidUnit"),
        "Should throw IllegalArgumentException for an invalid unit.");
  }

  @Test
  void testConstructorInvalidAmount() {
    assertThrows(IllegalArgumentException.class, () -> new IngredientDetail(-2.0, "kg"),
        "Should throw IllegalArgumentException for a negative amount.");
  }

  @Test
  void testSetAmountAndUnitValidInputs() {
    ingredient.setAmountAndUnit(0.5, "kg"); // 500 mg
    assertEquals(500, ingredient.getAmount(), "The amount should be converted to 0.5 grams.");
    assertEquals("g", ingredient.getUnit(), "The unit should be standardized to 'g'.");
  }

  @Test
  void testSetAmountAndUnitInvalidUnit() {
    assertThrows(IllegalArgumentException.class, () -> ingredient.setAmountAndUnit(500, "invalidUnit"),
        "Should throw IllegalArgumentException for an invalid unit.");
  }

  @Test
  void testSetAmountAndUnitInvalidAmount() {
    assertThrows(IllegalArgumentException.class, () -> ingredient.setAmountAndUnit(-100, "g"),
        "Should throw IllegalArgumentException for a negative amount.");
  }

  @Test
  void testToString() {
    assertEquals("2000.00 g", ingredient.toString(),
        "toString() should return the formatted amount and unit.");
  }
}

