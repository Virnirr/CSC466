package AprioriAlgorithm;

import java.util.Objects;

public class Rule {
    ItemSet left, right;
    public Rule(ItemSet left, ItemSet right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return Objects.equals(left, rule.left) && Objects.equals(right, rule.right);
    }

    @Override
    public String toString() {
        return left + "->" + right;
    }
}
