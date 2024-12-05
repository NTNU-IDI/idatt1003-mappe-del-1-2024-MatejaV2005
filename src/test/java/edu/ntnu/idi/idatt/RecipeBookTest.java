package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.Model.FoodStorage;
import edu.ntnu.idi.idatt.Model.Grocery;
import edu.ntnu.idi.idatt.Model.IngredientDetail;
import edu.ntnu.idi.idatt.Model.Recipe;
import edu.ntnu.idi.idatt.Model.RecipeBook;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class RecipeBookTest {

  private RecipeBook recipeBook;
  private FoodStorage storage;

  @BeforeEach
  void setUp() {
    recipeBook = new RecipeBook();
    storage = new FoodStorage();

    storage.registerToStorage(new Grocery("Spaghetti", 10.0, 500.0, "g", LocalDate.now().plusDays(10)));
    storage.registerToStorage(new Grocery("Ground Beef", 50.0, 300.0, "g", LocalDate.now().plusDays(5)));
    storage.registerToStorage(new Grocery("Tomato Sauce", 20.0, 200.0, "ml", LocalDate.now().plusDays(15)));
  }

  @Test
  void testAddRecipeSuccessfully() {
    Recipe recipe = new Recipe("Spaghetti Bolognese", "A classic Italian dish",
        "Cook pasta, prepare sauce, mix and serve.",
        Map.of(
            "Spaghetti", new IngredientDetail(200.0, "g"),
            "Ground Beef", new IngredientDetail(300.0, "g"),
            "Tomato Sauce", new IngredientDetail(150.0, "ml")
        ));

    recipeBook.addRecipe(recipe);
    assertNotNull(recipeBook.getRecipe("Spaghetti Bolognese"));
    assertEquals("Spaghetti Bolognese", recipeBook.getRecipe("Spaghetti Bolognese").getNameOfRecipe());
  }

  @Test
  void testAddDuplicateRecipeThrowsException() {
    Recipe recipe = new Recipe("Spaghetti Bolognese", "A classic Italian dish",
        "Cook pasta, prepare sauce, mix and serve.",
        Map.of(
            "Spaghetti", new IngredientDetail(200.0, "g"),
            "Ground Beef", new IngredientDetail(300.0, "g"),
            "Tomato Sauce", new IngredientDetail(150.0, "ml")
        ));

    recipeBook.addRecipe(recipe);
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        recipeBook.addRecipe(recipe));
    assertEquals("Recipe with the same name already exists in the cookbook.", exception.getMessage());
  }

  @Test
  void testAddNullRecipeThrowsException() {
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        recipeBook.addRecipe(null));
    assertEquals("Recipe cannot be null.", exception.getMessage());
  }
}

