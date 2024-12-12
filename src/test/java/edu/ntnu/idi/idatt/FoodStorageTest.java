package edu.ntnu.idi.idatt;

import static org.junit.jupiter.api.Assertions.*;


import edu.ntnu.idi.idatt.model.FoodStorage;
import edu.ntnu.idi.idatt.model.Grocery;
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

    List<Grocery> milkGroceries = foodStorage.findGroceriesByName("milk");  // Use lowercase here
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

    List<Grocery> milkGroceries = foodStorage.findGroceriesByName("milk");  // Use lowercase here
    assertEquals(2, milkGroceries.size());
    assertEquals(3.0, milkGroceries.getFirst().getAmount());
  }

  @Test
  void TestingIfStorageIsSortedByDate() {
    Grocery grocery1 = new Grocery("Milk", 35.0, 1.5, "l", LocalDate.now().plusDays(20));
    Grocery grocery2 = new Grocery("Milk", 35.0, 1.5, "l", LocalDate.now().plusDays(10));
    Grocery grocery3 = new Grocery("Milk", 35.0, 1.5, "l", LocalDate.now().plusDays(1));

    foodStorage.registerToStorage(grocery1);
    foodStorage.registerToStorage(grocery2);
    foodStorage.registerToStorage(grocery3);

    List<Grocery> milkGroceries = foodStorage.findGroceriesByName("milk");  // Use lowercase here

    assertEquals(grocery3, milkGroceries.get(0));
    assertEquals(grocery2, milkGroceries.get(1));
    assertEquals(grocery1, milkGroceries.get(2));
  }

  @Test
  void removingAmountFromStorage_PartialRemoval() {
    Grocery grocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now());

    foodStorage.registerToStorage(grocery1);
    foodStorage.removeAmountFromStorage("milk", 4.0, "l");

    List<Grocery> groceries = foodStorage.findGroceriesByName("milk");
    assertEquals(1, groceries.getFirst().getAmount());
  }

  @Test
  void removingAmountFromStorage_CompleteRemovalOfOne() {
    Grocery grocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now());
    Grocery grocery2 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now().plusDays(2));

    foodStorage.registerToStorage(grocery1);
    foodStorage.registerToStorage(grocery2);
    foodStorage.removeAmountFromStorage("milk", 50.0, "dl");  // Use lowercase here

    List<Grocery> groceries = foodStorage.findGroceriesByName("milk");  // Use lowercase here

    assertEquals(grocery2, groceries.getFirst());
    assertEquals(1, groceries.size());
  }

  @Test
  void removingAmountFromStorage_IterativeRemoval() {
    Grocery grocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now());
    Grocery grocery2 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now().plusDays(2));

    foodStorage.registerToStorage(grocery1);
    foodStorage.registerToStorage(grocery2);
    foodStorage.removeAmountFromStorage("milk", 9.0, "l");  // Use lowercase here

    List<Grocery> groceries = foodStorage.findGroceriesByName("milk");  // Use lowercase here

    assertEquals(1, groceries.getFirst().getAmount());
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
  void testFindGroceryInNormalStorage() {
    // Setup: Add non-expired groceries
    foodStorage.registerToStorage(new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now().plusDays(2))); // Not expired
    foodStorage.registerToStorage(new Grocery("Cheese", 35.0, 2.0, "kg", LocalDate.now().plusDays(2))); // Not expired

    // Test normal storage lookup
    List<Grocery> normalMilk = foodStorage.findInStorage("milk", false);
    assertEquals(1, normalMilk.size(), "Milk should have 1 non-expired entry.");
    assertEquals("milk", normalMilk.getFirst().getName(), "Name should match.");

    List<Grocery> normalCheese = foodStorage.findInStorage("cheese", false);
    assertEquals(1, normalCheese.size(), "Cheese should have 1 non-expired entry.");
    assertEquals("cheese", normalCheese.getFirst().getName(), "Name should match.");

    // Test for groceries not in storage
    List<Grocery> notFound = foodStorage.findInStorage("apple", false);
    assertTrue(notFound.isEmpty(), "Apple should not be found in normal storage.");
  }

  @Test
  void testFindGroceryInExpiredStorage() {
    // Setup: Add expired groceries and process them
    foodStorage.registerToStorage(new Grocery("Milk", 35.0, 4.0, "l", LocalDate.now().minusDays(1))); // Expired
    foodStorage.filterAndGroupExpiredGroceries();

    // Test expired storage lookup
    List<Grocery> expiredMilk = foodStorage.findInStorage("milk", true);
    assertEquals(1, expiredMilk.size(), "Milk should have 1 expired entry.");
    assertEquals("milk", expiredMilk.getFirst().getName(), "Name should match.");

    List<Grocery> expiredCheese = foodStorage.findInStorage("cheese", true);
    assertTrue(expiredCheese.isEmpty(), "Cheese should not have any expired entries.");

    // Test for groceries not in expired storage
    List<Grocery> notFoundExpired = foodStorage.findInStorage("apple", true);
    assertTrue(notFoundExpired.isEmpty(), "Apple should not be found in expired storage.");
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

    assertEquals(105.0, foodStorage.totalValueOfGroceries());
  }

  @Test
  void TotalValueOfExpiredGroceriesInStorage() {
    Grocery grocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now().minusDays(2));
    Grocery grocery2 = new Grocery("Cheese", 65.0, 2.0, "kg", LocalDate.now().minusDays(2));
    Grocery grocery3 = new Grocery("Meat", 35.0, 2.0, "kg", LocalDate.now().plusDays(4));

    foodStorage.registerToStorage(grocery1);
    foodStorage.registerToStorage(grocery2);
    foodStorage.registerToStorage(grocery3);

    foodStorage.filterAndGroupExpiredGroceries();

    assertEquals(100.0, foodStorage.totalValueOfExpiredGroceries());
  }

  @Test
  void testFilterAndGroupExpiredGroceries() {
    // Arrange
    Grocery expiredMilk = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now().minusDays(1));
    Grocery expiredCheese = new Grocery("Cheese", 50.0, 2.0, "kg", LocalDate.now().minusDays(2));
    Grocery freshBread = new Grocery("Bread", 20.0, 1.0, "stk", LocalDate.now().plusDays(3));

    foodStorage.registerToStorage(expiredMilk);
    foodStorage.registerToStorage(expiredCheese);
    foodStorage.registerToStorage(freshBread);

    // Act
    Map<String, List<Grocery>> expiredGroceries = foodStorage.filterAndGroupExpiredGroceries();

    // Assert
    assertEquals(2, expiredGroceries.size(), "There should be 2 categories of expired groceries");
    assertTrue(expiredGroceries.containsKey("milk"), "Expired groceries should contain 'Milk'");
    assertTrue(expiredGroceries.containsKey("cheese"), "Expired groceries should contain 'Cheese'");

    assertEquals(1, expiredGroceries.get("milk").size(), "There should be 1 expired Milk item");
    assertEquals(1, expiredGroceries.get("cheese").size(), "There should be 1 expired Cheese item");
  }

  @Test //UNFINISHED, REMEMBER TO FINISH TOMORROW!!!!!!!!!!!!!!!!!!!
  void TestingRemovalOfExpiredGroceries () {
    // Arrange
    Grocery expiredGrocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now().minusDays(1)); // Expired
    Grocery expiredGrocery2 = new Grocery("Cheese", 50.0, 2.0, "kg", LocalDate.now().minusDays(2)); // Expired
    Grocery freshGrocery = new Grocery("Bread", 20.0, 1.0, "stk", LocalDate.now().plusDays(3)); // Not expired

    foodStorage.registerToStorage(expiredGrocery1);
    foodStorage.registerToStorage(expiredGrocery2);
    foodStorage.registerToStorage(freshGrocery);

    // Act
    foodStorage.removeExpiredGroceries();

    // Assert
    assertTrue(foodStorage.findGroceriesByName("Milk").isEmpty(), "Milk should be removed as it is expired");
    assertTrue(foodStorage.findGroceriesByName("Cheese").isEmpty(), "Cheese should be removed as it is expired");

    List<Grocery> remainingBread = foodStorage.findGroceriesByName("Bread");
    assertEquals(1, remainingBread.size(), "Only Bread should remain in storage");
    assertEquals("bread", remainingBread.getFirst().getName(), "Remaining grocery should be Bread");
  }



  //NEGATIVE-TESTS----------------------------------------------------------------------------
  @Test
  void testIfRemovalAmountExceedsTotalAmount() {
    Grocery grocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now());
    Grocery grocery2 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now().plusDays(2));

    foodStorage.registerToStorage(grocery1);
    foodStorage.registerToStorage(grocery2);

    assertThrows(IllegalArgumentException.class, () -> foodStorage.removeAmountFromStorage("Milk", 11.0, "l"));
  }

  @Test
  void testMismatchingUnitsForRemoval() {
    Grocery grocery1 = new Grocery("Milk", 35.0, 5.0, "l", LocalDate.now());

    foodStorage.registerToStorage(grocery1);

    assertThrows(IllegalArgumentException.class, () -> foodStorage.removeAmountFromStorage("Milk", 4.0, "g"));

  }

  @Test
  public void testRegisterToStorageNullGrocery() {
    FoodStorage foodStorage = new FoodStorage();

    assertThrows(IllegalArgumentException.class, () -> foodStorage.registerToStorage(null), "Should throw IllegalArgumentException when adding null grocery");
  }

  @Test
  public void testRemoveAmountFromStorageInvalidGrocery() {
    FoodStorage foodStorage = new FoodStorage();

    foodStorage.registerToStorage(new Grocery("Apple", 2.0, 1.0, "kg", LocalDate.of(2024, 12, 31)));

    assertThrows(IllegalArgumentException.class, () -> foodStorage.removeAmountFromStorage("Banana", 1.0, "kg"), "Should throw IllegalArgumentException when removing non-existent grocery");

    assertThrows(IllegalArgumentException.class, () -> foodStorage.removeAmountFromStorage("", 1.0, "kg"), "Should throw IllegalArgumentException when removing grocery of empty String");

    assertThrows(IllegalArgumentException.class, () -> foodStorage.removeAmountFromStorage(null, 1.0, "kg"), "Should throw IllegalArgumentException when removing null grocery");
  }

  @Test
  public void testRemoveAmountFromStorageInvalidAmount() {
    FoodStorage foodStorage = new FoodStorage();

    foodStorage.registerToStorage(new Grocery("Apple", 2.0, 1.0, "kg", LocalDate.of(2024, 12, 31)));

    assertThrows(IllegalArgumentException.class, () -> foodStorage.removeAmountFromStorage("Apple", -1.0, "kg"), "Should throw IllegalArgumentException when removing a negative amount");
  }

  @Test
  void SearchForGroceryNotInStorage() {
    FoodStorage foodStorage = new FoodStorage();
    foodStorage.registerToStorage(new Grocery("Apple", 2.0, 1.0, "kg", LocalDate.of(2024, 12, 31)));

    List<Grocery> result = foodStorage.findInStorage("Banana", false);
    assertTrue(result.isEmpty(), "Should return an empty list when the name is an empty string.");

  }

  @Test
  void testFindInStorageNonExistentGrocery() {
    FoodStorage foodStorage = new FoodStorage();
    foodStorage.registerToStorage(new Grocery("Apple", 2.0, 1.0, "kg", LocalDate.of(2024, 12, 31)));

    List<Grocery> result = foodStorage.findInStorage("Banana", false);
    assertTrue(result.isEmpty(), "Should return an empty list when the grocery is not found in normal storage.");

    result = foodStorage.findInStorage("Banana", true);
    assertTrue(result.isEmpty(), "Should return an empty list when the grocery is not found in expired storage.");
  }

  @Test
  void testFindInStorageWithEmptyStorage() {
    FoodStorage foodStorage = new FoodStorage();

    List<Grocery> result = foodStorage.findInStorage("Apple", false);
    assertTrue(result.isEmpty(), "Should return an empty list when normal storage is empty.");

    result = foodStorage.findInStorage("Apple", true);
    assertTrue(result.isEmpty(), "Should return an empty list when expired storage is empty.");
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

    double totalValue = foodStorage.totalValueOfGroceries();
    assertEquals(0.0, totalValue, "Total value should be 0.0 when storage is empty");
  }

  @Test
  public void testMoveToExpiredGroceriesNoExpired() {
    FoodStorage foodStorage = new FoodStorage();

    foodStorage.registerToStorage(new Grocery("Apple", 2.0, 1.0, "kg", LocalDate.of(2025, 12, 31)));

    Map<String, List<Grocery>> expiredGroceries = foodStorage.filterAndGroupExpiredGroceries();
    assertTrue(expiredGroceries.isEmpty(), "Should return an empty map when there are no expired groceries");
  }

  @Test
  public void testIfGroceriesAreMovedToExpiredGroceries() {
    FoodStorage foodStorage = new FoodStorage();

    // Adding expired grocery
    foodStorage.registerToStorage(new Grocery("Apple", 2.0, 1.0, "kg", LocalDate.of(2023, 12, 31)));

    // Checking before calling moveToExpiredGroceries
    Map<String, List<Grocery>> expiredGroceries = foodStorage.filterAndGroupExpiredGroceries();
    foodStorage.removeExpiredGroceries();
    assertFalse(expiredGroceries.isEmpty(), "Should move expired groceries to the expired list");
    assertTrue(foodStorage.findGroceriesByName("Apple").isEmpty(), "Should remove expired groceries from storage");
  }

  @Test
  public void testTotalValueOfExpiredGroceries_NonExpired() {
    FoodStorage foodStorage = new FoodStorage();

    foodStorage.registerToStorage(new Grocery("Apple", 2.0, 1.0, "kg", LocalDate.of(2025, 12, 31)));

    double totalValue = foodStorage.totalValueOfExpiredGroceries();
    assertEquals(0.0, totalValue, "Total value of expired groceries should be 0.0 when there are no expired groceries");
  }

}
