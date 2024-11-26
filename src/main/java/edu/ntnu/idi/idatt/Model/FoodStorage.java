package edu.ntnu.idi.idatt.Model;

import edu.ntnu.idi.idatt.Utils.ExceptionHandling;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class representing a food storage system that can manage groceries by adding, removing,
 * sorting, and calculating the total value of stored groceries.
 */
public class FoodStorage {

  /**
   * A map to store groceries by their name, where each grocery name maps to a list of grocery items.
   */
  private Map<String, List<Grocery>> storage = new HashMap<>();


  /**
   * Adds a grocery item to the storage.
   * If the grocery's name does not exist as a key in the storage, a new entry is created
   * with the name as the key and an empty list as the value.
   * <p>
   * If the key already exists:
   * <ul>
   *   <li>If a grocery with the same name and expiry date exists in the list, their amounts are combined.</li>
   *   <li>Otherwise, the new grocery is added to the list.</li>
   * </ul>
   * After adding or updating, the list of groceries is sorted by their earliest expiry date.
   * <p>
   *
   * @param groceryToAdd the grocery item to be added to the storage
   *
   * @throws IllegalArgumentException if the specified Grocery is null
   */
  public void registerToStorage(Grocery groceryToAdd) {
    ExceptionHandling.nullGrocery(groceryToAdd);

    List<Grocery> groceries = storage.computeIfAbsent(groceryToAdd.getName().toLowerCase(), k -> new ArrayList<>());

    // Use streams to check if there's an existing grocery item with the same expiry date.
    groceries.stream()
        .filter(g -> g.getExpiryDate().equals(groceryToAdd.getExpiryDate()))
        .findFirst()
        .ifPresentOrElse(
            existingGrocery -> existingGrocery.increaseAmount(groceryToAdd.getAmount()),
            () -> groceries.add(groceryToAdd));

    // Sort the groceries list by expiry date.
    groceries.sort(Comparator.comparing(Grocery::getExpiryDate));
  }


  /**
   * Removes a specified amount of a grocery from the storage.
   * <p>
   * The removal prioritizes items with the earliest expiry date first.
   * If the amount to remove exceeds the available quantity of an item,
   * it continues to the next item in the list until the amount is fully removed.
   * <p>
   * If all items of a grocery are removed, the grocery is also removed from the storage.
   *
   * @param groceryToRemove the grocery to remove, identified by its name
   * @param amount the amount to remove from the storage
   * @throws IllegalArgumentException if the specified grocery is not found in storage, the name is null or an empty String, and if the amount is <= 0
   */
  public void removeAmountFromStorage(String groceryToRemove, double amount) {
    ExceptionHandling.validateName(groceryToRemove);
    ExceptionHandling.validateAmount(amount);
    ExceptionHandling.validateStorageContainsItem(storage, groceryToRemove);


    String key = groceryToRemove.toLowerCase(); // Convert to lowercase for case-insensitive comparison
    List<Grocery> itemsToRemove = storage.get(key);
    Iterator<Grocery> it = itemsToRemove.iterator();

    while (it.hasNext() && amount > 0) {
      Grocery item = it.next();
      double currentAmount = item.getAmount();

      if (amount >= currentAmount) {
        amount -= currentAmount;
        it.remove();
      } else {
        item.decreaseAmount(amount);
        amount = 0;
      }
    }

    if (itemsToRemove.isEmpty()) {
      storage.remove(key);
      System.out.println("you are out of: " + groceryToRemove);
    }
  }

  /**
   * Converts the storage `HashMap` into a `TreeMap` to sort groceries alphabetically by their keys.
   * <p>
   * A `TreeMap` is used because it automatically maintains the keys in sorted order.
   * This method copies all entries from the `storage` map into a new `TreeMap` instance.
   *
   * @return a `TreeMap` containing the groceries sorted alphabetically by their keys
   */
  public Map<String, List<Grocery>> sortGroceries() {
    Map<String, List<Grocery>> sortedGroceries = new TreeMap<>();
    sortedGroceries.putAll(storage);
    return sortedGroceries;
  }

  /**
   * Checks if a specific grocery exists in the storage.
   * <p>
   * Searches through all stored groceries to find a match based on the provided grocery's details.
   *
   * @param groceryName the grocery to check for in the storage
   * @return the matching grocery if found, or an empty {@code ArrayList} if no match exists.
   *         Prints a message if no match is found.
   */
  public List<Grocery> inStorage(String groceryName) {
    ExceptionHandling.validateName(groceryName);

    String key = groceryName.toLowerCase(); // Convert to lowercase for case-insensitive comparison
    List<Grocery> foundGroceries = storage.getOrDefault(key, new ArrayList<>());

    if (foundGroceries.isEmpty()) {
      // Log a clear message if no groceries are found
      System.out.println("Grocery not found: " + groceryName);
    } else {
      // Print found groceries for clarity
      System.out.println("Found groceries for '" + groceryName + "':");
      foundGroceries.forEach(g -> System.out.println(g));
    }

    return foundGroceries;
  }

  /**
   * Returns a list of groceries that expire before the specified date.
   *
   * @param date the cutoff date for filtering groceries
   * @return a list of groceries expiring before the given date
   */
  //REMEMBER TO ADD LOGIC FOR IF RETURNING AN EMPTY LIST
  public List<Grocery> bestBefore(LocalDate date) {
    ExceptionHandling.validateExpiryDate(date);

    return storage.values().stream()
        .flatMap(List::stream)
        .filter(bestBefore -> bestBefore.getExpiryDate().isBefore(date))
        .toList();
  }

  /**
   * Computes the total monetary value of all groceries stored.
   *
   * <p>This method processes all grocery items within the `storage` map, which organizes
   * groceries into lists. The total value is calculated by summing the results of
   * {@link Grocery#getPrice()} for each grocery item. If the storage is empty,
   * the method returns {@code 0.0}.</p>
   *
   * @return the total value of all groceries as a {@code double}.
   */
  public double TotalValueOfGroceries() {
    return storage.values().stream()
        .flatMap(List::stream)
        .mapToDouble(Grocery::getPrice)
        .sum();
  }

  public Map<String, List<Grocery>> moveToExpiredGroceries() {
    Map<String, List<Grocery>> expiredGroceries = new HashMap<>(); // Map to hold expired groceries
    List<Grocery> listOfExpiredGroceries = new ArrayList<>(); // Temporary list to store expired groceries


    storage.values().stream()
        .flatMap(List::stream)
        .filter(Grocery::isExpired)
        .forEach(listOfExpiredGroceries::add);

    listOfExpiredGroceries.forEach(grocery -> {
      expiredGroceries.computeIfAbsent(grocery.getName().toLowerCase(), k -> new ArrayList<>()).add(grocery); // Convert name to lowercase
    });

    listOfExpiredGroceries.forEach(grocery -> {
      storage.values().forEach(list -> list.remove(grocery)); // Remove expired grocery from all lists in storage
    });

    return expiredGroceries;
  }


  public double TotalValueOfExpiredGroceries() {
    return storage.values().stream()
        .flatMap(List::stream)
        .filter(exp_g -> exp_g.isExpired())
        .mapToDouble(Grocery::getPrice)
        .sum();
  }

  /**
   * Generates a string representation of the storage content.
   * <p>
   * The string will contain a list of all grocery names, their amounts, and expiry dates.
   *
   * @return a formatted string displaying all stored groceries with details
   */
  @Override
  public String toString() {
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    StringBuilder sb = new StringBuilder();

    for (Map.Entry<String, List<Grocery>> entry : storage.entrySet()) {
      String groceryName = entry.getKey();
      List<Grocery> groceryList = entry.getValue();

      sb.append(groceryName.toUpperCase()).append(":\n");
      sb.append(String.format("%-20s %-17s %-1s\n", "Name", "Amount", "Expiry Date"));
      sb.append("---------------------------------------------------\n");

      for (Grocery grocery : groceryList) {
        sb.append(String.format("%-20s %.2f %-12s %-1s\n",
            grocery.getName(),
            grocery.getAmount(),
            grocery.getUnit(),
            dateFormat.format(grocery.getExpiryDate())));
      }
      sb.append("---------------------------------------------------\n");
      sb.append("\n");
    }

    return sb.toString();
  }

  /**
   * Retrieves a list of groceries from storage based on the given name.
   * <p>
   * This method returns a list of groceries associated with the specified name.
   * If the name is not found in storage, an empty list is returned.
   * </p>
   *
   * @param name the name of the grocery to search for
   * @return a list of groceries with the specified name, or an empty list if no groceries are found
   */
  public List<Grocery> getGroceriesByName(String name) {
    return storage.getOrDefault(name.toLowerCase(), new ArrayList<>());
  }

}
