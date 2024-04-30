package AprioriAlgorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Lab6 {
    static ArrayList<ItemSet> transactions = new ArrayList<>(); //lists of all itemsets
    static ArrayList<Integer> items = new ArrayList<>(); // lists of all items
    //lists frequent itemsets. E.g., for key=1, store all 1-itemsets, for key=2, all 2-itemsets and so on.
    static HashMap<Integer, ArrayList<ItemSet>> frequentItemSet = new HashMap<>();
    static final String fileName = "/Users/zhihe/CSC466/labs/src/AprioriAlgorithm/shopping_data.txt";
    static final double MINSUP = 0.01;
    static final double MINCONFIDENCE = 0.99;
    static ArrayList<Rule> rules = new ArrayList<>();
    static final String RESULTS = "{1=[[0], [1], [2], [3], [4], [5], [6], [7], [8], [9], [10], [11], [12], [13], [14], [15], [16], [17], [18], [19], [20], [21], [22], [23], [24], [25], [26], [27], [28], [29], [30], [31], [32], [33], [34], [35], [36], [37], [38], [39], [40], [41], [42], [43], [44], [45], [46], [47], [48], [49]], 2=[[0, 2], [0, 46], [1, 19], [2, 46], [3, 18], [3, 35], [4, 9], [5, 14], [5, 22], [5, 31], [5, 42], [7, 11], [7, 15], [7, 37], [7, 45], [7, 49], [9, 13], [9, 45], [11, 37], [11, 45], [12, 31], [12, 36], [12, 48], [14, 22], [14, 44], [15, 49], [16, 32], [16, 45], [17, 29], [17, 47], [18, 35], [23, 24], [23, 33], [23, 40], [23, 41], [23, 42], [23, 43], [24, 40], [24, 41], [24, 43], [27, 28], [29, 47], [31, 36], [31, 48], [32, 45], [33, 42], [36, 48], [37, 45], [40, 41], [40, 43], [41, 43]], 3=[[0, 2, 46], [3, 18, 35], [7, 11, 37], [7, 11, 45], [7, 15, 49], [7, 37, 45], [11, 37, 45], [12, 31, 36], [12, 31, 48], [12, 36, 48], [16, 32, 45], [17, 29, 47], [23, 24, 40], [23, 24, 41], [23, 24, 43], [23, 40, 41], [23, 40, 43], [23, 41, 43], [24, 40, 41], [24, 40, 43], [24, 41, 43], [31, 36, 48], [40, 41, 43]], 4=[[7, 11, 37, 45], [12, 31, 36, 48], [23, 24, 40, 41], [23, 24, 40, 43], [23, 24, 41, 43], [23, 40, 41, 43], [24, 40, 41, 43]], 5=[[23, 24, 40, 41, 43]]}";
    static final String RULERESULTS ="[[32, 45]->[16], [17, 29]->[47], [29, 47]->[17], [23, 41]->[24], [7, 11, 45]->[37], [7, 37, 45]->[11], [11, 37, 45]->[7], [12, 31, 48]->[36], [12, 36, 48]->[31], [31, 36, 48]->[12], [23, 24, 40]->[41], [23, 40, 41]->[24], [24, 40, 41]->[23], [23, 24, 43]->[40], [23, 40, 43]->[24], [24, 40, 43]->[23], [23, 24, 43]->[41], [23, 41, 43]->[24], [24, 41, 43]->[23], [23, 40, 43]->[41], [23, 41, 43]->[40], [40, 41, 43]->[23], [24, 40, 43]->[41], [24, 41, 43]->[40], [40, 41, 43]->[24], [23, 24, 40, 43]->[41], [23, 24, 41, 43]->[40], [23, 24, 43]->[40, 41], [23, 40, 41, 43]->[24], [23, 40, 43]->[24, 41], [23, 41, 43]->[24, 40], [24, 40, 41, 43]->[23], [24, 40, 43]->[23, 41], [24, 41, 43]->[23, 40], [40, 41, 43]->[23, 24]]";

    public static void main(String[] args) {
        // process the file
        process(fileName);

        findFrequentSingleItemSets();

//        frequentItemSet.get(k) == null || frequentItemSet.get(k).size() > 0;
        for (int k = 2; frequentItemSet.get(k) == null; k++) {
            ArrayList<ItemSet> candidates = candidateGen(frequentItemSet.get(k-1), k-1);
            for (ItemSet candidate : candidates) {
                frequentItemSet.computeIfAbsent(k, x -> new ArrayList<>());
                if (isFrequent(candidate)) {
                    frequentItemSet.get(k).add(candidate);
                }
            }
            if (frequentItemSet.get(k) == null) {
                break;
            }
        }
        System.out.println(frequentItemSet);
        assert frequentItemSet.toString().equals(RESULTS);


        generateRules();
        System.out.println(rules);
        assert rules.toString().equals(RULERESULTS);
    }
    public static void generateRules() {
        for (int k = 2; k < frequentItemSet.size() + 1; k++) {
            ArrayList<ItemSet> frequenSet = frequentItemSet.get(k);
            ArrayList<Rule> allAssociationRules = new ArrayList<>();
            for (ItemSet frequenItems : frequenSet) {
                // create subsets
                ArrayList<Integer> items = frequenItems.getItems();
                for (Integer item : items) {
                    ArrayList<Integer> tempItems = (ArrayList<Integer>) items.clone();
                    ItemSet one_item = new ItemSet(Collections.singletonList(item));

                    // remove the item from the list and call it the rest
                    tempItems.remove(tempItems.indexOf(item)); // Remove the item from the copy
                    ItemSet rest_item = new ItemSet(tempItems);

                    // if rule meets minimum confidence requirement, add it to the list.
                    Rule association = new Rule(rest_item, one_item);
                    if (isMinConfidenceMet(association)) {
                        allAssociationRules.add(association);
                    }
                }
            }
            rules.addAll(allAssociationRules);
        }
    }

    public static boolean isMinConfidenceMet(Rule r) {
        // calculate the confidence score of a Rule
        double countCombinedItemSet = 0;
        double countLeftItemSet = 0;
        for (ItemSet item : transactions) {
            if (item.containsSet(r.combined)) {
                countCombinedItemSet++;
            }
            if (item.containsSet(r.left)) {
                countLeftItemSet++;
            }
        }
        // (X U Y).count / X.count
        return (countCombinedItemSet / countLeftItemSet) >= MINCONFIDENCE;
    }


    public static void process(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scan_file = new Scanner(file);

            String[] tokenized_transaction;

            while (scan_file.hasNextLine()) {
                tokenized_transaction = scan_file.nextLine().split(", ");
                ArrayList<Integer> integerList = new ArrayList<>(
                        Arrays.stream(tokenized_transaction, 1, tokenized_transaction.length)
                                .mapToInt(Integer::parseInt)
                                .boxed()
                                .collect(Collectors.toList())
                );
                transactions.add(new ItemSet(integerList));
                items.addAll(integerList);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<ItemSet> candidateGen(ArrayList<ItemSet> itemSets, int k) {
        ArrayList<ItemSet> candidates = new ArrayList<>();
        for (int i = 0; i < itemSets.size(); i++) {
            for (int j = i + 1; j < itemSets.size(); j++) {
                // join and add candiddates
                ItemSet joinedItemSet = joinCandidate(itemSets.get(i), itemSets.get(j), k);
                // make sure they have similar subset when joined
                if (joinedItemSet != null) {
                    boolean containedAlready = false;
                    for (ItemSet candidate : candidates) {
                        if (candidate.containsSet(joinedItemSet)){
                            containedAlready = true;
                            break;
                        }
                    }
                    if (containedAlready) {
                        continue;
                    }
                    // get all combinations of k-1 subsets
                    ArrayList<ItemSet> subset = combinations(joinedItemSet.getItems(), k);
                    candidates.add(joinedItemSet);
                    // if subset does not contain in Fk-1 transaction, then remove it from candidate list
                    // prune
                    for (ItemSet sSet : subset) {
                        if (!containInSubset(itemSets, sSet)) {
                            candidates.remove(candidates.size()-1);
                            break;
                        }
                    }
                }
            }
        }
        return candidates;
    }

    public static boolean containInSubset(ArrayList<ItemSet> itemSets, ItemSet item) {
        // return True if item is in itemSets
        for (ItemSet checkSets : itemSets){
            if (checkSets.containsSet(item)) {
                return true;
            }
        }
        return false;
    }

    static <T> ArrayList<ItemSet> combinations(List<T> list, int n) {
        int length = list.size();
        List<List<T>> result = new ArrayList<>();
        T[] selections = (T[])new Object[n];
        new Object() {
            void select(int start, int index) {
                if (index >= n)
                    result.add(List.of(selections));
                else if (start < length){
                    selections[index] = list.get(start);
                    select(start + 1, index + 1);
                    select(start + 1, index);
                }
            }
        }.select(0, 0);

        ArrayList<ItemSet> subsetItemSet = new ArrayList<>();
        for (List<T> entry : result) {
            ArrayList<Integer> mutableList = (ArrayList<Integer>) new ArrayList<>(entry);
            subsetItemSet.add(new ItemSet(mutableList));
        }

        return subsetItemSet;
    }

    public static ItemSet joinCandidate(ItemSet c1, ItemSet c2, int k) {
        ArrayList<Integer> joinedSet = new ArrayList<>();
        joinedSet.addAll(c1.getItems());
        for (Integer itemC2 : c2.getItems()) {
            if (!joinedSet.contains(itemC2)) {
                joinedSet.add(itemC2);
            }
        }
        if (joinedSet.size() > k+1) {
            return null;
        }
        return new ItemSet(joinedSet);
    }

    public static boolean findFrequentItemSets(int k) {
        //finds all k-itemsets, Returns false if no itemsets were found (precondition k>=2)
        return true;
    }
    public static boolean isFrequent(ItemSet itemSet) {
        //tells if the itemset is frequent, i.e., meets the minimum support

        int total_freq_in_transac = 0;
        for (ItemSet itemS : transactions) {
            if (itemS.containsSet(itemSet)) {
                total_freq_in_transac++;
            }
        }
        double support = (double) total_freq_in_transac / transactions.size();
        return support >= MINSUP;
    }
    public static void findFrequentSingleItemSets() {
        //finds all 1-itemsets main //the main method
        HashSet<Integer> singleItems = new HashSet<>(items);
        frequentItemSet.put(1, new ArrayList<ItemSet>());
        // find all 1-item frequent itemset
        for (Integer item : singleItems) {

            ItemSet newItemSet = new ItemSet(new ArrayList<Integer>(Collections.singletonList(item)));
            if (isFrequent(newItemSet)) {
                frequentItemSet.get(1).add(newItemSet);
            }
        }
    }
}
