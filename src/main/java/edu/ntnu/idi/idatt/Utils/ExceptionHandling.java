package edu.ntnu.idi.idatt.Utils;

import edu.ntnu.idi.idatt.Model.Grocery;
import edu.ntnu.idi.idatt.Model.Recipe;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public class ExceptionHandling {

  public static void validateName(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Name can not be a null or empty value");
    }

    if (name.trim().matches("-?\\d+(\\.\\d+)?")) {
      throw new IllegalArgumentException("name can not be a numerical value");
    }
  }

  public static void validatePrice(double price) {
    if (price <= 0) {
      throw new IllegalArgumentException("price must be greater than 0.");
    }
  }

  public static void validateAmount(double amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Amount must be greater than 0.");
    }
  }

  public static void validateUnit(String unit) {
    if (unit == null || unit.trim().isEmpty()) {
      throw new IllegalArgumentException("Unit can not be null or empty.");
    }
  }

  public static void validateExpiryDate(LocalDate expiryDate) {
    if (expiryDate == null) {
      throw new IllegalArgumentException("Expiry date cannot be null.");
    }
  }


  public static void validateAmountIncrease(double amountIncrease) {
    if (amountIncrease <= 0) {
      throw new IllegalArgumentException("Amount to increase with must be greater than 0.");
    }
  }

  public static void validateAmountDecrease(Grocery grocery, double amountDecrease) {
    if (amountDecrease <= 0) {
      throw new IllegalArgumentException("Amount to decrease with must be greater than 0.");
    }

    if (amountDecrease > grocery.getAmount()) {
      throw new IllegalArgumentException("Amount to decrease cannot be greater than the current amount.");
    }
  }


  //FOR FOODSTORAGE-CLASS----------------------------------------------------------------------------------------------------
  public static void nullGrocery(Grocery grocery) {
    if(grocery == null) {
      throw new IllegalArgumentException("Grocery cannot be null.");
    }
  }

  public static void validateStorageContainsItem(Map<String, List<Grocery>> storage, String itemName) {
    if (!storage.containsKey(itemName.toLowerCase()) || storage.get(itemName.toLowerCase()).isEmpty()) {
      throw new IllegalArgumentException("The grocery item '" + itemName + "' does not exist in storage.");
    }
  }

  public static void validateAmountToRemove(Map<String, List<Grocery>> storage, Double amount) {
    double totalAmount = storage.values().stream()
        .flatMap(List::stream)
        .mapToDouble(Grocery::getAmount)
        .sum();

    if (amount >= totalAmount) {
      throw new IllegalArgumentException("Amount to be removed cannot be greater than the current total amount of the grocery.");
    }
  }

  //FOR RECIPE CLASS---------------------------------------------------
  public static void validateRecipe(Recipe recipe) {
    if (recipe == null) {
      throw new IllegalArgumentException("Recipe cannot be null.");
    }
  }


  public static void validateRecipeInRecipeBook(Recipe recipe, List<Recipe> recipes) {
    if (recipe == null) {
      throw new IllegalArgumentException("Recipe cannot be null.");
    }
    if (recipes.stream().anyMatch(r -> r.getNameOfRecipe().equalsIgnoreCase(recipe.getNameOfRecipe()))) {
      throw new IllegalArgumentException("Recipe with the same name already exists in the cookbook.");
    }
  }
}


