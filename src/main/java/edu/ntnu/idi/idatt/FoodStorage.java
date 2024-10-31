package edu.ntnu.idi.idatt;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class FoodStorage {
  private HashMap<String, List<Grocery>> storage; /*a hashmap with a String key and a List (defined as list for flecibilty) as values
                                                     The idea here is that if the user adds for instance milks with different expiry dates, but both have the Key milk, they won't override eachother*/
  private double TotalValue;

  public FoodStorage() {
      this.storage = new HashMap<>();
      this.TotalValue = 0.0;
    }

  //grocery toAdd = new grocery("melk", 30.0, 4.0, "liter", 30-11-2025)
    public void addToStorage(Grocery groceryToAdd) {
      storage.putIfAbsent(groceryToAdd.getName(), new ArrayList<>()); // here we set the key and type of value for a grocery so that it knows what it will contain. I use putIfAbsent to only name the key of the arraylist ONCE to avoid overriding
      storage.get(groceryToAdd.getName()).add(groceryToAdd); //actually adds the grocery
    }

    //grocery toRemove = toAdd
    public void removeFromStorage(Grocery groceryToRemove) {
      //Må ha logikk for å fjerne en enkel item fra listen for en spesifikk key i storage
      List<Grocery> items = storage.get(groceryToRemove.getName()); //henter bare ut values i storage til newGrocery
      if (items != null) {
        items.remove(groceryToRemove); //fjerner sepsifikk instans av parameteren
        if (items.isEmpty()) { // hvis listen blir tom, fjern hele keyen
          storage.remove(groceryToRemove.getName());
        }
      }
    }

    public boolean inStorage(Grocery groceryInStorage) {
      List<Grocery> items = storage.get(groceryInStorage.getName()); // returnerer en liste med Grocery-objekter, av den keyen som blir henta ut
      if(items.contains(storage.get(groceryInStorage.getName()))) {
        return true;
      }
      else {
        return false;
      }
    }

    public void updateStorage() {
      //logikk for
    }

    public String printStorage() {
      // use java streams here, ex. ForEach to print out each
      return "null";
    }
}
