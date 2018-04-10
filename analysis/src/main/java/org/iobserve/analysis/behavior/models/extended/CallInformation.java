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
package org.iobserve.analysis.behavior.models.extended;

/**
 * Represents single and multiple call information.
 *
 * @author Jannis Kuckei
 *
 */
public class CallInformation {
    private String informationSignature;
    private String informationParameter;
    private int count;

    public CallInformation(final String informationSignature, final String informationParameter, final int count) {
        this.informationSignature = informationSignature;
        this.informationParameter = informationParameter;
        this.count = count;
    }

    public CallInformation(final String informationSignature, final String informationParameter) {
        this(informationSignature, informationParameter, 1);
    }

    public String getInformationSignature() {
        return this.informationSignature;
    }

    public void setInformationSignature(final String informationSignature) {
        this.informationSignature = informationSignature;
    }

    public String getInformationParameter() {
        return this.informationParameter;
    }

    public void setInformationParameter(final String informationParameter) {
        this.informationParameter = informationParameter;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(final int count) {
        this.count = count;
    }

    public void addCount(final int increase) {
        this.count += increase;
    }

    public boolean hasSameParameters(final CallInformation c) {
        return this.informationSignature == c.informationSignature
                && this.informationParameter == c.informationParameter;
    }
}
