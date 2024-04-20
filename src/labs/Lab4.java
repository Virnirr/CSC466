package labs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Lab4 {
    public static void main(String[] args) throws FileNotFoundException {
        HashMap<Integer, ArrayList<Integer>>adjacency_list = new HashMap<>();
        HashSet<Integer> nodes = new HashSet<>();
        HashMap<Integer, Integer> outgoing_links = new HashMap<Integer, Integer>();

        HashMap<Integer, Double> pageRankOld = new HashMap<>();
        HashMap<Integer, Double> pageRankNew = new HashMap<>();

        try {
            File file = new File("/Users/zhihe/CSC466/labs/src/labs/graph.txt");
            Scanner scan_file = new Scanner(file);

            String[] nodes_tokenized;

            while (scan_file.hasNextLine()) {
                nodes_tokenized = scan_file.nextLine().split(",");
                int from_node = Integer.parseInt(nodes_tokenized[0]);
                int to_node = Integer.parseInt(nodes_tokenized[2]);
                // Add the "to" node to the adjacency list of the "from" node
                // Keep track of the outgoing links for the "from" node

                // add to adjacency list to define node <-- incoming
                adjacency_list.computeIfAbsent(to_node, k -> new ArrayList<Integer>());
                // Add the edge to the adjacency list only if it doesn't already exist
                if (!adjacency_list.get(to_node).contains(from_node)) {
                    adjacency_list.get(to_node).add(from_node);
                }
                outgoing_links.put(from_node, outgoing_links.getOrDefault(from_node, 0) + 1);

                // keep track of all nodes including incoming and outgoing links
                nodes.add(from_node);
                nodes.add(to_node);
            }

            // pageRank0 1/|V|
            for (Integer pageEntry : nodes) {
                pageRankOld.put(pageEntry, 1.0 / (double) nodes.size());
            }

            double L1Norm;
            int iter = 1;
            double d = 0.9;
            do {
                for (Integer pageEntry : nodes) {
                    // (1-d) / (1/|V|)
                    double page_rank = (1.0-d) / (double) nodes.size();
                    // sum of all pageRankOld with new iteration of incoming links
                    if (adjacency_list.get(pageEntry) != null) {
                        for (Integer incoming_link : adjacency_list.get(pageEntry)) {
                            double incoming_link_prestige = (pageRankOld.get(incoming_link) / outgoing_links.get(incoming_link));
                            page_rank += (d * incoming_link_prestige);
                        }
                    }
                    else {
                        page_rank = (1.0-d) / (double) nodes.size();
                    }
                    pageRankNew.put(pageEntry, page_rank);
                }

                // Normalize the pageRank
                normalizePageRank(pageRankNew);
                L1Norm = findDistance(pageRankNew, pageRankOld);

                pageRankOld = (HashMap<Integer, Double>) pageRankNew.clone();

            } while (L1Norm >= 0.001);

            ArrayList<Integer> top20links = pageRankOld.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(20)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toCollection(ArrayList::new));

            ArrayList<Integer> expected_output = new ArrayList<>(Arrays.asList(1159, 1293, 155, 55, 1051, 641, 729, 1153, 855, 323, 1245, 1260, 798, 1112, 1461, 963, 1463, 1306, 1179, 535));
            System.out.println(top20links);
            System.out.println(expected_output.equals(top20links));

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void normalizePageRank(HashMap<Integer, Double> pageRank) {
        // normalize the pageRank by dividing every value by the total sum
        double total_score = pageRank.values().stream().mapToDouble(Double::doubleValue).sum();
        for(Map.Entry<Integer, Double> entry : pageRank.entrySet()) {
            double normalized_form = entry.getValue() / total_score;
            pageRank.put(entry.getKey(), normalized_form);
        }
    }

    public static double findDistance(HashMap<Integer, Double> pageRankOld, HashMap<Integer, Double> pageRankNew) {
        double L1Norm = 0.0;
        for (Map.Entry<Integer, Double> pageEntry : pageRankOld.entrySet()) {
            int docNum = pageEntry.getKey();
            L1Norm += Math.abs(pageRankOld.get(docNum) - pageRankNew.get(docNum));
        }
        return L1Norm;
    }
}
