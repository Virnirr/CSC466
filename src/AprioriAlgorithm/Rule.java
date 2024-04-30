package AprioriAlgorithm;

import java.util.ArrayList;
import java.util.Objects;

public class Rule {
    ItemSet left, right, combined;
    public Rule(ItemSet left, ItemSet right) {
        this.left = left;
        this.right = right;

        // add combined to calculate Confidence score
        this.combined = new ItemSet(new ArrayList<>());
        this.combined.addSet(left);
        this.combined.addSet(right);
    }

    @Override
    public String toString() {
        return left + "->" + right;
    }
}
