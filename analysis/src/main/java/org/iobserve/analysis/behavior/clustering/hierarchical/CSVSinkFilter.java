package org.iobserve.analysis.behavior.clustering.hierarchical;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.net4j.util.collection.Pair;

import weka.core.Instance;

/**
 *
 * @author SL
 *
 *         Writes a KV-Pair to a CSV file.
 */
public class CSVSinkFilter {

    public CSVSinkFilter() { // NOCS empty constructor
        // empty constructor
    }

    /**
     * Create a csv file with cluster data.
     *
     * @param outputPath
     *            output path of csv file.
     * @param data
     *            Data contains pairs of instance and assigned cluster
     * @throws IOException
     */
    public void createCSVFromClusteringResult(final String outputPath, final Map<Double, Double> data)
            throws IOException {
        if (!data.isEmpty()) {
            final FileWriter fw = new FileWriter(outputPath);
            final BufferedWriter writer = new BufferedWriter(fw);
            writer.append("Buffer");
            writer.append(',');
            writer.append("Attribute");
            writer.append('\n');
            for (final Entry<Double, Double> entry : data.entrySet()) { // Kann man hier Set
                                                                        // nehmen ?????
                writer.append(String.valueOf(entry.getKey()));
                writer.append(',');
                writer.append(String.valueOf(entry.getValue()));
                writer.append('\n');
            }

            writer.flush();
            writer.close();
        }
    }

    public Map<Double, Double> convertClusteringResultsToKVPair(
            final Map<Integer, List<Pair<Instance, Double>>> clusteringResults) {

        final Map<Double, Double> resultKVPair = new HashMap<>();

        for (int i = 0; i < clusteringResults.size(); i++) {
            final List<Pair<Instance, Double>> listOfClusterI = clusteringResults.get(i);
            if (listOfClusterI != null) {
                for (final Pair<Instance, Double> entry : listOfClusterI) {
                    final double value = entry.getElement1().weight(); // Welchen value soll man
                                                                       // hier nehmen?????? WEIGHT
                                                                       // IST HIEr FALSCH
                    resultKVPair.put((double) i, value);
                }
            }
        }

        return resultKVPair;
    }
}
