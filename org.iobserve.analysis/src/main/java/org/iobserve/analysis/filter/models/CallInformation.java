/* Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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

package org.iobserve.analysis.filter.models;

/**
 * Represents a additional Information of a Call
 *
 * @author Christoph Dornieden
 *
 */

public class CallInformation {
    private final String informationSignature;
    private final long informationCode;

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
     * getter
     *
     * @return informationCode
     */
    public long getInformationCode() {
        return this.informationCode;
    }

}