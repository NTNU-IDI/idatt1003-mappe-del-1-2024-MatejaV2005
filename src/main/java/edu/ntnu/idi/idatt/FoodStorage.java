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



public class FoodStorage {
  private Map<String, List<Grocery>> storage = new HashMap<>(); /*a hashmap with a String key and a List (defined as list for flecibilty) as values                                           The idea here is that if the user adds for instance milks with different expiry dates, but both have the Key milk, they won't override eachother*/


  /**
   * adds a grocery to storage based on name, if already in storage; increases amount
   * @param groceryToAdd name of grocery to be added to storage
   */
    public void registerToStorage(Grocery groceryToAdd) {
      storage.computeIfAbsent(groceryToAdd.getName(), toList -> new ArrayList<>()).add(groceryToAdd);

      // sorts the grocery items in each corresponding list by the earlist date to expiry
      storage.get(groceryToAdd.getName())
          .sort(Comparator.comparing(Grocery::getExpiryDate));

      storage.values().stream()
          .flatMap(List::stream)
          .filter(g -> groceryToAdd.equals(g))
          .findFirst()
          .ifPresent(g -> g.increaseAmount(groceryToAdd.getAmount()));
    }

    // MAYBE: use stream() logic here?


  /**
   * Removes a grocery from storage based on name
   * @param groceryToRemove name of grocery to be removed
   * @param amount the amount to be removed
   */

  //REMEMBER THIS LOGIC. YOU WILL WANT TO USE ITERATOR!!!! THIS IS BECAUSE: if you sort the values of f.ex milk after soonest coming expiryDate, you will naturally want to remove the amount first for the one with soonest coming expiryDate
  //You will also want it to iterate to the next value IF you remove more than what is present int the first milk
    public void removeAmountFromStorage(Grocery groceryToRemove, double amount) {
      List<Grocery> itemsToRemove = storage.get(groceryToRemove.getName()); //Get only the values in storage for the parameter GroceryToRemove
      Iterator<Grocery> it = itemsToRemove.iterator();

      while (it.hasNext() && amount > 0) {
        Grocery item = it.next();
        double currentAmount = item.getAmount();

        if (currentAmount <= amount) {
          amount -= currentAmount;
          it.remove();
        }
        else {
          item.decreaseAmount(amount);
          amount = 0;
        }
      }

      if (itemsToRemove.isEmpty()) {
        storage.remove(groceryToRemove.getName());
        System.out.println("nothing to remove, you are out of: " + groceryToRemove.getName());
      }
    }


    //REMEMBER SORT STORAGE METHOD, AND DOCUMENTATION OF TREEMAP
    public Map<String, List<Grocery>> sortGroceries() {
      Map<String, List<Grocery>> SortedMap = new TreeMap<>();

      for (String key : storage.keySet()) {
        List<Grocery> items = storage.get(key);
        SortedMap.put(key, items);
      }
      return SortedMap;
    }

  /**
   * Checks if a grocery is in storage
   * @param groceryInStorage name of grocery to be removed
   */
    public Grocery inStorage(Grocery groceryInStorage) {
      return storage.values().stream()
          .flatMap(List::stream)
          .filter(inStorage -> inStorage.equals(groceryInStorage))
          .findFirst()
          .orElse(null);
    }

    public List<Grocery> bestBefore(LocalDate date) {
      return storage.values().stream()
          .flatMap(List::stream)
          .filter(bestBefore -> bestBefore.getExpiryDate().isBefore(date))
          .toList();
    }

    public double TotalValueOfGroceries() {
      return storage.values().stream()
          .flatMap(List::stream)
          .mapToDouble(Grocery::totalValueOfGrocery)
          .sum();
    }

   //public void expirySoon {
    //TODO: add a method here;



  @Override
  public String toString() {
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    StringBuilder sb = new StringBuilder();

    for (Map.Entry<String, List<Grocery>> entry : storage.entrySet()) {
      String groceryName = entry.getKey();
      List<Grocery> groceryList = entry.getValue();

      sb.append(groceryName.toUpperCase()).append(":\n");
      sb.append(String.format("%-20s %-8s %-15s\n", "Name", "Amount", "Expiry Date"));
      sb.append("-------------------------------------------------\n");

      for (Grocery grocery : groceryList) {
        sb.append(String.format("%-20s %-8.2f %-15s\n", grocery.getName(), grocery.getAmount(), dateFormat.format(grocery.getExpiryDate())));
      }
      sb.append("\n");
    }

    return sb.toString();
  }
}

//
