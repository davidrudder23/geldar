package org.noses.game.character.inventory;

import org.noses.game.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Inventory {

    List<Item> itemsInInventory;

    public Inventory() {
        itemsInInventory = new ArrayList<>();

    }

    public boolean contains(Item item) {
        return itemsInInventory.contains(item);
    }

    public void add(Item item) {
        itemsInInventory.add(item);
    }

    public boolean remove(Item item) {
        return itemsInInventory.remove(item);
    }

    public List<Item> getItemsInInventory() {
        return itemsInInventory;
    }

    public HashMap<String, List<Item>> getSortedInventory() {
        HashMap<String, List<Item>> sortedInventory = new HashMap<>();


        for (Item item: itemsInInventory) {
            List<Item> itemsOfThisType = sortedInventory.get(item.getItemName());
            if (itemsOfThisType == null) {
                itemsOfThisType = new ArrayList<>();
            }

            itemsOfThisType.add(item);
            sortedInventory.put(item.getItemName(), itemsOfThisType);
        }

        return sortedInventory;
    }

    public void setItemsInInventory(List<Item> itemsInInventory) {
        this.itemsInInventory = itemsInInventory;
    }
}
