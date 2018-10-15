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

package org.iobserve.analysis.test.behavior.clustering.hierarchical;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.net4j.util.collection.Pair;
import org.iobserve.analysis.behavior.clustering.hierarchical.AvgSilhouetteMethod;
import org.iobserve.analysis.behavior.clustering.hierarchical.CSVSinkFilter;
import org.iobserve.analysis.behavior.clustering.hierarchical.ElbowMethod;
import org.iobserve.analysis.behavior.clustering.hierarchical.GapStatisticMethod;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import weka.clusterers.HierarchicalClusterer;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * This class performs several JUnit tests for all main implemented classes. It tests all clustering
 * methods using simple input data which tests the ClusteringResultsBuilder as well. Additionally it
 * tests the CSVSinkFilter.
 *
 * @author SL
 * @since 0.0.3
 *
 *        TODO path issue
 */
@Ignore
public class JUnitTestsHierarchicalClustering {

    /* Set value of CSVOUTPUTPATH accordingly */
    private static final String CSVOUTPUTPATH = "/Users/SL/git/single-jpetstore-clustering-experiment/results";
    private static final String EXPECTEDFIRSTLINECSVTWOCLUSTERS = "# of cluster,2";
    private static final String EXPECTEDLASTLINECSVTWOCLUSTERS = "1,1,[[1000,1200],]";

    private Instances testInstancesOneCluster;
    private Map<Integer, List<Pair<Instance, Double>>> expectedResultsOneCluster;
    private Instances testInstancesTwoClusters;
    private Map<Integer, List<Pair<Instance, Double>>> expectedResultsTwoCluster;

    /**
     * Create test data sets and their expected clustering results.
     *
     * @throws IOException
     *             when failing to write a CSV file to CSVOUTPUTPATH
     */
    @Before
    public void setupTestData() throws IOException {
        // Create Attribute names for the test data sets.
        final FastVector attVector = new FastVector(1);
        attVector.addElement(new Attribute("Attribute1"));
        attVector.addElement(new Attribute("Attribute2"));

        // Create a data set with two similar instances i1 and i2 with two attributes.
        final Instance i1one = new Instance(1.0, new double[] { 1.0, 2.0 });
        final Instance i2one = new Instance(1.0, new double[] { 1.5, 2.1 });
        final Instances instancesOne = new Instances("Instances", attVector, 2);
        instancesOne.add(i1one);
        instancesOne.add(i2one);
        i1one.setDataset(instancesOne);
        i2one.setDataset(instancesOne);
        this.setTestInstancesOneCluster(instancesOne);

        // Create expected result.
        final Map<Integer, List<Pair<Instance, Double>>> expResOne = new HashMap<>();
        expResOne.put(0, new LinkedList<Pair<Instance, Double>>());
        expResOne.get(0).add(new Pair<>(i1one, 1.0));
        expResOne.get(0).add(new Pair<>(i2one, 1.0));
        this.setExpectedResultsOneCluster(expResOne);

        /*
         * Create a data set with two similar instances i1 and i2 and one different instance i3 with
         * two attributes.
         */
        final Instance i1two = new Instance(1.0, new double[] { 1.0, 2.0 });
        final Instance i2two = new Instance(1.0, new double[] { 1.5, 2.1 });
        final Instance i3two = new Instance(1.0, new double[] { 1000.0, 1200.0 });
        final Instances instancesTwo = new Instances("Instances", attVector, 3);
        instancesTwo.add(i1two);
        instancesTwo.add(i2two);
        instancesTwo.add(i3two);
        i1two.setDataset(instancesTwo);
        i2two.setDataset(instancesTwo);
        i3two.setDataset(instancesTwo);
        this.setTestInstancesTwoClusters(instancesTwo);

        // Create expected result.
        final Map<Integer, List<Pair<Instance, Double>>> expResTwo = new HashMap<>();
        expResTwo.put(0, new LinkedList<Pair<Instance, Double>>());
        expResTwo.put(1, new LinkedList<Pair<Instance, Double>>());
        expResTwo.get(0).add(new Pair<>(i1two, 1.0));
        expResTwo.get(0).add(new Pair<>(i2two, 1.0));
        expResTwo.get(1).add(new Pair<>(i3two, 1.0));
        this.setExpectedResultsTwoCluster(expResTwo);

        // Write a CSV file for a clustering result which expects two clusters.
        final CSVSinkFilter csvFilter = new CSVSinkFilter();
        final Map<Double, List<Instance>> clusteringKVs = csvFilter.convertClusteringResultsToKVPair(expResTwo);
        csvFilter.createCSVFromClusteringResult(JUnitTestsHierarchicalClustering.CSVOUTPUTPATH, clusteringKVs);

    }

    /*------------------------*/
    /* Tests for Elbow Method */
    /*------------------------*/
    /**
     * Test Elbow Method with empty input list.
     *
     * @result Elbow Method works correctly with an empty input data list.
     */
    @Test // (expected = Exception.class)
    public void testElbowMethodWithEmptyInput() {
        // Perform hierarchical clustering using the Elbow Method on the data set.
        final ElbowMethod elbowMethod = new ElbowMethod(new HierarchicalClusterer(), null);
        final Map<Integer, List<Pair<Instance, Double>>> elbowResults = elbowMethod.analyze();
        Assert.assertEquals(new HashMap<>(), elbowResults);
    }

    /**
     * Test Elbow Method with some similar instances as input list.
     *
     * @result Elbow Method works correctly with an input data list with similar data.
     */
    @Test
    public void testElbowMethodWithSomeInstances() {

        // Perform hierarchical clustering using the Elbow Method on the data set.
        final ElbowMethod elbowMethod = new ElbowMethod(new HierarchicalClusterer(), this.testInstancesOneCluster);
        final Map<Integer, List<Pair<Instance, Double>>> elbowResults = elbowMethod.analyze();

        // Check if the clustering results equal the expected results.
        final boolean success = this.checkEqual(elbowResults, this.expectedResultsOneCluster);
        if (!success) {
            Assert.fail();
        }
    }

    /**
     * Test Elbow Method with some different instances as input list.
     *
     * @result Elbow Method works correctly with an input data list with similar and non-similar
     *         data.
     */
    @Test
    public void testElbowMethodWithDifferentInstances() {

        // Perform hierarchical clustering using the Elbow Method on the data set.
        final ElbowMethod elbowMethod = new ElbowMethod(new HierarchicalClusterer(), this.testInstancesTwoClusters);
        final Map<Integer, List<Pair<Instance, Double>>> elbowResults = elbowMethod.analyze();

        // Check if the clustering results equal the expected results.
        final boolean success = this.checkEqual(elbowResults, this.expectedResultsTwoCluster);
        if (!success) {
            Assert.fail();
        }
    }

    /*------------------------*/
    /* Tests for Average Silhouette Method */
    /*------------------------*/
    /**
     * Test Average Silhouette Method with empty input list.
     *
     * @result Average Silhouette Method works correctly with an empty input data list.
     */
    @Test
    public void testAvgSilMethodWithEmptyInput() {
        // Perform hierarchical clustering using the Average Silhouette Method on the data set.
        final AvgSilhouetteMethod avgSilMethod = new AvgSilhouetteMethod(new HierarchicalClusterer(), null);
        final Map<Integer, List<Pair<Instance, Double>>> avgSilResults = avgSilMethod.analyze();
        Assert.assertEquals(new HashMap<>(), avgSilResults);
    }

    /**
     * Test Average Silhouette Method with some instances as input list.
     *
     * @result Average Silhouette Method works correctly with an input data list with similar data.
     */
    @Test
    public void testAvgSilMethodWithSomeInstances() {

        // Perform hierarchical clustering using the Average Silhouette Method on the data set.
        final AvgSilhouetteMethod avgSilMethod = new AvgSilhouetteMethod(new HierarchicalClusterer(),
                this.testInstancesOneCluster);
        final Map<Integer, List<Pair<Instance, Double>>> avgSilResults = avgSilMethod.analyze();

        // Check if the clustering results equal the expected results.
        final boolean success = this.checkEqual(avgSilResults, this.expectedResultsOneCluster);
        if (!success) {
            Assert.fail();
        }
    }

    /**
     * Test Average Silhouette Method with some different instances as input list.
     *
     * @result Average Silhouette Method works correctly with an input data list with similar and
     *         non-similar data.
     */
    @Test
    public void testAvgSilMethodWithDifferentInstances() {

        // Perform hierarchical clustering using the Elbow Method on the data set.
        final AvgSilhouetteMethod avgSilMethod = new AvgSilhouetteMethod(new HierarchicalClusterer(),
                this.testInstancesTwoClusters);
        final Map<Integer, List<Pair<Instance, Double>>> avgSilResults = avgSilMethod.analyze();

        // Check if the clustering results equal the expected results.
        final boolean success = this.checkEqual(avgSilResults, this.expectedResultsTwoCluster);
        if (!success) {
            Assert.fail();
        }
    }

    /*--------------------------------*/
    /* Tests for Gap Statistic Method */
    /*--------------------------------*/
    /**
     * Test Gap Statistic Method with empty input list.
     *
     * @result Gap Statistic Method works correctly with an empty input data list.
     */
    @Test
    public void testGapMethodWithEmptyInput() {
        // Perform hierarchical clustering using the Gap Statistic Method on the data set.
        final GapStatisticMethod gapMethod = new GapStatisticMethod(new HierarchicalClusterer(), null);
        final Map<Integer, List<Pair<Instance, Double>>> gapResults = gapMethod.analyze();
        Assert.assertEquals(new HashMap<>(), gapResults);
    }

    /**
     * Test Gap Statistic Method with some instances as input list.
     *
     * @result Gap Statistic Method works correctly with an input data list with similar data.
     */
    @Test
    public void testGapMethodWithSomeInstances() {

        // Perform hierarchical clustering using the Gap Statistic Method on the data set.
        final GapStatisticMethod gapMethod = new GapStatisticMethod(new HierarchicalClusterer(),
                this.testInstancesOneCluster);
        final Map<Integer, List<Pair<Instance, Double>>> gapResults = gapMethod.analyze();

        // Check if the clustering results equal the expected results.
        final boolean success = this.checkEqual(gapResults, this.expectedResultsOneCluster);
        if (!success) {
            Assert.fail();
        }
    }

    /*---------------------*/
    /* Tests CSVSinkFilter */
    /*--------------- -----*/
    /**
     * Tests CSVSinkFilter by checking if there is a file with the correct name at the output path
     * and checks the first and the last list of the file for the expected values.
     *
     * @result CSVSinkFilter writes the clustering results correctly into a csv file at the given
     *         output path.
     * @throws FileNotFoundException
     *             if the csv file is not found
     */
    @Test
    public void testCSVFilterSink() throws FileNotFoundException {
        final String fileName = JUnitTestsHierarchicalClustering.CSVOUTPUTPATH + "/hierarchResults.csv";
        // Check if csv file exists.
        final File f = new File(fileName);
        if (!f.exists()) {
            Assert.fail();
        }
        // Check first life on the CSV file.
        final List<String[]> records = new ArrayList<>();
        final BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            final String firstLine = br.readLine();
            String lastLine = "";
            String tmp = "";
            while ((tmp = br.readLine()) != null) {
                lastLine = tmp;
            }
            if (!(firstLine.equals(JUnitTestsHierarchicalClustering.EXPECTEDFIRSTLINECSVTWOCLUSTERS)
                    && lastLine.equals(JUnitTestsHierarchicalClustering.EXPECTEDLASTLINECSVTWOCLUSTERS))) {
                Assert.fail();
            }

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if test results and expected results are equal.
     *
     * @param clusteringRestults
     *            Actual clustering results of the chosen clustering method.
     * @param expectedResults
     *            Expected clustering results for the given input data.
     * @return Boolean value whether the values of the clustering result is equal to the values of
     *         the expected clustering result.
     */
    private boolean checkEqual(final Map<Integer, List<Pair<Instance, Double>>> clusteringRestults,
            final Map<Integer, List<Pair<Instance, Double>>> expectedResults) {

        // Both lists must have the same length, otherwise they cannot be equal.
        if (expectedResults.size() != clusteringRestults.size()) {
            return false;
        }
        // Check if the attribute values of each entry are the same.
        boolean isEqual = true;
        for (final Map.Entry<Integer, List<Pair<Instance, Double>>> entry : clusteringRestults.entrySet()) {
            final List<Pair<Instance, Double>> lRes = entry.getValue();
            final List<Pair<Instance, Double>> lExp = expectedResults.get(entry.getKey());
            for (int i = 0; i < lRes.size(); i++) {
                final double[] pRes = lRes.get(i).getElement1().toDoubleArray();
                final double[] pExp = lExp.get(i).getElement1().toDoubleArray();
                if (!Arrays.equals(pRes, pExp)) {
                    isEqual = false;
                }
                if (!isEqual) {
                    break;
                }
            }
        }
        return isEqual;
    }

    /* Getter and Setter for test data sets. */
    public Instances getTestInstancesOneCluster() {
        return this.testInstancesOneCluster;
    }

    public void setTestInstancesOneCluster(final Instances testInstances) {
        this.testInstancesOneCluster = testInstances;
    }

    public Map<Integer, List<Pair<Instance, Double>>> getExpectedResultsOneCluster() {
        return this.expectedResultsOneCluster;
    }

    public void setExpectedResultsOneCluster(final Map<Integer, List<Pair<Instance, Double>>> expectedResults) {
        this.expectedResultsOneCluster = expectedResults;
    }

    public Instances getTestInstancesTwoClusters() {
        return this.testInstancesTwoClusters;
    }

    public void setTestInstancesTwoClusters(final Instances testInstancesTwoClusters) {
        this.testInstancesTwoClusters = testInstancesTwoClusters;
    }

    public Map<Integer, List<Pair<Instance, Double>>> getExpectedResultsTwoCluster() {
        return this.expectedResultsTwoCluster;
    }

    public void setExpectedResultsTwoCluster(
            final Map<Integer, List<Pair<Instance, Double>>> expectedResultsTwoCluster) {
        this.expectedResultsTwoCluster = expectedResultsTwoCluster;
    }
}
