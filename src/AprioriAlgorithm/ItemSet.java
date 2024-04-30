package AprioriAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemSet {
    private final ArrayList<Integer> items;
    public ItemSet(ArrayList<Integer> items) {
        this.items = items;
    }
    public ArrayList<Integer> getItems() {
        return this.items;
    }

    public boolean containsSet(ItemSet itemSet) {
        // checks to see if itemSet contains in the current instance itemSet
        // return true if all items in itemS else flase
        for (Integer item : itemSet.getItems()) {
            if (!this.items.contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemSet itemSet = (ItemSet) o;
        return Objects.equals(items, itemSet.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

    public String toString() {
        return this.items.toString();
    }
}
