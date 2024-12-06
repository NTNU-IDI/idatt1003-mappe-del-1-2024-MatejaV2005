package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.Model.FoodStorage;
import edu.ntnu.idi.idatt.Model.Grocery;
import edu.ntnu.idi.idatt.Model.IngredientDetail;
import edu.ntnu.idi.idatt.Model.Recipe;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

  private Recipe recipe;
  private FoodStorage storage;

  @BeforeEach
  void setUp() {
    storage = new FoodStorage();
    storage.registerToStorage(new Grocery("Spaghetti", 10.0, 500.0, "g", LocalDate.of(2024, 12, 31)));
    storage.registerToStorage(new Grocery("Ground Beef", 50.0, 300.0, "g", LocalDate.of(2024, 12, 31)));
    storage.registerToStorage(new Grocery("Tomato Sauce", 20.0, 200.0, "ml", LocalDate.of(2024, 12, 31)));

    Map<String, IngredientDetail> ingredients = new HashMap<>();
    ingredients.put("Spaghetti", new IngredientDetail(200.0, "g"));
    ingredients.put("Ground Beef", new IngredientDetail(300.0, "g"));
    ingredients.put("Tomato Sauce", new IngredientDetail(150.0, "ml"));
    ingredients.put("Onion", new IngredientDetail(50.0, "g"));

    recipe = new Recipe("Spaghetti Bolognese", "Classic Italian pasta dish with meat sauce",
        "Cook pasta, prepare sauce, mix and serve.", ingredients);

    recipe.setStorage(storage);
  }

  @Test
  void testCanMakeRecipe() {
    assertFalse(recipe.canMakeRecipe(), "Recipe should not be possible due to missing Onion");

    storage.registerToStorage(new Grocery("Onion", 5.0, 50.0, "g", LocalDate.of(2024, 12, 31)));

    assertTrue(recipe.canMakeRecipe(), "Recipe should now be possible");
  }

  @Test
  void testGetMissingIngredients() {
    List<String> missingIngredients = recipe.getIngredients().entrySet().stream()
        .filter(entry -> {
          String ingredientName = entry.getKey();
          IngredientDetail requiredDetail = entry.getValue();

          double availableAmount = storage.getGroceriesByName(ingredientName).stream()
              .mapToDouble(Grocery::getAmount)
              .sum();

          return availableAmount < requiredDetail.getAmount();
        })
        .map(Map.Entry::getKey)
        .toList();

    assertEquals(1, missingIngredients.size(), "There should be 1 missing ingredient");
    assertTrue(missingIngredients.contains("Onion"), "Missing ingredient should be 'Onion'");
  }


  @Test
  void testEmptyStorage() {
    FoodStorage emptyStorage = new FoodStorage();
    recipe.setStorage(emptyStorage);

    assertFalse(recipe.canMakeRecipe(), "Recipe should not be possible with empty storage.");
    List<String> missingIngredients = recipe.getIngredients().keySet().stream().toList();
    assertEquals(missingIngredients.size(), recipe.getIngredients().size(),
        "All ingredients should be missing when storage is empty.");
  }

  @Test
  void testPartiallyAvailableIngredient() {
    storage.removeAmountFromStorage("Spaghetti", 400.0, "g");
    assertFalse(recipe.canMakeRecipe(), "Recipe should not be possible with insufficient Spaghetti.");
  }

  @Test
  void testInvalidIngredientUnit() {
    assertThrows(IllegalArgumentException.class, () -> {
      new IngredientDetail(200, "Ounces");
    });

    assertThrows(IllegalArgumentException.class, () -> {
      new IngredientDetail(200, "");
    });

    assertThrows(IllegalArgumentException.class, () -> {
      new IngredientDetail(200, null);
    });
  }

  @Test
  void testNegativeIngredientAmount() {
    assertThrows(IllegalArgumentException.class, () -> {
      new IngredientDetail(-200, "g");
    });
  }


  // set-method throws exception
  @Test
  void validateAndSetName_ThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Recipe(" ", "test", "test", Map.of());
    });
  }

  @Test
  void validateAndSetIngredients_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Recipe("Empty Recipe", "This recipe has no ingredients.",
          "No process needed.", Map.of());
    });
  }
}
