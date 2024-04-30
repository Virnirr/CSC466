package AprioriAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemSet {
    private final ArrayList<Integer> items;
    public ItemSet(List<Integer> items) {
        this.items = new ArrayList<>(items);
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

    public void addSet(ItemSet items) {
        this.items.addAll(items.getItems());
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
