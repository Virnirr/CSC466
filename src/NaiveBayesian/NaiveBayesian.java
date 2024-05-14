package NaiveBayesian;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class NaiveBayesian extends Matrix {
    public final int CATEGORY_INDEX = 4;
    public NaiveBayesian(int [][] data) {
        super(data);
    }
    public ArrayList<Integer> findAllRows() {
        //returns all the indices of all rows, e.g., 0,1,... up to the total number of rows -1
        ArrayList<Integer> allRows = new ArrayList<>();
        for (int i = 0; i < this.matrix.length; i++) {
            allRows.add(i);
        }
        return allRows;
    }

    public int getCategoryAttribute() {
        //returns the index of the category attribute
        return CATEGORY_INDEX;
    }


    public double findProb(int[] row, int category) {
        // takes as input the values for a single row, e.g., 5,3,1,2 and the category, e.g. 2.
        // Returns the probability that the row belongs to the category using the Naïve Bayesian model.
        // Pr(C=t|A=m,B=q) =
        // Pr(C=t)*Pr(A=m|C=t)*Pr(B=q|C=t)*Z
        // use the formula that contains λ and set it to 1/n where n is the total number of tuples (150 in our case)
        double λ = 1.0 / 150;

        ArrayList<Integer> allRows = findAllRows();

        // find probability of category
        double n_j = 0;
        for (int i = 0; i < this.matrix.length; i++) {
            if (category == this.matrix[i][this.getCategoryAttribute()]) {
                n_j++;
            }
        }
        double pr_c = n_j / allRows.size();

        // find probability of each value in each attribute
        for (int i = 0; i < row.length; i++) {
            // find total number of different values in current attribute
            int n_ij = 0;
            HashSet<Integer> totalDiffVal = findDifferentValues(i, allRows);
            double m_i = totalDiffVal.size();

            // calculate total row with val and category
            for (int j = 0; j < allRows.size(); j++) {
                if (row[i] == this.matrix[j][i] && this.matrix[j][getCategoryAttribute()] == category) {
                    n_ij++;
                }
            }

            double pr_a_c = (n_ij + λ) / (n_j + (λ * m_i));

            pr_c *= pr_a_c;
        }

        return pr_c;
    }

    public int findCategory(int[] row) {
        //takes as input the values for a single row, e.g., 5,3,1,2.
        // Returns the most probable category of the row using the Naïve Bayesian Model.

        HashSet<Integer> allDiffCategory = findDifferentValues(getCategoryAttribute(), findAllRows());
        double highestProb = 0;
        int categoryWithHighestProb = 0;
        for (Integer category : allDiffCategory) {
            double prob = findProb(row, category);
            if (prob > highestProb) {
                highestProb = prob;
                categoryWithHighestProb = category;
            }
            System.out.printf("For value %d: Probability is: %.15e%n", category, prob);
        }
        return categoryWithHighestProb;
    }
}
