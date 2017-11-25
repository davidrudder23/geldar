package org.noses.game.character.inventory;

import org.noses.game.item.Item;

import java.util.ArrayList;
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

    public List<Item> getItemsInInventory() {
        return itemsInInventory;
    }

    public void setItemsInInventory(List<Item> itemsInInventory) {
        this.itemsInInventory = itemsInInventory;
    }
}
