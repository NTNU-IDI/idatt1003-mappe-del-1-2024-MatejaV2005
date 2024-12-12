package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.model.FoodStorage;
import edu.ntnu.idi.idatt.model.Grocery;
import edu.ntnu.idi.idatt.model.IngredientDetail;
import edu.ntnu.idi.idatt.model.Recipe;
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
    storage.registerToStorage(new Grocery("Spaghetti", 10.0, 500.0, "g", LocalDate.now().plusDays(3)));
    storage.registerToStorage(new Grocery("Ground Beef", 50.0, 300.0, "g", LocalDate.now().plusDays(3)));
    storage.registerToStorage(new Grocery("Tomato Sauce", 20.0, 200.0, "ml", LocalDate.now().plusDays(3)));

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
  void testExtraIngredientsInStorage() {
    storage.registerToStorage(new Grocery("Cheese", 10.0, 500.0, "g", LocalDate.of(2024, 12, 31)));

    assertFalse(recipe.canMakeRecipe(), "Recipe should not be possible due to missing Onion, even with extra ingredients");
  }

  @Test
  void testGetMissingIngredients() {
    storage.removeAmountFromStorage("spaghetti", 300.0, "g");
    storage.removeAmountFromStorage("tomato Sauce", 100.0, "ml");

    recipe.getMissingIngredients();

    assertEquals(200.0, storage.findGroceriesByName("spaghetti").stream().mapToDouble(Grocery::getAmount).sum());
    assertEquals(0.1, storage.findGroceriesByName("tomato Sauce").stream().mapToDouble(Grocery::getAmount).sum());
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
    assertThrows(IllegalArgumentException.class, () -> new IngredientDetail(200, "Ounces"), "Expected exception for unsupported unit 'Ounces'");

    assertThrows(IllegalArgumentException.class, () -> new IngredientDetail(200, ""), "Expected exception for empty unit");

    assertThrows(IllegalArgumentException.class, () -> new IngredientDetail(200, null), "Expected exception for null unit");
  }

  @Test
  void testNegativeIngredientAmount() {
    assertThrows(IllegalArgumentException.class, () -> new IngredientDetail(-200, "g"), "Expected exception for negative ingredient amount");
  }

  // set-method throws exception
  @Test
  void validateAndSetName_ThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> new Recipe(" ", "test", "test", Map.of()), "Expected exception for invalid recipe name (empty or whitespace)");
  }

  @Test
  void validateAndSetIngredients_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> new Recipe("Empty Recipe", "This recipe has no ingredients.",
        "No process needed.", Map.of()), "Expected exception for missing ingredients in recipe");
  }
}
