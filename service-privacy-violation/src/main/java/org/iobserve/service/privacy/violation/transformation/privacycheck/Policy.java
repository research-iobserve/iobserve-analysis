/***************************************************************************
 * Copyright 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.service.privacy.violation.transformation.privacycheck;

import org.iobserve.model.privacy.EDataProtectionLevel;
import org.iobserve.model.privacy.EISOCode;

/**
 *
 * This class represents the common policy.
 *
 * @author Eric Schmieders
 */

public class Policy {

    // TODO should be replaced by the existing model level enumeration if possible
    /** geolocation. */

    // TODO should be replaced by the existing model level enumeration if possible
    /** data classification. */
    public enum EDataClassification {
        ANONYMOUS, DEPERSONALIZED, PERSONAL
    }

    private final EDataClassification dataClassification;
    private final EISOCode eisocode;

    /**
     * Create new policy.
     *
     * @param dataClassification
     *            data classification
     * @param geoLocation
     *            geolocation
     */
    protected Policy(final EDataClassification dataClassification, final EISOCode geoLocation) {
        this.eisocode = geoLocation;
        this.dataClassification = dataClassification;
    }

    /**
     * Check whether the compared classification is equal or higher than the basis.
     *
     * @param basis
     *            the basis
     * @param compared
     *            the new classification
     *
     * @return true if the new classification is higher
     */
    // TODO this is a method which belongs to the enumeration
    public static boolean isEqualOrMoreCritical(final EDataClassification basis, final EDataClassification compared) {
        return compared.ordinal() >= basis.ordinal();
    }

    public EDataClassification getDataClassification() {
        return this.dataClassification;
    }

    public EISOCode getEisocode() {
        return this.eisocode;
    }

    /**
     * Get the data classification for a data privacy level.
     *
     * @param level
     *            data privacy level
     * @return the data classification
     */
    // TODO this is mostly useless, you could EDataPrivacyLevel instead
    public static EDataClassification getDataClassification(final EDataProtectionLevel level) {
        for (final EDataClassification data : EDataClassification.values()) {
            if (data.name().equalsIgnoreCase(level.getName())) {
                return data;
            }
        }
        return null;
    }

    public String getPrint() {
        return "!(" + this.dataClassification + "," + this.eisocode + ")";
    }
}