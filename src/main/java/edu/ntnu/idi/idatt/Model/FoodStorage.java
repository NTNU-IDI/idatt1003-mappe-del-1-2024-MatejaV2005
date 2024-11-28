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
 * sorting, and calculating the total value of stored groceries, as well as managing expired groceries
 */
public class FoodStorage {

  /**
   * A map to store groceries by their name, where each grocery name maps to a list of grocery items.
   */
  private Map<String, List<Grocery>> storage = new HashMap<>();

  Map<String, List<Grocery>> expiredGroceries = new HashMap<>(); // Map to hold expired groceries


  /**
   * Adds a grocery item to the storage.
   * If the grocery's name does not exist as a key in the storage, a new entry is created
   * with the name as the key and an empty list as the value, where the grocery is then added.
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
   * If the amount to remove exceeds the available quantity of a single item,
   * it continues to the next item in the list until the amount is fully removed.
   * <p>
   * If all items of a grocery are removed, the grocery is also removed from the storage.
   *
   * @param groceryToRemove the grocery to remove, identified by its name
   * @param amount the amount to remove from the storage
   * @throws IllegalArgumentException if the specified grocery is not found in storage, the name is null or an empty String
   * @throws IllegalArgumentException if the amount to remove is greater than the total amount of a specified grocery in storage
   */
  public void removeAmountFromStorage(String groceryToRemove, double amount) {
    ExceptionHandling.validateName(groceryToRemove);
    ExceptionHandling.validateAmount(amount);
    ExceptionHandling.validateStorageContainsItem(storage, groceryToRemove);
    ExceptionHandling.validateAmountToRemove(storage, amount);


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
   * @return all the items of the matching grocery name if found.
   *         otherwise returns a message to inform if no match exists.
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
      System.out.println("Found groceries for |" + groceryName + "|:");
      System.out.println("--------------------------------------------");
      foundGroceries.forEach(g -> System.out.println(g));
    }

    return foundGroceries;
  }

  /**
   * Returns a list of groceries that expire before the specified date.
   *
   * @param date the cutoff date for filtering groceries
   * @return a list of groceries expiring before the given date.
   *         if no groceries aer found before the best-before date, prints a message
   */
  //REMEMBER TO ADD LOGIC FOR IF RETURNING AN EMPTY LIST
  public List<Grocery> bestBefore(LocalDate date) {
    ExceptionHandling.validateExpiryDate(date);

    List<Grocery> beforeDate =
        storage.values().stream()
        .flatMap(List::stream)
        .filter(bestBefore -> bestBefore.getExpiryDate().isBefore(date))
        .toList();

    if (beforeDate.isEmpty()) {
      System.out.println("No groceries found with a best-before date for following: " +date);
    }

    return beforeDate;
  }
  //REMEMBER TO ADD A TEST FOR CHECKING IF IT RETURNS MESSAGE IF NO GROCERIES FOUND BEFORE GIVEN DATE

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


  public void removeExpiredGroceries() {
    //Loop variable groceryList for each list in storage.
    for (List<Grocery> groceryList : storage.values()) {
      Iterator<Grocery> it = groceryList.iterator();

      while (it.hasNext()) {
        Grocery grocery = it.next();

        if (grocery.isExpired()) {
          it.remove();
        }
      }
    }
  }

  /**
   * Moves all expired groceries from the storage to a new {@code Map} of expired groceries.
   *
   * <p>This method identifies and removes expired groceries from the current {@code storage} and organizes
   * them into a new {@code Map}. The map's keys are the lowercase names of the expired groceries,
   * and the values are lists of corresponding {@code Grocery} objects.
   *
   * <p>Steps performed:
   * <ol>
   *   <li>Filters all expired groceries from the {@code storage} using {@link Grocery#isExpired()}.
   *   <li>Adds the expired groceries to a temporary list ({@code listOfExpiredGroceries}).
   *   <li>Populates the {@code expiredGroceries} map:
   *       <ul>
   *         <li>The name of each grocery (in lowercase) is used as the key.</li>
   *         <li>An {@code ArrayList} of all occurrences of the expired grocery is added as the value.</li>
   *       </ul>
   * </ol>
   *
   * @return a {@code Map<String, List<Grocery>>} containing expired groceries, grouped by name
   *         (in lowercase).
   */
  //TODO: TRY TO SPLIT UP METHOD MORE
  public Map<String, List<Grocery>> FilterAndGroupExpiredGroceries() {
    List<Grocery> listOfExpiredGroceries = new ArrayList<>(); // Temporary list to store expired groceries

    storage.values().stream()
        .flatMap(List::stream)
        .filter(Grocery::isExpired)
        .forEach(listOfExpiredGroceries::add);

    listOfExpiredGroceries.forEach(grocery -> {
      expiredGroceries.computeIfAbsent(grocery.getName().toLowerCase(), k -> new ArrayList<>()).add(grocery); // Convert name to lowercase
    });

    removeExpiredGroceries();


    return expiredGroceries;
  }

  /**
   * Calculates the total value of all expired groceries in the storage.
   *
   * <p>Filters expired groceries from the {@code storage} using {@link Grocery#isExpired()},
   * retrieves their prices using {@link Grocery#getPrice()}, and sums them up to return the
   * total value of expired groceries.
   *
   * @return the total sum of prices of all expired groceries as a {@code double}.
   */
  public double TotalValueOfExpiredGroceries() {
    return storage.values().stream()
        .flatMap(List::stream)
        .filter(exp_g -> exp_g.isExpired())
        .mapToDouble(Grocery::getPrice)
        .sum();
  }


  //DISPLAY-METHODS____________________________________________________________________________
  //TODO: REWRITE JAVADOC
  /**
   * Generates a string representation of the storage content.
   * <p>
   * The string will contain a list of all grocery names, their amounts, and expiry dates.
   *
   * @param groceries decides which storage will be formatted
   *
   * @return a formatted string displaying all stored groceries with details
   */


  public String formatGroceries(Map<String, List<Grocery>> groceries) {
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    StringBuilder sb = new StringBuilder();

    for (Map.Entry<String, List<Grocery>> entry : groceries.entrySet()) {
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
   * Returns a string representation of the grocery storage.
   * <p>
   * If {@code sorted} is {@code true}, the groceries are displayed in alphabetical order by name.
   * Otherwise, they are displayed in their original order.
   *
   * @param sorted whether to sort the groceries by name
   * @return a formatted string of all groceries with their details
   */
  public String toString (boolean sorted) {
    Map<String, List<Grocery>> groceriesToDisplay = sorted ? sortGroceries() : storage;
    return formatGroceries(groceriesToDisplay);
  }


  /**
   * Helper method for testing
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
