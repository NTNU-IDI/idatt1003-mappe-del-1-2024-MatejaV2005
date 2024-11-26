package edu.ntnu.idi.idatt;

import static org.junit.jupiter.api.Assertions.*;


import edu.ntnu.idi.idatt.Model.FoodStorage;
import edu.ntnu.idi.idatt.Model.Grocery;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FoodStorageTest {

  private FoodStorage foodStorage;

  @BeforeEach
  void SetUp() {
    foodStorage = new FoodStorage();
  }

  @Test
  void AddingNewGroceryToEmptyStorage() {
    Grocery grocery = new Grocery("Milk", 35.0, 1.5, "l", LocalDate.now());

    foodStorage.registerToStorage(grocery);

    List<Grocery> milkGroceries = foodStorage.getGroceriesByName("Milk");
    assertNotNull(milkGroceries);
    assertEquals(1, milkGroceries.size());
    assertEquals(1.5, grocery.getAmount());
  }

  @Test
  void AddingDuplicateGrocery_IncreasesAmount() {
    Grocery grocery = new Grocery("Milk", 35.0, 1.5, "l", LocalDate.now());
    Grocery sameGrocery = new Grocery("Milk", 35.0, 1.5, "l", LocalDate.now());
    Grocery OtherGrocery = new Grocery("Milk", 35.0, 1.5, "l", LocalDate.now().plusDays(1));

    foodStorage.registerToStorage(grocery);
    foodStorage.registerToStorage(sameGrocery);
    foodStorage.registerToStorage(OtherGrocery);

    List<Grocery> milkGroceries = foodStorage.getGroceriesByName("Milk");
    assertEquals(2, milkGroceries.size());
    assertEquals(3.0, milkGroceries.get(0).getAmount());
  }

  @Test
  void TestingIfStorageIsSortedByDate() {
    Grocery grocery1 = new Grocery("Milk", 35.0, 1.5, "l", LocalDate.now().plusDays(20));
    Grocery grocery2 = new Grocery("Milk", 35.0, 1.5, "l", LocalDate.now().plusDays(10));
    Grocery grocery3 = new Grocery("Milk", 35.0, 1.5, "l", LocalDate.now().plusDays(1));

    foodStorage.registerToStorage(grocery1);
    foodStorage.registerToStorage(grocery2);
    foodStorage.registerToStorage(grocery3);

    List<Grocery> milkGroceries = foodStorage.getGroceriesByName("Milk");

    assertEquals(grocery3, milkGroceries.get(0));
    assertEquals(grocery2, milkGroceries.get(1));
    assertEquals(grocery1, milkGroceries.get(2));
  }

  @Test
  void removingAmountFromStorage_PartialRemoval() {
    Grocery grocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now());

    foodStorage.registerToStorage(grocery1);
    foodStorage.removeAmountFromStorage("Milk", 4.0);

    List<Grocery> groceries = foodStorage.getGroceriesByName(grocery1.getName());
    assertEquals(1, groceries.get(0).getAmount());
  }

  @Test
  void removingAmountFromStorage_CompleteRemovalOfOne() {
    Grocery grocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now());
    Grocery grocery2 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now().plusDays(2));

    foodStorage.registerToStorage(grocery1);
    foodStorage.registerToStorage(grocery2);
    foodStorage.removeAmountFromStorage("Milk", 5.0);

    List<Grocery> groceries = foodStorage.getGroceriesByName(grocery1.getName());

    assertEquals(grocery2, groceries.get(0));
    assertEquals(1, groceries.size());
  }

  @Test
  void removingAmountFromStorage_IterativeRemoval() {
    Grocery grocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now());
    Grocery grocery2 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now().plusDays(2));

    foodStorage.registerToStorage(grocery1);
    foodStorage.registerToStorage(grocery2);
    foodStorage.removeAmountFromStorage("Milk", 9.0);

    List<Grocery> groceries = foodStorage.getGroceriesByName(grocery1.getName());

    assertEquals(1, groceries.get(0).getAmount());
  }

  @Test
  void testingTheInStorageMethod() {
    // Setup
    Grocery grocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now().plusDays(2));
    Grocery grocery2 = new Grocery("Milk", 35.0, 4.0, "l", LocalDate.now());
    Grocery grocery3 = new Grocery("Cheese", 35.0, 2.0, "kg", LocalDate.now().plusDays(2));
    Grocery grocery4 = new Grocery("Meat", 35.0, 2.0, "kg", LocalDate.now().plusDays(4));

    foodStorage.registerToStorage(grocery1);
    foodStorage.registerToStorage(grocery2);
    foodStorage.registerToStorage(grocery3);
    foodStorage.registerToStorage(grocery4);

    // Test for "Milk" (should return grocery1 and grocery2)
    List<Grocery> expectedMilk = List.of(grocery2, grocery1); //HAD TO MANUALLY SORT HERE BY DATE, OTHERWISE THE TEST WONT WORK. GUESSING ITS OK SINCE ITS FOR THE SAKE OF TESTING THIS METHOD
    List<Grocery> actualMilk = foodStorage.inStorage("Milk");
    assertEquals(expectedMilk, actualMilk, "Milk groceries do not match!");

    // Test for "Cheese" (should return grocery3)
    List<Grocery> expectedCheese = List.of(grocery3);
    List<Grocery> actualCheese = foodStorage.inStorage("Cheese");
    assertEquals(expectedCheese, actualCheese, "Cheese groceries do not match!");

    // Test for "Meat" (should return grocery4)
    List<Grocery> expectedMeat = List.of(grocery4);
    List<Grocery> actualMeat = foodStorage.inStorage("Meat");
    assertEquals(expectedMeat, actualMeat, "Meat groceries do not match!");

    // Test for a grocery not in storage (e.g., "Apple")
    List<Grocery> actualApple = foodStorage.inStorage("Apple");
    assertTrue(actualApple.isEmpty(), "Apple should not be found!");
  }

  @Test
  void testingTheBestBeforeMethod() {
    Grocery grocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now());
    Grocery grocery2 = new Grocery("Cheese", 35.0, 2.0, "kg", LocalDate.now().plusDays(2));
    Grocery grocery3 = new Grocery("Meat", 35.0, 2.0, "kg", LocalDate.now().plusDays(4));

    foodStorage.registerToStorage(grocery1);
    foodStorage.registerToStorage(grocery2);
    foodStorage.registerToStorage(grocery3);

    List<Grocery> bestBeforeGroceries = List.of(grocery2, grocery1);

    assertEquals(bestBeforeGroceries, foodStorage.bestBefore(LocalDate.now().plusDays(3)));
  }

  @Test
  void TotalValueOfGroceriesInStorage() {
    Grocery grocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now());
    Grocery grocery2 = new Grocery("Cheese", 35.0, 2.0, "kg", LocalDate.now().plusDays(2));
    Grocery grocery3 = new Grocery("Meat", 35.0, 2.0, "kg", LocalDate.now().plusDays(4));

    foodStorage.registerToStorage(grocery1);
    foodStorage.registerToStorage(grocery2);
    foodStorage.registerToStorage(grocery3);

    assertEquals(105.0, foodStorage.TotalValueOfGroceries());

  }

  @Test
  void TotalValueOfExpiredGroceriesInStorage() {
    Grocery grocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now().minusDays(2));
    Grocery grocery2 = new Grocery("Cheese", 65.0, 2.0, "kg", LocalDate.now().minusDays(2));
    Grocery grocery3 = new Grocery("Meat", 35.0, 2.0, "kg", LocalDate.now().plusDays(4));

    foodStorage.registerToStorage(grocery1);
    foodStorage.registerToStorage(grocery2);
    foodStorage.registerToStorage(grocery3);

    assertEquals(100.0, foodStorage.TotalValueOfExpiredGroceries());
    }






  //NEGATIVE-TESTS----------------------------------------------------------------------------
  void removingAmountFromStorage_CompleteRemovalOfALl() {
    Grocery grocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now());
    Grocery grocery2 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now().plusDays(2));

    foodStorage.registerToStorage(grocery1);
    foodStorage.registerToStorage(grocery2);
    foodStorage.removeAmountFromStorage("Milk", 10.0);

    List<Grocery> groceries = foodStorage.getGroceriesByName(grocery1.getName());

    //assertEquals("You are out of: Milk", foodStorage.removeAmountFromStorage("Milk", 10));
  }
}