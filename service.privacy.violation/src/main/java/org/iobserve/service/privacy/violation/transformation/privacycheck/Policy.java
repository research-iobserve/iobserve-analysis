package org.iobserve.service.privacy.violation.transformation.privacycheck;

import org.iobserve.model.privacy.EDataPrivacyLevel;

/**
 *
 * This class represents the common policy
 *
 * @author Eric Schmieders
 */

public class Policy {

    public static enum GEOLOCATION {
        DEU, USA, RUS
    }

    public static enum DATACLASSIFICATION {
        ANONYMOUS, DEPERSONALIZED, PERSONAL
    }

    private final DATACLASSIFICATION dataClassification;
    private final GEOLOCATION geoLocation;

    protected Policy(final DATACLASSIFICATION dataClassification, final GEOLOCATION geoLocation) {
        this.geoLocation = geoLocation;
        this.dataClassification = dataClassification;
    }

    public static boolean isEqualOrMoreCritical(final DATACLASSIFICATION basis, final DATACLASSIFICATION compared) {
        if (compared.ordinal() >= basis.ordinal()) {
            return true;
        }

        return false;
    }

    public DATACLASSIFICATION getDataClassification() {
        return this.dataClassification;
    }

    public GEOLOCATION getGeoLocation() {
        return this.geoLocation;
    }

    public static DATACLASSIFICATION getDataClassification(final EDataPrivacyLevel level) {
        for (final DATACLASSIFICATION data : DATACLASSIFICATION.values()) {
            if (data.name().toLowerCase().equals(level.getName().toLowerCase())) {
                return data;
            }
        }
        return null;
    }

    public String getPrint() {
        return "!(" + this.dataClassification + "," + this.geoLocation + ")";
    }
}