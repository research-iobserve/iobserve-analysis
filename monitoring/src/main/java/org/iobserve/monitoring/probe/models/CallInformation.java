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
package org.iobserve.monitoring.probe.models;

/**
 * Represents a additional information of a call.
 *
 * @author Christoph Dornieden
 *
 */

public class CallInformation {
    private String informationSignature;
    private long informationCode;

    /**
     * base constructor
     */
    public CallInformation() {
        // nothing
    }

    /**
     * constructor
     *
     * @param informationSignature
     *            informationSignature
     * @param informationCode
     *            informationCode
     */
    public CallInformation(final String informationSignature, final long informationCode) {
        super();
        this.informationSignature = informationSignature;
        this.informationCode = informationCode;
    }

    /**
     * getter
     *
     * @return informationSignature
     */
    public String getInformationSignature() {
        return this.informationSignature;
    }

    /**
     * setter
     *
     * @param informationSignature
     *            informationSignature
     */
    public void setInformationSignature(final String informationSignature) {
        this.informationSignature = informationSignature;
    }

    /**
     * getter
     *
     * @return informationCode
     */
    public long getInformationCode() {
        return this.informationCode;
    }

    /**
     * setter
     *
     * @param informationCode
     *            informationCode
     */
    public void setInformationCode(final long informationCode) {
        this.informationCode = informationCode;
    }

}