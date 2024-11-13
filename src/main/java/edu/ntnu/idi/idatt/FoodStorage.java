package edu.ntnu.idi.idatt;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FoodStorage {
  private HashMap<String, List<Grocery>> storage; /*a hashmap with a String key and a List (defined as list for flecibilty) as values
                                                     The idea here is that if the user adds for instance milks with different expiry dates, but both have the Key milk, they won't override eachother*/
  private double TotalValue;

  public FoodStorage() {
      this.storage = new HashMap<>();
      this.TotalValue = 0.0;
    }

  /**
   * adds a grocery to storage based on name
   * @param groceryToAdd name of grocery to be added to storage
   */
    public void addToStorage(Grocery groceryToAdd) {
      storage.computeIfAbsent(groceryToAdd.getName(), k -> new ArrayList<>()).add(groceryToAdd);
    }

    // MAYBE: use stream() logic here?

  /**
   * Removes a grocery from storage based on name
   * @param groceryToRemove name of grocery to be removed
   */
    public void removeFromStorage(Grocery groceryToRemove) {
      List<Grocery> items = storage.get(groceryToRemove.getName()); //Get only the values in storage for the parameter GroceryToRemove
      if (items != null) {
        items.remove(groceryToRemove); //removes specific instance of parameter
        if (items.isEmpty()) { // if list is empty, remove key
          storage.remove(groceryToRemove.getName());
        }
      }
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


    public HashMap<String, List<Grocery>> printStorage() {
      // use java streams here, ex. ForEach to print out each
      // Should i have a print method here? potentially have a toString but not a print Method
      return null;
    }
}

//
