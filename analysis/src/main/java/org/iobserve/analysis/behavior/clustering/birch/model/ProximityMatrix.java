/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.behavior.clustering.birch.model;

import java.util.List;

/**
 * Auxiliary class for computing a distance matrix for list of ClusteringFeatures.
 *
 * @author melf
 *
 */
class ProximityMatrix {

    private final double[][] matrix;
    private final int n;
    private final int[] farthestPair = new int[2];
    private final int[] closestPair = new int[2];

    /**
     * Constructor.
     * 
     * @param cfs
     *            clustering features to create distance matrix of
     */
    ProximityMatrix(final List<ClusteringFeature> cfs) {
        this.n = cfs.size();
        this.matrix = new double[this.n][this.n];
        double max = 0.0;
        double min = Double.MAX_VALUE;

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < i; j++) {
                this.matrix[i][j] = cfs.get(i).compare(cfs.get(j));
                this.matrix[j][i] = this.matrix[i][j];
                if (max < this.matrix[i][j]) {
                    max = this.matrix[i][j];
                    this.farthestPair[0] = i;
                    this.farthestPair[1] = j;
                }
                if (min > this.matrix[i][j]) {
                    min = this.matrix[i][j];
                    this.closestPair[0] = i;
                    this.closestPair[1] = j;
                }
            }
        }
    }

    boolean isClosestPair(final int i, final int j) {
        return (i == this.closestPair[0] && j == this.closestPair[1])
                | (j == this.farthestPair[0] && i == this.farthestPair[1]);
    }

    int[] getFarthestPair() {
        return this.farthestPair;
    }

    int[] getClosestPair() {
        return this.closestPair;
    }

    public double[][] getMatrix() {
        return this.matrix;
    }

    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                string += String.format("%.2f", this.matrix[i][j]);
            }
            string += "\n";
        }
        string += "Farthest: " + this.farthestPair[0] + "," + this.farthestPair[1];
        string += "Closest: " + this.closestPair[0] + "," + this.closestPair[1];
        return string;
    }
}
