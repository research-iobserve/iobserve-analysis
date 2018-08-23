/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.test.userbehavior;

/**
 * Contains the metrics of the modeling accuracy: Jaccard Coefficient and Spearman Rank Coreelation
 * Coefficient.
 *
 * @author David Peter, Robert Heinrich
 */
public class AccuracyResults {

    private double jc;
    private double srcc;

    /**
     * Default constructor.
     */
    public AccuracyResults() {
        // nothing to be done here
    }

    public double getJc() {
        return this.jc;
    }

    public void setJc(final double jc) {
        this.jc = jc;
    }

    public double getSrcc() {
        return this.srcc;
    }

    public void setSrcc(final double srcc) {
        this.srcc = srcc;
    }

}
