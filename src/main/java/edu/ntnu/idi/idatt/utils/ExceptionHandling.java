package edu.ntnu.idi.idatt.utils;

import edu.ntnu.idi.idatt.model.FoodStorage;
import edu.ntnu.idi.idatt.model.Grocery;
import edu.ntnu.idi.idatt.model.IngredientDetail;
import edu.ntnu.idi.idatt.model.Recipe;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Utility class for validating inputs and ensuring data integrity across the application.
 *
 * <p>Provides static methods to validate common fields (e.g., names, amounts, units)
 * and enforce constraints for {@link edu.ntnu.idi.idatt.model.Grocery},
 * {@link edu.ntnu.idi.idatt.model.FoodStorage}, and {@link edu.ntnu.idi.idatt.model.Recipe}.
 *
 * <p>Throws {@link IllegalArgumentException} with descriptive messages when validation fails.
 *
 * <p>Examples of functionality:
 * <ul>
 *   <li>Ensure non-null and valid names, prices, and units.</li>
 *   <li>Validate sufficient quantities for operations.</li>
 *   <li>Enforce unique recipes in a recipe book.</li>
 * </ul>
 */

public class ExceptionHandling {

  /**
   * Validates the provided name to ensure it meets the required conditions. The name must not be
   * null, empty, or consist solely of numerical values.
   *
   * @param name the name to validate
   * @throws IllegalArgumentException if the name is null, empty, or a numerical value
   */

  public static void validateName(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Name can not be a null or empty value");
    }

    if (name.trim().matches("-?\\d+(\\.\\d+)?")) {
      throw new IllegalArgumentException("name can not be a numerical value");
    }
  }

  /**
   * Ensures the given price is valid by verifying it is a positive value.
   *
   * @param price the price to validate
   * @throws IllegalArgumentException if the price is less than or equal to zero, as only positive
   *                                  values are permitted.
   */

  public static void validatePrice(double price) {
    if (price <= 0) {
      throw new IllegalArgumentException("price must be greater than 0.");
    }
  }

  /**
   * Validates that the provided amount is greater than zero.
   *
   * @param amount the amount to validate
   * @throws IllegalArgumentException if the amount is less than or equal to zero, as only positive
   *                                  values are allowed.
   */

  public static void validateAmount(double amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Amount must be greater than 0.");
    }
  }

  /**
   * Validates that the provided unit is not null or empty.
   *
   * @param unit the unit to validate
   * @throws IllegalArgumentException if the unit is null or an empty string, as a valid unit is
   *                                  required.
   */

  public static void validateUnit(String unit) {
    if (unit == null || unit.trim().isEmpty()) {
      throw new IllegalArgumentException("Unit can not be null or empty.");
    }
  }

  /**
   * Validates that the provided expiry date is not null.
   *
   * @param expiryDate the expiry date to validate
   * @throws IllegalArgumentException if the expiry date is null, as a valid expiry date is
   *                                  required.
   */

  public static void validateExpiryDate(LocalDate expiryDate) {
    if (expiryDate == null) {
      throw new IllegalArgumentException("Expiry date cannot be null.");
    }
  }

  /**
   * Validates that the specified amount to increase is greater than zero.
   *
   * @param amountIncrease the amount to validate
   * @throws IllegalArgumentException if the amount is less than or equal to zero, as increasing by
   *                                  zero or a negative value is not allowed.
   */

  public static void validateAmountIncrease(double amountIncrease) {
    if (amountIncrease <= 0) {
      throw new IllegalArgumentException("Amount to increase with must be greater than 0.");
    }
  }

  /**
   * Validates that the specified amount to decrease is valid for the given grocery item.
   *
   * @param grocery        the grocery item from which the amount will be decreased
   * @param amountDecrease the amount to validate
   * @throws IllegalArgumentException if the amount to decrease is less than or equal to zero, or
   *                                  greater than the current amount in the grocery item
   */

  public static void validateAmountDecrease(Grocery grocery, double amountDecrease) {
    if (amountDecrease <= 0) {
      throw new IllegalArgumentException("Amount to decrease with must be greater than 0.");
    }

    if (amountDecrease > grocery.getAmount()) {
      throw new IllegalArgumentException(
          "Amount to decrease cannot be greater than the current amount.");
    }
  }

  /**
   * Validates that the provided grocery is not null.
   *
   * @param grocery the grocery item to validate
   * @throws IllegalArgumentException if the grocery is null
   */

  public static void nullGrocery(Grocery grocery) {
    if (grocery == null) {
      throw new IllegalArgumentException("Grocery cannot be null.");
    }
  }

  /**
   * Validates that the provided storage is not null.
   *
   * @param storage the food storage to validate
   * @throws IllegalArgumentException if the storage is null
   */

  public static void nullStorage(FoodStorage storage) {
    if (storage == null) {
      throw new IllegalArgumentException("Storage cannot be null.");
    }
  }

  /**
   * Validates that the provided recipe is not null.
   *
   * @param recipe the recipe to validate
   * @throws IllegalArgumentException if the recipe is null
   */
  public static void nullRecipe(Recipe recipe) {
    if (recipe == null) {
      throw new IllegalArgumentException("Recipe cannot be null.");
    }
  }

  /**
   * Validates that the specified item exists in the storage.
   *
   * @param storage  the storage map to check
   * @param itemName the name of the grocery item to validate
   * @throws IllegalArgumentException if the item does not exist in storage or is empty
   */

  // Suppressed line length check as breaking the method definition reduces readability
  @SuppressWarnings("checkstyle:LineLength")
  public static void validateStorageContainsItem(Map<String, List<Grocery>> storage, String itemName) {
    if (!storage.containsKey(itemName.toLowerCase()) || storage.get(itemName.toLowerCase())
        .isEmpty()) {
      throw new IllegalArgumentException(
          "The grocery item '" + itemName + "' does not exist in storage.");
    }
  }

  /**
   * Validates that the amount to remove is within the allowable range for the given item in
   * storage.
   *
   * @param storage        the storage map to check
   * @param amountToRemove the amount to validate
   * @param itemName       the name of the grocery item to validate against
   * @throws IllegalArgumentException if the amount to remove exceeds the total available amount
   */

  public static void validateAmountToRemove(Map<String, List<Grocery>> storage, Double amountToRemove, String itemName) {
    double totalAmount = storage.values().stream()
        .flatMap(List::stream)
        .filter(g -> g.getName().equalsIgnoreCase(itemName))
        .mapToDouble(Grocery::getAmount)
        .sum();

    if (amountToRemove > totalAmount) {
      throw new IllegalArgumentException(
          "Amount to be removed cannot be greater than the current total amount of the grocery.");
    }
  }

  /**
   * Validates that the unit is compatible with the existing unit for a given grocery item.
   *
   * @param unit        the unit to validate
   * @param groceryName the name of the grocery item to check
   * @param storage     the storage map containing the grocery items
   * @throws IllegalArgumentException if the unit is incompatible with the grocery's standard unit
   */

  // Suppressed line length check as breaking the method definition reduces readability
  @SuppressWarnings("checkstyle:LineLength")
  public static void validateUnitCompatibility(String unit, String groceryName, Map<String, List<Grocery>> storage) {
    String standardUnit = UnitConverter.getStandardUnit(unit);
    List<Grocery> groceries = storage.get(groceryName.toLowerCase());

    String groceryStandardUnit = UnitConverter.getStandardUnit(groceries.get(0).getUnit());

    if (!standardUnit.equals(groceryStandardUnit)) {
      throw new IllegalArgumentException(
          "Unit mismatch: Cannot use '" + unit + "' with groceries measured in '"
              + groceryStandardUnit + "'.");
    }
  }

  /**
   * Validates that the provided ingredients map is not null or empty.
   *
   * @param ingredients the map of ingredients to validate
   * @throws IllegalArgumentException if the ingredients map is null or empty
   */

  public static void validateIngredients(Map<String, IngredientDetail> ingredients) {
    if (ingredients == null || ingredients.isEmpty()) {
      throw new IllegalArgumentException("Ingredients map cannot be null or empty.");
    }
  }

  /**
   * Validates that the recipe does not already exist in the provided recipe book.
   *
   * @param recipe the recipe to check
   * @param recipes the list of existing recipes
   * @throws IllegalArgumentException if a recipe with the same name already exists
   */

  public static void validateExistingRecipe(Recipe recipe, List<Recipe> recipes) {
    if (recipes.stream()
        .anyMatch(rcp -> rcp.getNameOfRecipe().equalsIgnoreCase(recipe.getNameOfRecipe()))) {
      throw new IllegalArgumentException(
          "Recipe with the same name already exists in the cookbook.");
    }
  }
}


