package edu.ntnu.idi.idatt;

import static org.junit.jupiter.api.Assertions.*;


import edu.ntnu.idi.idatt.Model.FoodStorage;
import edu.ntnu.idi.idatt.Model.Grocery;
import java.time.LocalDate;
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
    assertEquals(1.5, milkGroceries.get(0).getAmount());
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
    foodStorage.removeAmountFromStorage(grocery1, );

  }

}
