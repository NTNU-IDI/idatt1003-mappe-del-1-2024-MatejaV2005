package edu.ntnu.idi.idatt.Model;


import edu.ntnu.idi.idatt.Utils.ExceptionHandling;
import edu.ntnu.idi.idatt.Utils.UnitConverter;
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

  /**
   * A map to store the expired groceries by their name, where each grocery name maps to a list of grocery items.
   */
  Map<String, List<Grocery>> expiredStorage = new HashMap<>(); // Map to hold expired groceries


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

    // Use streams to check if there's an existing grocery item with the same expiry date and unit.
    groceries.stream()
        .filter(g -> g.getExpiryDate().equals(groceryToAdd.getExpiryDate()) && g.getUnit().equals(groceryToAdd.getUnit()))
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
   * @param unit unit for the amount to be removed
   * @throws IllegalArgumentException if the specified grocery is not found in storage, the name is null or an empty String
   * @throws IllegalArgumentException if the amount to remove is greater than the total amount of a specified grocery in storage
   */
  public void removeAmountFromStorage(String groceryToRemove, double amount, String unit) {
    ExceptionHandling.validateName(groceryToRemove);
    ExceptionHandling.validateAmount(amount);
    ExceptionHandling.validateStorageContainsItem(storage, groceryToRemove);
    ExceptionHandling.validateAmountToRemove(storage, amount);

    amount = UnitConverter.ConvertUnitAmount(amount, unit);


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
    Map<String, List<Grocery>> sortedStorage = new TreeMap<>(storage);
    return sortedStorage;
  }

  /**
   * Searches for a specific grocery in either the main storage or expired storage based on the provided flag.
   * <p>
   * If the grocery is found, it prints a message indicating whether it was found in the main storage or expired storage.
   * If no matching grocery is found, a "not found" message is printed.
   * </p>
   *
   * @param groceryName the name of the grocery to search for.
   * @param searchExpired a boolean indicating whether to search in expired storage (true) or main storage (false).
   * @return a list of matching groceries from the selected storage. If no matches are found, an empty list is returned.
   */
  public List<Grocery> findInStorage(String groceryName, boolean searchExpired) {
    ExceptionHandling.validateName(groceryName);

    String key = groceryName.toLowerCase();
    Map<String, List<Grocery>> targetStorage = searchExpired ? expiredStorage : storage; // searches the corresponding storage based on boolean value

    List<Grocery> foundGroceries = targetStorage.getOrDefault(key, new ArrayList<>()).stream()
        .filter(grocery -> searchExpired || !grocery.getExpiryDate().isBefore(LocalDate.now()))
        .toList();

    String storageType = searchExpired ? "expired storage" : "storage";
    if (foundGroceries.isEmpty()) {
      System.out.printf("No groceries found in %s: %s%n \n", storageType, groceryName);
    } else {
      System.out.printf("Found %s for |%s|:%n", searchExpired ? "expired groceries" : "groceries in storage", groceryName);
      System.out.println("--------------------------------------------");
      foundGroceries.forEach(System.out::println);
      System.out.println();
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

  public List<Grocery> bestBefore(LocalDate date) {
    ExceptionHandling.validateExpiryDate(date);

    List<Grocery> beforeDate =
        storage.values().stream()
            .flatMap(List::stream)
            .filter(bestBefore -> bestBefore.getExpiryDate().isBefore(date))
            .toList();

    if (beforeDate.isEmpty()) {
      System.out.println("No groceries found with a best-before date for the following date: " + date);
    } else {
      System.out.println("Groceries with a best-before date before " + date + ":");
      beforeDate.forEach(grocery -> System.out.println(grocery.toString()));
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

  /**
   * Removes all expired groceries from the storage.
   *
   * <p>This method iterates through each grocery list in the {@code storage} map, checking if each
   * grocery item is expired using {@link Grocery#isExpired()}. Expired items are removed from the list
   * using an {@link Iterator} to ensure safe modification during iteration.</p>
   *
   * <p>After execution, the storage will no longer contain expired groceries, but the overall structure
   * of the {@code storage} map remains unchanged.</p>
   */
  public void removeExpiredGroceries() {
    //Loop variable groceryList for each list in storage.
    for (List<Grocery> groceryList : storage.values()) {
      groceryList.removeIf(Grocery::isExpired); //TODO: REMEMBER TO CHECK THIS OUT
    }
  }

  /**
   * Filters and groups expired groceries by name.
   *
   * <p>This method scans the {@code storage} map to identify groceries that have expired,
   * as determined by {@link Grocery#isExpired()}. All expired items are then grouped by their
   * lowercase name in the {@code expiredGroceries} map, ensuring case-insensitive organization.
   * Each key in the map represents a grocery name (in lowercase), and the value is a list of
   * expired items with that name.</p>
   *
   * <p>This method ensures that expired groceries are efficiently organized for further
   * processing, while leaving the original {@code storage} map unmodified.</p>
   *
   * @return a map where keys are grocery names (in lowercase) and values are lists of expired groceries.
   */

  public Map<String, List<Grocery>> FilterAndGroupExpiredGroceries() {
    List<Grocery> listOfExpiredGroceries = new ArrayList<>(); // Temporary list to store expired groceries

    storage.values().stream()
        .flatMap(List::stream)
        .filter(Grocery::isExpired)
        .forEach(listOfExpiredGroceries::add);

    listOfExpiredGroceries.forEach(grocery -> {
      expiredStorage.computeIfAbsent(grocery.getName().toLowerCase(), k -> new ArrayList<>()).add(grocery); // Convert name to lowercase
    });

    return expiredStorage;
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
  public double totalValueOfExpiredGroceries() {
    return expiredStorage.values().stream()
        .flatMap(List::stream)
        .filter(Grocery::isExpired)
        .mapToDouble(Grocery::getPrice)
        .sum();
  }


  //DISPLAY-METHODS____________________________________________________________________________
  /**
   * Formats a map of groceries into a readable string representation with a table-like structure.
   * Each grocery name is presented as a header, followed by its details (name, amount, unit, and expiry date)
   * in a tabular format.
   *
   * <p><b>Example Output:</b>
   * <pre>
   * APPLE:
   * Name                 Amount           Expiry Date
   * ---------------------------------------------------
   * Apple                1.00            stk         15-12-2024
   * Apple                2.50            kg          10-11-2024
   * ---------------------------------------------------
   * </pre>
   *
   * @param groceries a map where the key is the grocery name and the value is a list of {@link Grocery} objects.
   *                  Each list contains all grocery items for that specific name.
   * @return a formatted string representing the grocery information in a structured format.
   */
  public String formatGroceries(Map<String, List<Grocery>> groceries) {
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    StringBuilder sb = new StringBuilder();

    // Iterate through each entry in the groceries map
    for (Map.Entry<String, List<Grocery>> entry : groceries.entrySet()) {
      String groceryName = entry.getKey();
      List<Grocery> groceryList = entry.getValue();

      // Only print the table for groceries if there are items in the list
      if (!groceryList.isEmpty()) {
        sb.append(groceryName.toUpperCase()).append(":\n");
        sb.append(String.format("%-20s %-17s %-1s\n", "Name", "Amount", "Expiry Date"));
        sb.append("---------------------------------------------------\n");

        // Iterate through the list of groceries and print each one
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
  public String toString(boolean sorted) {
    Map<String, List<Grocery>> groceriesToDisplay = sorted ? sortGroceries() : storage;
    return formatGroceries(groceriesToDisplay);
  }

  /**
   * Displays all expired groceries in a formatted string.
   *
   * <p>This method filters and groups expired groceries using {@link #FilterAndGroupExpiredGroceries()},
   * and then formats the resulting map of expired groceries using {@link #formatGroceries(Map)}.</p>
   *
   * @return a formatted string representing all expired groceries, grouped by name.
   */
  public String DisplayExpiredGroceries() {
    Map<String, List<Grocery>> expiredGroceriesToDisplay = FilterAndGroupExpiredGroceries();
    return formatGroceries(expiredGroceriesToDisplay);
  }

  //HELPER METHOD--------------------------------------------------------------
  /**
   * Helper method for getting specific groceries as a list
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
