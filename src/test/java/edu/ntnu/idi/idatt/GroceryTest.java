package edu.ntnu.idi.idatt;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import org.junit.jupiter.api.Test;

class GroceryTest {

  @Test
  void testeingTheToStringFunction() {
    Date date = new Date();
    Grocery grocery = new Grocery("Kjøtt", 10.0, 35, "Litre", date);

    assertEquals(grocery.toString(), "Kjøtt 10 liter, ");

  }
}