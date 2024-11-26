package edu.ntnu.idi.idatt;

import static org.junit.jupiter.api.Assertions.*;


import edu.ntnu.idi.idatt.Model.FoodStorage;
import edu.ntnu.idi.idatt.Model.Grocery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    List<Grocery> milkGroceries = foodStorage.getGroceriesByName("milk");  // Use lowercase here
    assertNotNull(milkGroceries);
    assertEquals(1, milkGroceries.size());
    assertEquals(1.5, grocery.getAmount());
  }

  @Test
  void AddingDuplicateGrocery_IncreasesAmount() {
    Grocery grocery = new Grocery("Milk", 35.0, 1.5, "l", LocalDate.now());
    Grocery sameGrocery = new Grocery("Milk", 35.0, 1.5, "l", LocalDate.now());
    Grocery otherGrocery = new Grocery("Milk", 35.0, 1.5, "l", LocalDate.now().plusDays(1));

    foodStorage.registerToStorage(grocery);
    foodStorage.registerToStorage(sameGrocery);
    foodStorage.registerToStorage(otherGrocery);

    List<Grocery> milkGroceries = foodStorage.getGroceriesByName("milk");  // Use lowercase here
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

    List<Grocery> milkGroceries = foodStorage.getGroceriesByName("milk");  // Use lowercase here

    assertEquals(grocery3, milkGroceries.get(0));
    assertEquals(grocery2, milkGroceries.get(1));
    assertEquals(grocery1, milkGroceries.get(2));
  }

  @Test
  void removingAmountFromStorage_PartialRemoval() {
    Grocery grocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now());

    foodStorage.registerToStorage(grocery1);
    foodStorage.removeAmountFromStorage("milk", 4.0);  // Use lowercase here

    List<Grocery> groceries = foodStorage.getGroceriesByName("milk");  // Use lowercase here
    assertEquals(1, groceries.get(0).getAmount());
  }

  @Test
  void removingAmountFromStorage_CompleteRemovalOfOne() {
    Grocery grocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now());
    Grocery grocery2 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now().plusDays(2));

    foodStorage.registerToStorage(grocery1);
    foodStorage.registerToStorage(grocery2);
    foodStorage.removeAmountFromStorage("milk", 5.0);  // Use lowercase here

    List<Grocery> groceries = foodStorage.getGroceriesByName("milk");  // Use lowercase here

    assertEquals(grocery2, groceries.get(0));
    assertEquals(1, groceries.size());
  }

  @Test
  void removingAmountFromStorage_IterativeRemoval() {
    Grocery grocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now());
    Grocery grocery2 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now().plusDays(2));

    foodStorage.registerToStorage(grocery1);
    foodStorage.registerToStorage(grocery2);
    foodStorage.removeAmountFromStorage("milk", 9.0);  // Use lowercase here

    List<Grocery> groceries = foodStorage.getGroceriesByName("milk");  // Use lowercase here

    assertEquals(1, groceries.get(0).getAmount());
  }

  @Test
  void sortingGroceriesInAlphabeticalOrder() {
    // Setup test data
    Grocery grocery1 = new Grocery("Banana", 20.0, 5.0, "kg", LocalDate.now());
    Grocery grocery2 = new Grocery("Apple", 15.0, 3.0, "kg", LocalDate.now());
    Grocery grocery3 = new Grocery("Carrot", 10.0, 2.0, "kg", LocalDate.now());

    foodStorage.registerToStorage(grocery1);
    foodStorage.registerToStorage(grocery2);
    foodStorage.registerToStorage(grocery3);

    // Invoke method
    Map<String, List<Grocery>> sortedGroceries = foodStorage.sortGroceries();

    // Verify the keys are sorted alphabetically
    List<String> keys = new ArrayList<>(sortedGroceries.keySet());
    assertEquals(List.of("apple", "banana", "carrot"), keys);  // Use lowercase here
  }


  @Test
  void sortingGroceriesInEmptyStorage() {
    Map<String, List<Grocery>> sortedGroceries = foodStorage.sortGroceries();

    assertTrue(sortedGroceries.isEmpty(), "Sorted groceries map should be empty");
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
    List<Grocery> expectedMilk = List.of(grocery2, grocery1);  // Sorted by date (grocery2 before grocery1)
    List<Grocery> actualMilk = foodStorage.inStorage("milk");  // Use lowercase here
    assertEquals(expectedMilk, actualMilk, "Milk groceries do not match!");

    // Test for "Cheese" (should return grocery3)
    List<Grocery> expectedCheese = List.of(grocery3);
    List<Grocery> actualCheese = foodStorage.inStorage("cheese");  // Use lowercase here
    assertEquals(expectedCheese, actualCheese, "Cheese groceries do not match!");

    // Test for "Meat" (should return grocery4)
    List<Grocery> expectedMeat = List.of(grocery4);
    List<Grocery> actualMeat = foodStorage.inStorage("meat");  // Use lowercase here
    assertEquals(expectedMeat, actualMeat, "Meat groceries do not match!");

    // Test for a grocery not in storage (e.g., "Apple")
    List<Grocery> actualApple = foodStorage.inStorage("apple");  // Use lowercase here
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

    List<Grocery> bestBeforeGroceries = List.of(grocery1, grocery2);

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

  @Test
  void TestingMoveToExpiredStorage() {
    Grocery expiredGrocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now().minusDays(1));  // Expired
    Grocery expiredGrocery2 = new Grocery("Cheese", 50.0, 2.0, "kg", LocalDate.now().minusDays(2)); // Expired
    Grocery freshGrocery = new Grocery("Bread", 20.0, 1.0, "stk", LocalDate.now().plusDays(3)); // Not expired

    foodStorage.registerToStorage(expiredGrocery1);
    foodStorage.registerToStorage(expiredGrocery2);
    foodStorage.registerToStorage(freshGrocery);

    Map<String, List<Grocery>> expiredGroceries = foodStorage.moveToExpiredGroceries();

    assertFalse(expiredGroceries.isEmpty(), "Expired groceries map should not be empty");

    assertEquals(2, expiredGroceries.size(), "There should be 2 expired grocery categories in the map");

    assertTrue(expiredGroceries.containsKey("milk"), "Expired groceries should contain 'Milk'");  // Use lowercase here
    assertTrue(expiredGroceries.containsKey("cheese"), "Expired groceries should contain 'Cheese'");  // Use lowercase here

    assertEquals(1, expiredGroceries.get("milk").size(), "There should be 1 expired Milk in the map");  // Use lowercase here
    assertEquals(1, expiredGroceries.get("cheese").size(), "There should be 1 expired Cheese in the map");  // Use lowercase here

    List<Grocery> remainingMilk = foodStorage.getGroceriesByName("milk");  // Use lowercase here
    List<Grocery> remainingCheese = foodStorage.getGroceriesByName("cheese");  // Use lowercase here
    assertTrue(remainingMilk.isEmpty(), "Milk should be removed from storage after being moved to expired");
    assertTrue(remainingCheese.isEmpty(), "Cheese should be removed from storage after being moved to expired");

    List<Grocery> remainingBread = foodStorage.getGroceriesByName("bread");  // Use lowercase here
    assertFalse(remainingBread.isEmpty(), "Bread should still be in storage as it's not expired");
  }



  //NEGATIVE-TESTS----------------------------------------------------------------------------
  @Test
  void testIfRemovalAmountExceedsTotalAmount() {
    Grocery grocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now());
    Grocery grocery2 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now().plusDays(2));

    foodStorage.registerToStorage(grocery1);
    foodStorage.registerToStorage(grocery2);

    assertThrows(IllegalArgumentException.class, () -> foodStorage.removeAmountFromStorage("Milk", 11.0));
  }

  @Test
  public void testRegisterToStorageNullGrocery() {
    FoodStorage foodStorage = new FoodStorage();

    assertThrows(IllegalArgumentException.class, () -> {
      foodStorage.registerToStorage(null);
    }, "Should throw IllegalArgumentException when adding null grocery");
  }

  @Test
  public void testRemoveAmountFromStorageInvalidGrocery() {
    FoodStorage foodStorage = new FoodStorage();

    foodStorage.registerToStorage(new Grocery("Apple", 2.0, 1.0, "kg", LocalDate.of(2024, 12, 31)));

    assertThrows(IllegalArgumentException.class, () -> {
      foodStorage.removeAmountFromStorage("Banana", 1.0);
    }, "Should throw IllegalArgumentException when removing non-existent grocery");

    assertThrows(IllegalArgumentException.class, () -> {
      foodStorage.removeAmountFromStorage("", 1.0);
    }, "Should throw IllegalArgumentException when removing grocery of empty String");

    assertThrows(IllegalArgumentException.class, () -> {
      foodStorage.removeAmountFromStorage(null, 1.0);
    }, "Should throw IllegalArgumentException when removing null grocery");
  }

  @Test
  public void testRemoveAmountFromStorageInvalidAmount() {
    FoodStorage foodStorage = new FoodStorage();

    foodStorage.registerToStorage(new Grocery("Apple", 2.0, 1.0, "kg", LocalDate.of(2024, 12, 31)));

    assertThrows(IllegalArgumentException.class, () -> {
      foodStorage.removeAmountFromStorage("Apple", -1.0);
    }, "Should throw IllegalArgumentException when removing a negative amount");
  }

  @Test
  public void testInStorageInvalidName() {
    FoodStorage foodStorage = new FoodStorage();

    foodStorage.registerToStorage(new Grocery("Apple", 2.0, 1.0, "kg", LocalDate.of(2024, 12, 31)));

    List<Grocery> groceries = foodStorage.inStorage("Banana");
    assertTrue(groceries.isEmpty(), "Should return an empty list when the grocery is not found");
  }

  @Test
  public void testBestBeforeInvalidDate() {
    FoodStorage foodStorage = new FoodStorage();

    foodStorage.registerToStorage(new Grocery("Apple", 2.0, 1.0, "kg", LocalDate.of(2024, 12, 31)));

    assertThrows(IllegalArgumentException.class, () -> {
      foodStorage.bestBefore(null); // null date
    }, "Should throw IllegalArgumentException when the date is null");
  }

  @Test
  public void testTotalValueOfGroceriesEmptyStorage() {
    FoodStorage foodStorage = new FoodStorage();

    double totalValue = foodStorage.TotalValueOfGroceries();
    assertEquals(0.0, totalValue, "Total value should be 0.0 when storage is empty");
  }

  @Test
  public void testMoveToExpiredGroceriesNoExpired() {
    FoodStorage foodStorage = new FoodStorage();

    foodStorage.registerToStorage(new Grocery("Apple", 2.0, 1.0, "kg", LocalDate.of(2025, 12, 31)));

    Map<String, List<Grocery>> expiredGroceries = foodStorage.moveToExpiredGroceries();
    assertTrue(expiredGroceries.isEmpty(), "Should return an empty map when there are no expired groceries");
  }

  @Test
  public void testMoveToExpiredGroceries() {
    FoodStorage foodStorage = new FoodStorage();

    // Adding expired grocery
    foodStorage.registerToStorage(new Grocery("Apple", 2.0, 1.0, "kg", LocalDate.of(2023, 12, 31)));

    // Checking before calling moveToExpiredGroceries
    Map<String, List<Grocery>> expiredGroceries = foodStorage.moveToExpiredGroceries();
    assertFalse(expiredGroceries.isEmpty(), "Should move expired groceries to the expired list");
    assertTrue(foodStorage.getGroceriesByName("Apple").isEmpty(), "Should remove expired groceries from storage");
  }

  @Test
  public void testTotalValueOfExpiredGroceriesNotExpired() {
    FoodStorage foodStorage = new FoodStorage();

    foodStorage.registerToStorage(new Grocery("Apple", 2.0, 1.0, "kg", LocalDate.of(2025, 12, 31)));

    double totalValue = foodStorage.TotalValueOfExpiredGroceries();
    assertEquals(0.0, totalValue, "Total value of expired groceries should be 0.0 when there are no expired groceries");
  }

}
