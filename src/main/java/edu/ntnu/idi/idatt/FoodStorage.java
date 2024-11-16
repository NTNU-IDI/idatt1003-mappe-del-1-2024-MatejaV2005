package edu.ntnu.idi.idatt;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;


public class FoodStorage {
  private Map<String, List<Grocery>> storage; /*a hashmap with a String key and a List (defined as list for flecibilty) as values                                           The idea here is that if the user adds for instance milks with different expiry dates, but both have the Key milk, they won't override eachother*/
  private double TotalValue;

  public FoodStorage() {
      this.storage = new HashMap<>();
      this.TotalValue = 0.0;
    }

  /**
   * adds a grocery to storage based on name
   * @param groceryToAdd name of grocery to be added to storage
   */
    public void registerToStorage(Grocery groceryToAdd) {
      storage.computeIfAbsent(groceryToAdd.getName(), toList -> new ArrayList<>()).add(groceryToAdd);
      //REMEMBER: add code so that if user adds same grocery, the amount for that grocery increases

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
   */

  //REMEMBER THIS LOGIC. YOU WILL WANT TO USE ITERATOR!!!! THIS IS BECAUSE: if you sort the values of f.ex milk after soonest coming expiryDate, you will naturally want to remove the amount first for the one with soonest coming expiryDate
  //You will also want it to iterate to the next value IF you remove more than what is present int the first milk
    public void removeAmountFromStorage(Grocery groceryToRemove, double amount) {
      List<Grocery> items = storage.get(groceryToRemove.getName()); //Get only the values in storage for the parameter GroceryToRemove
      Iterator<Grocery> it = items.iterator();

      if (items != null) {
        items.remove(groceryToRemove); //removes specific instance of parameter
        if (items.isEmpty()) { // if list is empty, remove key
          storage.remove(groceryToRemove.getName());
        }
      }
    }

    //REMEMBER SORT STORAGE METHOD
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
      List<Grocery> expiredGroceries = storage.values().stream()
          .flatMap(List::stream)
          .filter(bestBefore -> bestBefore.getExpiryDate().isBefore(date))
          .toList();

      return expiredGroceries;
    }

  //get total value
  //REMEMBER THESE METHODS!!!!!!!!!!!!!!!!!!!!!

    public HashMap<String, List<Grocery>> getGroceryList() {
      // use java streams here, ex. ForEach to print out each
      // Should i have a print method here? potentially have a toString but not a print Method
      return null;
    }
}

//
