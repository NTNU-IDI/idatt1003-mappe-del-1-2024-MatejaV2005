package edu.ntnu.idi.idatt;

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
   * Adds a grocery to the storage. If the grocery's name does not exist as a key,
   * creates a new entry with the name as the key and an ArrayList as the value.
   * <p>
   * If the key already exists, adds the grocery to the corresponding list.
   * If an identical grocery (same name, expiry date) already exists, sums their amounts.
   * <p>
   * The groceries in each list are sorted by their earliest expiry date.
   *
   * @param groceryToAdd the grocery to be added to the storage
   */
  public void registerToStorage(Grocery groceryToAdd) {
    storage.computeIfAbsent(groceryToAdd.getName(), toList -> new ArrayList<>()).add(groceryToAdd);

    // sorts the grocery items in each corresponding list by the earliest date to expiry
    storage.get(groceryToAdd.getName())
        .sort(Comparator.comparing(Grocery::getExpiryDate));

    storage.values().stream()
        .flatMap(List::stream)
        .filter(g -> groceryToAdd.equals(g))
        .findFirst()
        .ifPresent(g -> g.increaseAmount(groceryToAdd.getAmount()));
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
   * @throws IllegalArgumentException if the specified grocery is not found in storage
   */
  public void removeAmountFromStorage(Grocery groceryToRemove, double amount) {
    List<Grocery> itemsToRemove = storage.get(groceryToRemove.getName());
    Iterator<Grocery> it = itemsToRemove.iterator();

    while (it.hasNext() && amount > 0) {
      Grocery item = it.next();
      double currentAmount = item.getAmount();

      if (currentAmount <= amount) {
        amount -= currentAmount;
        it.remove();
      } else {
        item.decreaseAmount(amount);
        amount = 0;
      }
    }

    if (itemsToRemove.isEmpty()) {
      storage.remove(groceryToRemove.getName());
      System.out.println("nothing to remove, you are out of: " + groceryToRemove.getName());
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
   * @param groceryInStorage the grocery to check for in the storage
   * @return the matching grocery if found, or {@code null} if no match exists.
   *         Prints a message if no match is found.
   */
  public Grocery inStorage(Grocery groceryInStorage) {
    Grocery foundGrocery = storage.values().stream()
        .flatMap(List::stream)
        .filter(inStorage -> inStorage.equals(groceryInStorage))
        .findFirst()
        .orElse(null);

    if (foundGrocery == null) {
      System.out.println("Grocery not found: " + groceryInStorage.getName());
    }

    return foundGrocery;
  }

  /**
   * Returns a list of groceries that expire before the specified date.
   *
   * @param date the cutoff date for filtering groceries
   * @return a list of groceries expiring before the given date
   */
  public List<Grocery> bestBefore(LocalDate date) {
    return storage.values().stream()
        .flatMap(List::stream)
        .filter(bestBefore -> bestBefore.getExpiryDate().isBefore(date))
        .toList();
  }

  /**
   * Calculates the total value of all groceries in storage.
   *
   * @return the total value of all groceries
   */
  public double TotalValueOfGroceries() {
    return storage.values().stream()
        .flatMap(List::stream)
        .mapToDouble(Grocery::totalValueOfGrocery)
        .sum();
  }

  //REMEMBER TO ADD:
  //METHOD THAT CALCULATES ALL THE EXPIRED GROCERIES AND ADDS IT TO AN EXPIRED LIST/MAP
  //CALCULATES THE TOTAL VALUE OF THE GROCERIES IN THE EXPIRED LIST/MAP

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
}
