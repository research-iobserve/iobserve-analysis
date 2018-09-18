/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package org.iobserve.analysis.behavior.clustering.hierarchical;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
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
 *         Writes the hierarchical clustering results to a CSV file.
 */
public class CSVSinkFilter {

    public CSVSinkFilter() { // NOCS empty constructor
        // empty constructor
    }

    /**
     * Create a csv file with cluster data.
     *
     * @param outputPath
     *            Output path of csv file.
     * @param data
     *            Data contains list of instances and their assigned cluster
     * @throws IOException
     *             In case CSV file writing fails.
     */
    public void createCSVFromClusteringResult(final String outputPath, final Map<Double, List<Instance>> data)
            throws IOException {
        if (!data.isEmpty()) {
            final File file = new File(outputPath + "/hierarchResults.csv");

            // Creates the csv file.
            file.createNewFile();
            final FileWriter fw = new FileWriter(file);
            final BufferedWriter writer = new BufferedWriter(fw);
            writer.append("# of cluster");
            writer.append(',');
            writer.append(String.valueOf(data.size()));
            writer.append('\n');

            // Write names of attributes to the file.
            final Enumeration attributeNames = data.entrySet().iterator().next().getValue().get(0)
                    .enumerateAttributes();
            String attrNameString = "";
            while (attributeNames.hasMoreElements()) {
                String attrName = attributeNames.nextElement().toString();
                attrName = attrName.replace("@attribute '><", "");
                attrName = attrName.replace("' numeric", "");
                attrNameString += attrName;
                attrNameString += ", ";
            }
            writer.append(attrNameString);
            writer.append('\n');

            // Write the different clusters and their assigned instances to the file.
            writer.append("ClusterID");
            writer.append(',');
            writer.append("# of instances");
            writer.append(',');
            writer.append("Assigned instances");
            writer.append('\n');
            for (final Entry<Double, List<Instance>> entry : data.entrySet()) {
                // Write cluster
                writer.append(String.valueOf(entry.getKey().intValue()));
                writer.append(',');
                // Write number of instances in this cluster
                writer.append(String.valueOf(entry.getValue().size()));
                writer.append(',');
                // Write list of instances which are assigned to this cluster.
                writer.append("[");
                for (final Instance instance : entry.getValue()) {
                    writer.append("[");
                    writer.append(String.valueOf(instance));
                    writer.append("],");
                }
                writer.append("]");
                writer.append('\n');
            }

            writer.flush();
            writer.close();
        }
    }

    /**
     * Convert clusteringResults to KV-Pair list that contains all clusters and their assigned
     * instances.
     *
     * @param clusteringResults
     *            Contain the clusters and their assigned instances with a probability. The
     *            probability with hierarchical clustering is always 1.
     * @return KV-Pair of clustering results.
     */
    public Map<Double, List<Instance>> convertClusteringResultsToKVPair(
            final Map<Integer, List<Pair<Instance, Double>>> clusteringResults) {

        final Map<Double, List<Instance>> resultKVPair = new HashMap<>();
        for (int i = 0; i < clusteringResults.size(); i++) {
            final List<Pair<Instance, Double>> listOfClusterI = clusteringResults.get(i);
            if (listOfClusterI != null) {
                // Create a new list without the probability and just the instance.
                final List<Instance> instanceList = new ArrayList<>();
                for (final Pair<Instance, Double> pair : listOfClusterI) {
                    instanceList.add(pair.getElement1());
                }
                resultKVPair.put((double) i, instanceList);
            }
        }
        return resultKVPair;
    }
}
